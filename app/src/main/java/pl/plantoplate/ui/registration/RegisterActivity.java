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
import java.util.Optional;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.models.CodeResponse;
import pl.plantoplate.data.remote.models.UserRegisterData;
import pl.plantoplate.data.remote.repository.AuthRepository;
import pl.plantoplate.databinding.RegisterActivityBinding;
import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.tools.ApplicationStateController;
import pl.plantoplate.tools.EmailValidator;
import pl.plantoplate.tools.SCryptStretcher;
import pl.plantoplate.ui.login.LoginActivity;
import timber.log.Timber;

/**
 * An activity for user registration.
 */
public class RegisterActivity extends AppCompatActivity implements ApplicationStateController {

    private CompositeDisposable compositeDisposable;
    private EditText enterNameEditText, enterEmailEditText, enterPasswordEditText;
    private CheckBox applyPolicyCheckBox;
    private Button registerButton;
    private TextView hasAccountTextView;
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
        compositeDisposable = new CompositeDisposable();
        Timber.d("Activity created");
    }

    private void initViews(RegisterActivityBinding binding) {
        Timber.d("Initializing views...");
        enterNameEditText = binding.enterName;
        enterEmailEditText = binding.enterEmail;
        enterPasswordEditText = binding.enterPassword;
        applyPolicyCheckBox = binding.checkboxWyrazamZgode;
        registerButton = binding.buttonZalozKonto;
        hasAccountTextView = binding.maszKonto;

        initHasAccountTextView();
    }

    private void initHasAccountTextView() {
        Timber.d("Initializing has account text view...");
        Spannable spans = new SpannableString("Masz konto?    ZAŁOGUJ SIĘ");
        spans.setSpan(new ForegroundColorSpan(Color.parseColor("#6692EA")), 11, 26, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        hasAccountTextView.setText(spans);
    }

    private void setClickListeners() {
        Timber.d("Setting click listeners...");
        registerButton.setOnClickListener(this::checkUserExists);
        hasAccountTextView.setOnClickListener(v -> signInAccount());
    }

    /**
     * Get the user's information from the input fields.
     *
     * @return A UserInfo object containing the user's information.
     */
    public UserRegisterData getUserInfo() {
        Timber.d("Getting user info...");
        String name = Optional.of(enterNameEditText.getText().toString()).orElse("").trim();
        String email = Optional.of(enterEmailEditText.getText().toString()).orElse("").trim();
        String password = Optional.of(enterPasswordEditText.getText().toString()).orElse("").trim();
        return new UserRegisterData(name, email, password);
    }

    /**
     * Validate the user's information and show a Snackbar message if any errors are found.
     *
     * @param view The view that was clicked.
     */
    public void validateUserInfo(View view) {
        Timber.d("Validating user info...");
        UserRegisterData info = getUserInfo();
        if (info.getUsername().isEmpty()) {
            Timber.d("Username is empty");
            showSnackbar(view, "Wprowadź imię użytkownika!");
        } else if (info.getEmail().isEmpty()) {
            Timber.d("Email is empty");
            showSnackbar(view, "Wprowadź adres email!");
        } else if (info.getPassword().isEmpty()) {
            Timber.d("Password is empty");
            showSnackbar(view, "Wprowadź hasło!");
        } else if (!EmailValidator.isEmail(info.getEmail())) {
            Timber.d("Email is invalid");
            showSnackbar(view, "Email jest niepoprawny!");
        } else if (info.getPassword().length() < 7) {
            Timber.d("Password is too short");
            showSnackbar(view, "Hasło musi być długie (co najmniej 7 znaków)");
        } else if (!applyPolicyCheckBox.isChecked()) {
            Timber.d("Policy not accepted");
            showSnackbar(view, "Musisz wyrazić zgodę na przetwarzanie danych osobowych");
        } else {
            Timber.d("User info is valid");
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
        Timber.d("Checking if user exists...");
        String email = String.valueOf(enterEmailEditText.getText()).trim();

        Disposable disposable = authRepository.userExists(email)
                .subscribe(message -> {
                            // user does not exist
                            prefs.edit().putString("email", email).apply();
                            validateUserInfo(view);
                        },
                        throwable ->
                                Snackbar.make(view, Objects.requireNonNull(throwable.getMessage()), Snackbar.LENGTH_LONG).show()
                );

        compositeDisposable.add(disposable);
    }

    /**
     * Sends the user data to the server and handles the response asynchronously.
     * @param userData The user data to send.
     * @param view The View to display the response in (e.g. error using SnackBar).
     */
    public void sendUserData(UserRegisterData userData, View view) {
        Timber.d("Sending user data to server");
        Intent intent = new Intent(view.getContext(), EmailConfirmActivity.class);
        startActivity(intent);

        Disposable disposable = authRepository.sendUserRegisterData(userData)
                .subscribe(codeResponse ->
                                saveUserData(userData, codeResponse),
                           throwable ->
                                Snackbar.make(view, Objects.requireNonNull(throwable.getMessage()), Snackbar.LENGTH_LONG).show());

        compositeDisposable.add(disposable);
    }

    private void saveUserData(UserRegisterData userData, CodeResponse codeResponse) {
        Timber.d("Saving user data to shared preferences");
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", userData.getEmail());
        editor.putString("name", userData.getUsername());
        editor.putString("password", userData.getPassword());
        editor.putString("code", codeResponse.getCode().trim()).apply();
    }

    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * Starts the LoginActivity to allow the user to sign in.
     */
    public void signInAccount() {
        Timber.d("Starting LoginActivity...");
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