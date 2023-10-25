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
import java.util.Optional;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.databinding.RemindPassword1Binding;
import pl.plantoplate.data.remote.repository.AuthRepository;
import timber.log.Timber;

/**
 * A class that is responsible for the first step of the password remind process.
 * It is responsible for getting the email from the user and sending it to the server.
 */
public class EnterEmailActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable;
    private TextInputEditText emailTextInput;
    private Button applyButton;
    private SharedPreferences prefs;
    private AuthRepository authRepository;

    /**
     * A method that is called when the activity is created.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RemindPassword1Binding remindPassword1Binding = RemindPassword1Binding.inflate(getLayoutInflater());
        setContentView(remindPassword1Binding.getRoot());
        compositeDisposable = new CompositeDisposable();
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        authRepository = new AuthRepository();

        initViews(remindPassword1Binding);
        setClickListeners();
    }

    private void initViews(RemindPassword1Binding remindPassword1Binding) {
        Timber.d("Initializing views...");
        emailTextInput = remindPassword1Binding.enterTheName;
        applyButton = remindPassword1Binding.buttonZatwierdz;

        String email = getIntent().getStringExtra("email");
        emailTextInput.setText(email);
    }

    private void setClickListeners() {
        applyButton.setOnClickListener(this::checkUserExists);
    }

    /**
     * A method that checks if the user exists in the database.
     * @param view The view that was clicked.
     */
    public void checkUserExists(View view){
        String email = String.valueOf(emailTextInput.getText());
        Disposable disposable = authRepository.userExists(email)
                .subscribe(
                        response -> {
                            // user exists
                            Snackbar.make(view, "Użytkownik o podanym adresie email nie istnieje!", Snackbar.LENGTH_LONG).show();
                        },
                        error -> {
                            // user don't exists
                            getConfirmCode(view);
                        }
                );

        compositeDisposable.add(disposable);
    }

    /**
     * A method that is responsible for getting the confirm code from the server.
     * @param v The view that was clicked.
     */
    public void getConfirmCode(View v) {
        String email = Optional.ofNullable(emailTextInput.getText()).map(Objects::toString).orElse("").trim();
        prefs.edit().putString("email", email).apply();
        Intent intent = new Intent(getApplicationContext(), EnterCodeActivity.class);
        startActivity(intent);

        AuthRepository authRepository = new AuthRepository();
        Disposable disposable = authRepository.getEmailConfirmCode(email, "reset")
                .subscribe(
                        response -> prefs.edit().putString("code", response.getCode()).apply(),
                        error -> Snackbar.make(v, Objects.requireNonNull(error.getMessage()), Snackbar.LENGTH_LONG).show()
                );

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}
