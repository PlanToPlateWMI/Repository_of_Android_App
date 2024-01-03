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
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import java.util.Objects;
import java.util.Timer;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.databinding.RemindPassword2Binding;
import pl.plantoplate.data.remote.repository.AuthRepository;
import timber.log.Timber;

/**
 * This activity is responsible for handling the user input of the code sent to the user's email
 * for password reset purposes. It also handles the resend code button.
 */
public class EnterCodeActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable;
    private TextView titleTextView;
    private TextInputEditText enterCodeTextInput;
    private Button confirmButton;
    private TextView resendCodeButton;
    private SharedPreferences prefs;

    /**
     * This method is called when the activity is created.
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RemindPassword2Binding remindPassword2Binding = RemindPassword2Binding.inflate(getLayoutInflater());
        setContentView(remindPassword2Binding.getRoot());
        compositeDisposable = new CompositeDisposable();
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        initViews(remindPassword2Binding);
        setListeners();
    }

    /**
     * This method is called when the activity is resumed.
     */
    private void initViews(RemindPassword2Binding remindPassword2Binding) {
        titleTextView = remindPassword2Binding.skorzystajZKodu;
        enterCodeTextInput = remindPassword2Binding.wprowadzKod;
        confirmButton = remindPassword2Binding.buttonZatwierdzenieLink;
        resendCodeButton = remindPassword2Binding.wyLijPono;

        initTitleTextView();
    }

    /**
     * This method is called when the activity is resumed.
     */
    private void initTitleTextView(){
        String email = prefs.getString("email", "");
        String text = titleTextView.getText().toString() + "\n" + email;
        titleTextView.setText(text);
    }

    /**
     * This method is called when the activity is resumed.
     */
    private void setListeners(){
        confirmButton.setOnClickListener(this::validateConfirmCode);
        resendCodeButton.setOnClickListener(this::resendCode);
    }

    /**
     * This method is called when the user clicks the confirm button. It checks if the code entered
     * by the user is correct and if so, it starts the ChangePasswordActivity.
     * @param view The view that was clicked.
     */
    public void validateConfirmCode(View view) {
        String entered_code = enterCodeTextInput.getText() != null ? enterCodeTextInput.getText().toString().trim(): "";
        String correct_code = prefs.getString("code", "");
        if (correct_code.equals(entered_code)){
            startActivity(new Intent(this, ChangePasswordActivity.class));
        }
        else {
            Snackbar.make(view, "Wprowadzony kod jest niepoprawny", Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * This method is called when the user clicks the resend code button. It sends a request to the
     * server to resend the code to the user's email.
     * @param view The view that was clicked.
     */
    public void resendCode(View view) {
        String email = prefs.getString("email", "");
        AuthRepository repository = new AuthRepository();

        Disposable disposable = repository.getEmailConfirmCode(email, "reset")
                .subscribe(
                        response -> {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("code", response.getCode());
                            editor.apply();
                        },
                        error -> Snackbar.make(view, Objects.requireNonNull(error.getMessage()), Snackbar.LENGTH_LONG).show()
                );

        compositeDisposable.add(disposable);
    }

    /**
     * This method is called when the activity is destroyed.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}
