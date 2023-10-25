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

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.repository.AuthRepository;
import pl.plantoplate.databinding.EmailConfirmationBinding;
import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.tools.ApplicationStateController;
import timber.log.Timber;

/**
 * This activity is responsible for handling the email confirmation process.
 */
public class EmailConfirmActivity extends AppCompatActivity implements ApplicationStateController {

    private CompositeDisposable compositeDisposable;
    private SharedPreferences prefs;
    private TextView emailInfoTextView;
    private EditText enterCodeEditText;
    private Button confirmButton;
    private TextView resendCodeButton;

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
        setEmailInfoText();
        setClickListeners();
        compositeDisposable = new CompositeDisposable();
        prefs = getSharedPreferences("prefs", 0);
        Timber.d("Activity created");
    }

    private void initViews(EmailConfirmationBinding email_confirm_view) {
        Timber.d("Initializing views...");
        emailInfoTextView = email_confirm_view.skorzystajZLinku;
        enterCodeEditText = email_confirm_view.wprowadzKod.getEditText();
        confirmButton = email_confirm_view.buttonZatwierdzenieLink;
        resendCodeButton = email_confirm_view.wyLijPono;
    }

    private void setEmailInfoText() {
        Timber.d("Setting email info text...");
        String email = prefs.getString("email", "");
        String text  = emailInfoTextView.getText() + "\n" + email;
        emailInfoTextView.setText(text);
    }

    private void setClickListeners() {
        Timber.d("Setting click listeners...");
        confirmButton.setOnClickListener(this::checkCode);
        resendCodeButton.setOnClickListener(this::getNewConfirmCode);
    }

    /**
     * This method is called when the user clicks the get new code button.
     * It is responsible for getting a new code from the server.
     * @param view The view that was clicked.
     */
    public void checkCode(View view) {
        Timber.d("Checking email confirm code...");
        String entered_code = enterCodeEditText.getText().toString();
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
        Timber.d("Getting new confirm code...");
        String email = prefs.getString("email", "");
        enterCodeEditText.setText("");
        AuthRepository authRepository = new AuthRepository();
        Disposable disposable = authRepository.getEmailConfirmCode(email, "registration")
                .subscribe(
                        response -> prefs.edit().putString("code", response.getCode()).apply(),
                        error -> showSnackbar(view, error.getMessage())
                );
        compositeDisposable.add(disposable);
        showSnackbar(view, "Wysłano nowy kod");
    }

    public void startGroupSelectActivity() {
        Timber.d("Starting group select activity...");
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
        Timber.d("Saving application state: %s", applicationState.toString());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("applicationState", applicationState.toString());
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Timber.d("Destroying activity...");
        compositeDisposable.dispose();
    }
}