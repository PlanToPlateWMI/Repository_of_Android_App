/*
 * Copyright 2023 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.plantoplate.ui.registration;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.repository.AuthRepository;
import pl.plantoplate.databinding.EmailConfirmationBinding;
import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.tools.ApplicationStateController;

/**
 * This activity is responsible for handling the email confirmation process.
 */
public class EmailConfirmActivity extends AppCompatActivity implements ApplicationStateController {

    private SharedPreferences prefs;
    private TextView email_info;
    private EditText enter_code;
    private Button confirm_button;
    private TextView resend_code_button;

    /**
     * This method is called when the activity is created.
     * @param savedInstanceState The saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EmailConfirmationBinding email_confirm_view = EmailConfirmationBinding.inflate(getLayoutInflater(), null, false);
        setContentView(email_confirm_view.getRoot());

        initViews(email_confirm_view);
        setClickListeners();
        prefs = getSharedPreferences("prefs", 0);
        setEmailInfoText();
    }

    private void initViews(EmailConfirmationBinding email_confirm_view) {
        email_info = email_confirm_view.skorzystajZLinku;
        enter_code = email_confirm_view.wprowadzKod.getEditText();
        confirm_button = email_confirm_view.buttonZatwierdzenieLink;
        resend_code_button = email_confirm_view.wyLijPono;
    }

    private void setClickListeners() {
        confirm_button.setOnClickListener(this::checkCode);
        resend_code_button.setOnClickListener(this::getNewConfirmCode);
    }

    @SuppressLint("SetTextI18n")
    private void setEmailInfoText() {
        String email = prefs.getString("email", "");
        email_info.setText(email_info.getText() + "\n" + email);
    }

    /**
     * This method is called when the user clicks the get new code button.
     * It is responsible for getting a new code from the server.
     * @param view The view that was clicked.
     */
    public void checkCode(View view) {
        String entered_code = enter_code.getText().toString();
        String correct_code = prefs.getString("code", "");
        if (correct_code.equals(entered_code)) {
            prefs.edit().remove("code").apply();
            startGroupSelectActivity();
        } else {
            showSnackbar(view, "Wprowadzony kod jest niepoprawny");
        }
    }

    /**
     * This method is called when the user clicks the get new code button.
     * It is responsible for getting a new code from the server.
     * @param view The view that was clicked.
     */
    public void getNewConfirmCode(View view) {
        String email = prefs.getString("email", "");
        enter_code.setText("");
        AuthRepository authRepository = new AuthRepository();
        Disposable disposable = authRepository.getEmailConfirmCode(email, "registration")
                .subscribe(
                        response -> prefs.edit().putString("code", response.getCode()).apply(),
                        error -> showSnackbar(view, error.getMessage())
                );
        showSnackbar(view, "Wysłano nowy kod");
    }

    public void startGroupSelectActivity() {
        Intent intent = new Intent(this, GroupSelectActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        saveAppState(ApplicationState.GROUP_CHOOSE);
    }

    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * This method is called when the user clicks the back button.
     * It is responsible for starting the login activity.
     */
    @Override
    public void saveAppState(ApplicationState applicationState) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("applicationState", applicationState.toString());
        editor.apply();
    }
}