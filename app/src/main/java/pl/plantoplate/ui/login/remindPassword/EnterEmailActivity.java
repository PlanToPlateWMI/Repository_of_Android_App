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
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.databinding.RemindPassword1Binding;
import pl.plantoplate.data.remote.ResponseCallback;
import pl.plantoplate.data.remote.repository.AuthRepository;
import pl.plantoplate.data.remote.models.Message;

/**
 * A class that is responsible for the first step of the password remind process.
 * It is responsible for getting the email from the user and sending it to the server.
 */
public class EnterEmailActivity extends AppCompatActivity {

    private RemindPassword1Binding change_password_view;

    private TextInputEditText email_field;
    private Button apply_button;

    private SharedPreferences prefs;
    private AuthRepository authRepository;

    /**
     * A method that is called when the activity is created.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String email = getIntent().getStringExtra("email");

        // Inflate the layout using view binding
        change_password_view = RemindPassword1Binding.inflate(getLayoutInflater());
        setContentView(change_password_view.getRoot());

        // Define the ui elements
        email_field = change_password_view.enterTheName;
        apply_button = change_password_view.buttonZatwierdz;

        email_field.setText(email);

        // Set a click listeners for the buttons
        apply_button.setOnClickListener(this::checkUserExists);

        // Get the shared preferences
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        // Create the auth repository
        authRepository = new AuthRepository();

    }

    /**
     * A method that checks if the user exists in the database.
     * @param view The view that was clicked.
     */
    public void checkUserExists(View view){
        String email = String.valueOf(email_field.getText());
        Disposable disposable = authRepository.userExists(email)
                .subscribe(
                        response -> {
                            // user exists
                            Snackbar.make(view, "Użytkownik o podanym adresie email nie istnieje!", Snackbar.LENGTH_LONG).show();
                        },
                        error -> {
                            // user don't exists
                            validateEmail(view);
                        }
                );
    }

    /**
     * A method that validates the email and sends it to the server, to get a email confirmation code.
     * @param v The view that was clicked.
     */
    public void validateEmail(View v) {
        // Get the email from the text field.
        String email = email_field.getText() != null ? email_field.getText().toString() : "";

        // remove whitespaces
        email = email.trim();

        // Save the email in the shared preferences.
        prefs.edit().putString("email", email).apply();

        // start the next activity
        Intent intent = new Intent(getApplicationContext(), EnterCodeActivity.class);
        startActivity(intent);

        AuthRepository authRepository = new AuthRepository();
        Disposable disposable = authRepository.getEmailConfirmCode(email, "reset")
                .subscribe(
                        response -> {
                            // save the code in the shared preferences
                            prefs.edit().putString("code", response.getCode()).apply();
                        },
                        error -> {
                            // show the error message
                            Snackbar.make(v, Objects.requireNonNull(error.getMessage()), Snackbar.LENGTH_LONG).show();
                        }
                );
    }
}
