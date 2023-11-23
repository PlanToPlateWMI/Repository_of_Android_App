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
import java.util.Objects;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.databinding.RemindPassword3Binding;
import pl.plantoplate.data.remote.repository.AuthRepository;
import pl.plantoplate.data.remote.models.auth.SignInData;
import pl.plantoplate.utils.ApplicationState;
import pl.plantoplate.utils.ApplicationStateController;
import pl.plantoplate.utils.SCryptStretcher;
import pl.plantoplate.ui.login.LoginActivity;

/**
 * An activity that allows users to change their password.
 */
public class ChangePasswordActivity extends AppCompatActivity implements ApplicationStateController {

    private CompositeDisposable compositeDisposable;
    private EditText newPasswordEditText, repeatNewPasswordEditText;
    private Button applyButton;
    private SharedPreferences prefs;

    /**
     * A method that is called when the activity is created.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RemindPassword3Binding remindPassword3Binding = RemindPassword3Binding.inflate(getLayoutInflater());
        setContentView(remindPassword3Binding.getRoot());
        compositeDisposable = new CompositeDisposable();
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        initViews(remindPassword3Binding);
        setClickListeners();
    }

    public void initViews(RemindPassword3Binding remindPassword3Binding){
        newPasswordEditText = remindPassword3Binding.wprowadzNoweHaslo.getEditText();
        repeatNewPasswordEditText = remindPassword3Binding.wprowadzNoweHaslo2.getEditText();
        applyButton = remindPassword3Binding.buttonZatwierdzenie;
    }

    public void setClickListeners(){
        applyButton.setOnClickListener(this::validatePassword);
    }

    /**
     * A method that validates the password and sends it to the server.
     * @param view The view that was clicked.
     */
    private void validatePassword(View view) {
        String new_password1 = newPasswordEditText.getText().toString().trim();
        String new_password2 = repeatNewPasswordEditText.getText().toString().trim();

        if (new_password1.equals(new_password2)) {
            String email = prefs.getString("email", "");
            if (new_password1.length() < 7) {
                showSnackbar(view, "Hasło musi mieć co najmniej 7 znaków");
                return;
            }

            SignInData userSignInData = new SignInData(email, SCryptStretcher.stretch(new_password1, email));
            sendNewPassword(userSignInData, view);
        }
        else {
            repeatNewPasswordEditText.setError("Hasła nie są takie same");
        }
    }

    /**
     * A method that sends the new password to the server and get the token.
     * @param userSignInData The data that will be sent to the server to apply the new password.
     * @param view The view that was clicked.
     */
    private void sendNewPassword(SignInData userSignInData, View view) {
        AuthRepository authRepository = new AuthRepository();

        Disposable disposable = authRepository.resetPassword(userSignInData)
                .subscribe(message -> {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    showSnackbar(view, "Pomyślnie zmieniono hasło!");
                    new Handler().postDelayed(() -> view.getContext().startActivity(intent), 500);
                }, throwable -> showSnackbar(view, Objects.requireNonNull(throwable.getMessage())));

        compositeDisposable.add(disposable);
    }

    private void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * A method that saves the application state.
     * @param applicationState The application state that will be saved.
     */
    @Override
    public void saveAppState(ApplicationState applicationState) {
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