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

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.ResponseCallback;
import pl.plantoplate.data.remote.models.Message;
import pl.plantoplate.data.remote.models.UserRegisterData;
import pl.plantoplate.data.remote.repository.AuthRepository;
import pl.plantoplate.databinding.RegisterActivityBinding;
import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.tools.ApplicationStateController;
import pl.plantoplate.tools.EmailValidator;
import pl.plantoplate.tools.SCryptStretcher;
import pl.plantoplate.ui.login.LoginActivity;

/**
 * An activity for user registration.
 */
public class RegisterActivity extends AppCompatActivity implements ApplicationStateController {

    private EditText enter_name, enter_email, enter_password;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RegisterActivityBinding registerViewBinding = RegisterActivityBinding.inflate(getLayoutInflater());
        setContentView(registerViewBinding.getRoot());

        initViews(registerViewBinding);
        setClickListeners();
        prefs = getSharedPreferences("prefs", 0);
        authRepository = new AuthRepository();
    }

    private void initViews(RegisterActivityBinding binding) {
        enter_name = binding.enterName;
        enter_email = binding.enterEmail;
        enter_password = binding.enterPassword;
        apply_policy = binding.checkboxWyrazamZgode;
        register_button = binding.buttonZalozKonto;
        masz_konto = binding.maszKonto;

        initHasAccountTextView();
    }

    private void setClickListeners() {
        register_button.setOnClickListener(this::checkUserExists);
        masz_konto.setOnClickListener(v -> signInAccount());
    }

    private void initHasAccountTextView() {
        Spannable spans = new SpannableString("Masz konto?    ZAŁOGUJ SIĘ");
        spans.setSpan(new ForegroundColorSpan(Color.parseColor("#6692EA")), 11, 26, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        masz_konto.setText(spans);
    }

    /**
     * Get the user's information from the input fields.
     *
     * @return A UserInfo object containing the user's information.
     */
    public UserRegisterData getUserInfo() {
        String name = enter_name.getText().toString().trim();
        String email = enter_email.getText().toString().trim();
        String password = enter_password.getText().toString();
        return new UserRegisterData(name, email, password);
    }

    /**
     * Validate the user's information and show a Snackbar message if any errors are found.
     *
     * @param view The view that was clicked.
     */
    public void validateUserInfo(View view) {
        UserRegisterData info = getUserInfo();

        if (textIsEmpty(info.getUsername())) {
            showSnackbar(view, "Wprowadź imię użytkownika!");
        } else if (textIsEmpty(info.getEmail())) {
            showSnackbar(view, "Wprowadź adres email!");
        } else if (textIsEmpty(info.getPassword())) {
            showSnackbar(view, "Wprowadź hasło!");
        } else if (!EmailValidator.isEmail(info.getEmail())) {
            showSnackbar(view, "Email jest niepoprawny!");
        } else if (info.getPassword().length() < 7) {
            showSnackbar(view, "Hasło musi być długie (co najmniej 7 znaków)");
        } else if (!apply_policy.isChecked()) {
            showSnackbar(view, "Musisz wyrazić zgodę na przetwarzanie danych osobowych");
        } else {
            info.setPassword(SCryptStretcher.stretch(info.getPassword(), info.getEmail()));
            sendUserData(info, view);
        }
    }

    /**
     * Send the user's information to the server and handle the response asynchronously.
     *
     * @param view The view to display the response in (e.g. error using SnackBar).
     */
    public void checkUserExists(View view){
        String email = String.valueOf(enter_email.getText());

        Disposable disposable = authRepository.userExists(email)
                .subscribe(message -> {
                            // user don't exists
                            prefs.edit().putString("email", email).apply();
                            validateUserInfo(view);
                        },
                        throwable ->
                                Snackbar.make(view, Objects.requireNonNull(throwable.getMessage()), Snackbar.LENGTH_LONG).show()
                );
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

        Disposable disposable = authRepository.sendUserRegisterData(userData)
                .subscribe(codeResponse -> {
                            // save user data to shared preferences
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("email", userData.getEmail());
                            editor.putString("name", userData.getUsername());
                            editor.putString("password", userData.getPassword());
                            editor.putString("code", codeResponse.getCode()).apply();
                        },
                        throwable ->
                                Snackbar.make(view, Objects.requireNonNull(throwable.getMessage()), Snackbar.LENGTH_LONG).show());
    }

    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    private boolean textIsEmpty(String text) {
        return text == null || text.isEmpty();
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