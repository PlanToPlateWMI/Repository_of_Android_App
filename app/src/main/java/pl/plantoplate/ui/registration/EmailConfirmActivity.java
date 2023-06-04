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

import java.util.Objects;

import okhttp3.ResponseBody;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.auth.AuthRepository;
import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.tools.ApplicationStateController;
import retrofit2.Call;

import pl.plantoplate.databinding.EmailConfirmationBinding;

/**
 * This activity is responsible for handling the email confirmation process.
 */
public class EmailConfirmActivity extends AppCompatActivity implements ApplicationStateController {

    private EmailConfirmationBinding email_confirm_view;

    private SharedPreferences prefs;

    private TextView email_info;
    private EditText enter_code;
    private Button confirm_button;
    private TextView resend_code_button;

    private String correct_code;

    /**
     * This method is called when the activity is created.
     * It is responsible for inflating the layout and defining the ui elements.
     * It is also responsible for setting the email info text.
     * It is also responsible for setting the listeners for the buttons.
     * It is also responsible for getting the shared preferences.
     * @param savedInstanceState
     */
    @SuppressLint({"SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using the View Binding Library
        email_confirm_view = EmailConfirmationBinding.inflate(getLayoutInflater(), null, false);
        setContentView(email_confirm_view.getRoot());

        // define ui elements
        email_info = email_confirm_view.skorzystajZLinku;
        enter_code = email_confirm_view.wprowadzKod.getEditText();
        confirm_button = email_confirm_view.buttonZatwierdzenieLink;
        resend_code_button = email_confirm_view.wyLijPono;

        // buttons listeners
        confirm_button.setOnClickListener(this::checkCode);
        resend_code_button.setOnClickListener(this::getNewConfirmCode);

        // get shared preferences
        prefs = getSharedPreferences("prefs", 0);

        // Set the email info text
        String email = prefs.getString("email", "");
        email_info.setText(email_info.getText() + "\n" + email);

    }

    /**
     * This method is called when the user clicks the confirm button.
     * It is responsible for checking if the entered code is correct.
     * If the code is correct, it starts the group select activity.
     * If the code is incorrect, it shows a snackbar with an error message.
     * @param view
     */
    public void checkCode(View view) {
        String entered_code = Objects.requireNonNull(email_confirm_view.wprowadzKod.getEditText()).getText().toString();
        String correct_code = prefs.getString("code", "");
        if (correct_code.equals(entered_code)){
            // delete the code from the shared preferences
            prefs.edit().remove("code").apply();

            // start the group select activity
            Intent intent = new Intent(this, GroupSelectActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            saveAppState(ApplicationState.GROUP_CHOOSE);
        }
        else {
            Snackbar.make(view, "Wprowadzony kod jest niepoprawny", Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * This method is called when the user clicks the resend code button.
     * It is responsible for getting a new code from the server.
     * It is also responsible for saving the new code in the shared preferences.
     * It is also responsible for clearing the entered code.
     * It is also responsible for showing a snackbar with a message that the code has been sent.
     * @param view
     */
    public void getNewConfirmCode(View view) {
        // Create a new Email object with the email from the shared preferences.
        String email = prefs.getString("email", "");

        // clear entered code
        enter_code.setText("");

        AuthRepository authRepository = new AuthRepository();
        authRepository.getEmailConfirmCode(email, "registration", new ResponseCallback<String>() {
            @Override
            public void onSuccess(String response) {
                // save the code in the shared preferences
                prefs.edit().putString("code", response).apply();
            }

            @Override
            public void onError(String errorMessage) {
                Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String failureMessage) {
                Snackbar.make(view, failureMessage, Snackbar.LENGTH_LONG).show();
            }
        });

        // make snackbar that informs the user that the code has been sent
        Snackbar.make(view, "Wysłano nowy kod", Snackbar.LENGTH_LONG).show();
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
