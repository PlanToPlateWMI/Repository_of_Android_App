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

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import pl.plantoplate.databinding.RegisterActivityBinding;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.auth.AuthRepository;
import pl.plantoplate.repository.remote.models.Message;
import pl.plantoplate.ui.login.LoginActivity;
import pl.plantoplate.repository.remote.models.UserRegisterData;
import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.tools.ApplicationStateController;
import pl.plantoplate.tools.EmailValidator;
import pl.plantoplate.tools.SCryptStretcher;
import com.google.android.material.snackbar.Snackbar;

/**
 * An activity for user registration.
 */
public class RegisterActivity extends AppCompatActivity implements ApplicationStateController {

    private RegisterActivityBinding register_view;
    private EditText enter_name;
    private EditText enter_email;
    private EditText enter_password;
    private CheckBox apply_policy;
    private Button register_button;
    private TextView masz_konto;

    private SharedPreferences prefs;
    private AuthRepository authRepository;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                               shut down then this Bundle contains the data it most recently
     *                               supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @SuppressLint({"ResourceType", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using the View Binding Library
        register_view = RegisterActivityBinding.inflate(getLayoutInflater());
        setContentView(register_view.getRoot());

        // Find the views in the layout
        enter_name = register_view.enterName;
        enter_email = register_view.enterEmail;
        enter_password = register_view.enterPassword;
        apply_policy = register_view.checkboxWyrazamZgode;
        register_button = register_view.buttonZalozKonto;
        masz_konto = register_view.maszKonto;

        Spannable spans = new SpannableString("Masz konto?    ZAŁOGUJ SIĘ");
        spans.setSpan(new ForegroundColorSpan(Color.parseColor("#6692EA")), 11, 26, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        masz_konto.setText(spans);

        // get auth repository
        authRepository = new AuthRepository();

        // Set the click listeners for the buttons
        register_button.setOnClickListener(this::checkUserExists);
        masz_konto.setOnClickListener(v -> signInAccount());

        // get shared preferences
        prefs = getSharedPreferences("prefs", 0);
    }

    /**
     * Get the user's information from the input fields.
     *
     * @return A UserInfo object containing the user's information.
     */
    public UserRegisterData getUserInfo(){
        String name = String.valueOf(enter_name.getText());
        String email = String.valueOf(enter_email.getText());

        //remove all whitespaces from email
        email = email.trim();

        String password = String.valueOf(enter_password.getText());
        return new UserRegisterData(name, email, password);
    }

    /**
     * Validate the user's information and show a Snackbar message if any errors are found.
     *
     * @param view The view that was clicked.
     */
    public void validateUserInfo(View view){
        UserRegisterData info = getUserInfo();

        if(info.getUsername() == null || info.getUsername().isEmpty()){
            Snackbar.make(view, "Wprowadż imię użytkownika!", Snackbar.LENGTH_LONG).show();
            return;
        }

        if(info.getEmail() == null || info.getEmail().isEmpty()){
            Snackbar.make(view, "Wprowadż adres email!", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (info.getPassword() == null || info.getPassword().isEmpty()){
            Snackbar.make(view, "Wprowadż hasło!", Snackbar.LENGTH_LONG).show();
            return;
        }

        if(!EmailValidator.isEmail(info.getEmail())){
            Snackbar.make(view, "Email jest niepoprawny!", Snackbar.LENGTH_LONG).show();
            return;
        }

        if(info.getPassword() == null || info.getPassword().length() < 7){
            Snackbar.make(view, "Hasło musi być długie (co najmniej 7 znaków)", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (apply_policy.isChecked()){

            //stretch password to make it unreadable and secure
            info.setPassword(SCryptStretcher.stretch(info.getPassword(), info.getEmail()));
            sendUserData(info, view);
        }
        else{
            Snackbar.make(view, "Musisz wyrazić zgodę na przetwarzanie danych osobowych", Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Send the user's information to the server and handle the response asynchronously.
     *
     * @param view The view to display the response in (e.g. error using SnackBar).
     */
    public void checkUserExists(View view){
        String email = String.valueOf(enter_email.getText());

        authRepository.userExists(email, new ResponseCallback<Message>() {

            @Override
            public void onSuccess(Message response) {
                // user don't exists
                prefs.edit().putString("email", email).apply();
                validateUserInfo(view);
            }

            @Override
            public void onError(String errorMessage) {
                // user exists
                Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String failureMessage) {
                Snackbar.make(view, failureMessage, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Sends the user data to the server and handles the response asynchronously.
     * @param userData The user data to send.
     * @param view The View to display the response in (e.g. error using SnackBar).
     */
    public void sendUserData(UserRegisterData userData, View view) {
        // start email confirmation activity
        Intent intent = new Intent(view.getContext(), EmailConfirmActivity.class);
        startActivity(intent);

        authRepository.sendUserRegisterData(userData, new ResponseCallback<String>() {

            /**
             * Called when the operation is successful and receives a code.
             *
             * @param code The code received from the operation.
             */
            @Override
            public void onSuccess(String code) {
                // save user data to shared preferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("email", userData.getEmail());
                editor.putString("name", userData.getUsername());
                editor.putString("password", userData.getPassword());
                editor.putString("code", code).apply();
            }

            /**
             * Called when an error occurs.
             *
             * @param errorMessage The error message to display.
             */
            @Override
            public void onError(String errorMessage) {
                Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
            }

            /**
             * Called when a failure occurs.
             *
             * @param failureMessage The failure message to display.
             */
            @Override
            public void onFailure(String failureMessage) {
                Snackbar.make(view, failureMessage, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Starts the LoginActivity to allow the user to sign in.
     */
    public void signInAccount() {
        // Create an intent to start the RegisterActivity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        saveAppState(ApplicationState.LOGIN);
    }

    /**
     * Saves the application state to shared preferences.
     * @param applicationState The application state to save.
     */
    @Override
    public void saveAppState(ApplicationState applicationState) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("applicationState", applicationState.toString());
        editor.apply();
    }
}
