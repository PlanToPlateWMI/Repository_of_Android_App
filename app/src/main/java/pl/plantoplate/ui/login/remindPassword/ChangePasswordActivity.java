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

package pl.plantoplate.ui.login.remindPassword;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import pl.plantoplate.databinding.RemindPassword3Binding;
import pl.plantoplate.repository.models.Message;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.auth.AuthRepository;
import pl.plantoplate.repository.models.SignInData;
import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.tools.ApplicationStateController;
import pl.plantoplate.tools.SCryptStretcher;
import pl.plantoplate.ui.login.LoginActivity;

/**
 * An activity that allows users to change their password.
 */
public class ChangePasswordActivity extends AppCompatActivity implements ApplicationStateController {

    private RemindPassword3Binding change_password_view;

    private EditText new_password_field1;
    private EditText new_password_field2;
    private Button apply_button;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using view binding
        change_password_view = RemindPassword3Binding.inflate(getLayoutInflater());
        setContentView(change_password_view.getRoot());

        // Define the ui elements
        new_password_field1 = change_password_view.wprowadzNoweHaslo.getEditText();
        new_password_field2 = change_password_view.wprowadzNoweHaslo2.getEditText();
        apply_button = change_password_view.buttonZatwierdzenie;

        // Get the shared preferences
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        // Set a click listeners for the buttons
        apply_button.setOnClickListener(this::validatePassword);
    }

    /**
     * A method that validates the password and sends it to the server.
     * @param view The view that was clicked.
     */
    private void validatePassword(View view) {
        String new_password1 = new_password_field1.getText().toString();
        String new_password2 = new_password_field2.getText().toString();

        if (new_password1.equals(new_password2)) {
            String email = prefs.getString("email", "");
            if (new_password1.length() < 7) {
                Snackbar.make(view, "Hasło musi mieć co najmniej 7 znaków", Snackbar.LENGTH_LONG).show();
                return;
            }

            SignInData userSignInData = new SignInData(email, SCryptStretcher.stretch(new_password1, email));
            sendNewPassword(userSignInData, view);
        }
        else {
            new_password_field2.setError("Hasła nie są takie same");
        }
    }

    /**
     * A method that sends the new password to the server and get the token.
     * @param userSignInData The data that will be sent to the server to apply the new password.
     * @param view The view that was clicked.
     */
    private void sendNewPassword(SignInData userSignInData, View view) {
        AuthRepository authRepository = new AuthRepository();
        authRepository.resetPassword(userSignInData, new ResponseCallback<Message>() {
            @Override
            public void onSuccess(Message message) {
                // Start the login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                Snackbar.make(view, "Pomyślnie zmieniono hasło!", Snackbar.LENGTH_LONG).show();
                new Handler().postDelayed(() -> view.getContext().startActivity(intent), 500);
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
    }

    @Override
    public void saveAppState(ApplicationState applicationState) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("applicationState", applicationState.toString());
        editor.apply();
    }
}
