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

package pl.plantoplate.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import pl.plantoplate.databinding.LoginPageBinding;
import pl.plantoplate.repository.remote.models.JwtResponse;
import pl.plantoplate.repository.remote.models.SignInData;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.auth.AuthRepository;
import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.tools.ApplicationStateController;
import pl.plantoplate.tools.SCryptStretcher;
import pl.plantoplate.ui.login.remindPassword.EnterEmailActivity;
import pl.plantoplate.ui.main.ActivityMain;
import pl.plantoplate.ui.registration.RegisterActivity;

/**
 * An activity that allows users to log in to their account.
 */
public class LoginActivity extends AppCompatActivity implements ApplicationStateController {

    private LoginPageBinding login_view;

    private TextInputEditText email_field;
    private TextInputEditText password_field;
    private Button sign_in_button;
    private TextView remind_password_button;
    private TextView nie_masz_konta;

    private SharedPreferences prefs;

    /**
     * A method that allows the user to log in to their account.
     * @param view The view that was clicked.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using view binding
        login_view = LoginPageBinding.inflate(getLayoutInflater());
        setContentView(login_view.getRoot());

        // Define the ui elements
        email_field = login_view.enterMail;
        password_field = login_view.enterPass;
        sign_in_button = login_view.buttonZalogujSie;
        remind_password_button = login_view.przypHaslo;
        nie_masz_konta = login_view.nieMaszKonta;

        Spannable spans = new SpannableString("Nie masz konta?    ZAŁÓŻ KONTO");
        spans.setSpan(new ForegroundColorSpan(Color.parseColor("#6692EA")), 15, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        nie_masz_konta.setText(spans);

        // Get the shared preferences
        prefs = getSharedPreferences("prefs", 0);

        // Set a click listeners for the buttons
        sign_in_button.setOnClickListener(this::signIn);
        remind_password_button.setOnClickListener(v -> remindPassword());
        nie_masz_konta.setOnClickListener(v -> createAccount());

    }

    /**
     * Get the user's information from the input fields.
     *
     * @return A UserInfo object containing the user's information.
     */
    public SignInData getUserInfo(){
        // get the email and password from the text fields
        String email = Objects.requireNonNull(email_field.getText()).toString();
        String password = Objects.requireNonNull(password_field.getText()).toString();
        // remove all whitespaces from email
        email = email.trim();
        checkMail(email);
        checkPassword(password, email);
        //stretch password to make it unreadable and secure
        password = SCryptStretcher.stretch(password, email);


        return new SignInData(email, password);
    }

    public void checkMail(String email){
        if(email.isEmpty()) {
            Snackbar.make(email_field, "Adres mail nie może być pusty!", Snackbar.LENGTH_LONG).show();
            getUserInfo();
        }
    }

    public void checkPassword(String password, String email){
        if(password.isEmpty()) {
            Snackbar.make(email_field, "Hasło nie może być pustę!", Snackbar.LENGTH_LONG).show();
            getUserInfo();
        }
    }

    /**
     * Signs the user in to their account.
     * @param view The view that was clicked.
     */
    public void signIn(View view){

        SignInData userSignInData = getUserInfo();

        AuthRepository authRepository = new AuthRepository();
        authRepository.signIn(userSignInData, new ResponseCallback<JwtResponse>() {
            @Override
            public void onSuccess(JwtResponse jwtResponse) {
                // save the token and role in the shared preferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("token", jwtResponse.getToken());
                editor.putString("role", jwtResponse.getRole());
                editor.apply();

                // Start the main activity
                Intent intent = new Intent(view.getContext(), ActivityMain.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                view.getContext().startActivity(intent);

                // save the app state
                saveAppState(ApplicationState.MAIN_ACTIVITY);
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


    /**
     * Starts the RegisterActivity to allow the user to create an account.
     */
    public void createAccount() {
        // Create an intent to start the RegisterActivity
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        saveAppState(ApplicationState.REGISTER);
    }

    /**
     * Starts the EnterEmailActivity to allow the user to reset their password.
     */
    public void remindPassword() {
        // Create an intent to start the RemindPasswordActivity
        Intent intent = new Intent(this, EnterEmailActivity.class);
        startActivity(intent);
    }

    /**
     * Saves the current app state in the shared preferences.
     * @param applicationState The current app state.
     */
    @Override
    public void saveAppState(ApplicationState applicationState){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("applicationState", applicationState.toString());
        editor.apply();
    }
}


