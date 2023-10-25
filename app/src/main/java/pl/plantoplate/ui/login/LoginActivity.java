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
import java.util.Optional;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.plantoplate.databinding.LoginPageBinding;
import pl.plantoplate.data.remote.models.SignInData;
import pl.plantoplate.data.remote.repository.AuthRepository;
import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.tools.ApplicationStateController;
import pl.plantoplate.tools.SCryptStretcher;
import pl.plantoplate.ui.login.remindPassword.EnterEmailActivity;
import pl.plantoplate.ui.main.ActivityMain;
import pl.plantoplate.ui.registration.RegisterActivity;
import timber.log.Timber;

/**
 * An activity that allows users to log in to their account.
 */
public class LoginActivity extends AppCompatActivity implements ApplicationStateController {

    private CompositeDisposable compositeDisposable;
    private TextInputEditText emailTextInput;
    private TextInputEditText passwordTextInput;
    private Button signInButton;
    private TextView remindPasswordButton;
    private TextView dontHaveAccountTextView;
    private SharedPreferences prefs;

    /**
     * A method that allows the user to log in to their account.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoginPageBinding loginPageBinding = LoginPageBinding.inflate(getLayoutInflater());
        setContentView(loginPageBinding.getRoot());
        compositeDisposable = new CompositeDisposable();
        prefs = getSharedPreferences("prefs", 0);

        initViews(loginPageBinding);
        setClickListeners();
    }

    private void initViews(LoginPageBinding loginPageBinding) {
        Timber.d("Initializing views...");
        emailTextInput = loginPageBinding.enterMail;
        passwordTextInput = loginPageBinding.enterPass;
        signInButton = loginPageBinding.buttonZalogujSie;
        remindPasswordButton = loginPageBinding.przypHaslo;
        dontHaveAccountTextView = loginPageBinding.nieMaszKonta;
        initDontHaveAccountTextView();
    }

    private void initDontHaveAccountTextView() {
        Timber.d("Initializing has account text view...");
        Spannable spans = new SpannableString("Nie masz konta?    ZAŁÓŻ KONTO");
        spans.setSpan(new ForegroundColorSpan(Color.parseColor("#6692EA")), 15, 30, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        dontHaveAccountTextView.setText(spans);
    }

    private void setClickListeners() {
        Timber.d("Setting click listeners...");
        signInButton.setOnClickListener(this::validateUserInfo);
        remindPasswordButton.setOnClickListener(v -> remindPassword());
        dontHaveAccountTextView.setOnClickListener(v -> createAccount());
    }

    /**
     * Get the user's information from the input fields.
     *
     * @return A UserInfo object containing the user's information.
     */
    public SignInData getUserInfo() {
        String email = Optional.ofNullable(emailTextInput.getText()).map(Objects::toString).orElse("");
        String password = Optional.ofNullable(passwordTextInput.getText()).map(Objects::toString).orElse("");
        return new SignInData(email, password);
    }

    public void validateUserInfo(View view) {
        SignInData signInData = getUserInfo();
        if (signInData.getEmail().isEmpty()) {
            showSnackbar(view, "Wprowadź adres email");
        }
        else if(signInData.getPassword().isEmpty()) {
            showSnackbar(view, "Wprowadź hasło");
        }
        else{
            signInData.setPassword(SCryptStretcher.stretch(signInData.getPassword(), signInData.getEmail()));
            signIn(signInData, view);
        }
    }

    /**
     * Signs the user in to their account.
     * @param view The view that was clicked.
     */
    public void signIn(SignInData userSignInData, View view){
        AuthRepository authRepository = new AuthRepository();
        Disposable disposable = authRepository.signIn(userSignInData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(jwtResponse -> {
                    saveUserData(userSignInData, jwtResponse.getRole(), jwtResponse.getToken());
                    startMainActivity(view);
                    saveAppState(ApplicationState.MAIN_ACTIVITY);
                }, throwable -> showSnackbar(view, Objects.requireNonNull(throwable.getMessage())));

        compositeDisposable.add(disposable);
    }

    public void saveUserData(SignInData signInData, String role, String token) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", signInData.getEmail());
        editor.putString("token", token);
        editor.putString("role", role);
        editor.putString("password", signInData.getPassword());
        editor.apply();
    }

    public void startMainActivity(View view) {
        Intent intent = new Intent(view.getContext(), ActivityMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        view.getContext().startActivity(intent);
    }

    public void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Starts the RegisterActivity to allow the user to create an account.
     */
    public void createAccount() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        saveAppState(ApplicationState.REGISTER);
    }

    /**
     * Starts the EnterEmailActivity to allow the user to reset their password.
     */
    public void remindPassword() {
        String email = Objects.requireNonNull(emailTextInput.getText()).toString();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}