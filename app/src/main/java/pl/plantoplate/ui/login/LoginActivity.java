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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.plantoplate.databinding.LoginPageBinding;
import pl.plantoplate.data.remote.models.JwtResponse;
import pl.plantoplate.data.remote.models.SignInData;
import pl.plantoplate.data.remote.ResponseCallback;
import pl.plantoplate.data.remote.repository.AuthRepository;
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
     * @param savedInstanceState The saved instance state.
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
        //stretch password to make it unreadable and secure
        if (!password.isEmpty()) {
            password = SCryptStretcher.stretch(password, email);
        }

        return new SignInData(email, password);
    }

    public boolean validMail(String email){
        return email != null && !email.isEmpty();
    }

    public boolean validPassword(String password){
        return password != null && !password.isEmpty();
    }

    /**
     * Signs the user in to their account.
     * @param view The view that was clicked.
     */
    public void signIn(View view){

        SignInData userSignInData = getUserInfo();

        // check if the email and password are not empty
        if(!validMail(userSignInData.getEmail())){
            Snackbar.make(view, "Wprowadź adres email", Snackbar.LENGTH_LONG).show();
            return;
        }
        // check if the email and password are not empty
        if(!validPassword(userSignInData.getPassword())){
            Snackbar.make(view, "Wprowadź hasło", Snackbar.LENGTH_LONG).show();
            return;
        }

        AuthRepository authRepository = new AuthRepository();
        Disposable disposable = authRepository.signIn(userSignInData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jwtResponse -> {
                    // save the token and role in the shared preferences
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("email", userSignInData.getEmail());
                    editor.putString("token", jwtResponse.getToken());
                    editor.putString("role", jwtResponse.getRole());
                    editor.putString("password", userSignInData.getPassword());
                    editor.apply();

                    // Start the main activity
                    Intent intent = new Intent(view.getContext(), ActivityMain.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    view.getContext().startActivity(intent);

                    // save the app state
                    saveAppState(ApplicationState.MAIN_ACTIVITY);
                }, throwable -> {
                    Snackbar.make(view, Objects.requireNonNull(throwable.getMessage()), Snackbar.LENGTH_LONG).show();
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
        //Get email from user
        String email = Objects.requireNonNull(email_field.getText()).toString();
        // Create an intent to start the RemindPasswordActivity
        Intent intent = new Intent(this, EnterEmailActivity.class);
        intent.putExtra("email", email);
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


