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
package pl.plantoplate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import okhttp3.ResponseBody;
import pl.plantoplate.databinding.LoginPageBinding;
import pl.plantoplate.requests.RetrofitClient;
import pl.plantoplate.requests.signin.SignInCallback;
import pl.plantoplate.requests.signin.SignInData;
import pl.plantoplate.tools.SCryptStretcher;
import pl.plantoplate.ui.login.remindPassword.EnterEmailActivity;
import pl.plantoplate.ui.registration.RegisterActivity;
import retrofit2.Call;

/**
 * An activity that allows users to log in to their account.
 */
public class LoginActivity extends AppCompatActivity {

    private LoginPageBinding login_view;

    private TextInputEditText email_field;
    private TextInputEditText password_field;
    private Button sign_in_button;
    private Button create_account_button;
    private TextView remind_password_button;

    private SharedPreferences prefs;

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
        create_account_button = login_view.buttonZalozKonto;
        remind_password_button = login_view.przypHaslo;

        // Set a click listeners for the buttons
        sign_in_button.setOnClickListener(this::signIn);
        create_account_button.setOnClickListener(v -> createAccount());
        remind_password_button.setOnClickListener(v -> remindPassword());

        // Get the shared preferences
        prefs = getSharedPreferences("prefs", 0);

    }

    /**
     * Signs the user in to their account.
     * @param view The view that was clicked.
     */
    public void signIn(View view){
        String email = Objects.requireNonNull(email_field.getText()).toString();
        String password = Objects.requireNonNull(password_field.getText()).toString();


        SignInData data = new SignInData(email, password);

        //stretch password to make it unreadable and secure
        data.setPassword(SCryptStretcher.stretch(data.getPassword(), email));

        Call<ResponseBody> myCall = RetrofitClient.getInstance().getApi().signinUser(data);

        myCall.enqueue(new SignInCallback(view));
    }


    /**
     * Starts the RegisterActivity to allow the user to create an account.
     */
    public void createAccount() {
        // Create an intent to start the RegisterActivity
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Starts the EnterEmailActivity to allow the user to reset their password.
     */
    public void remindPassword() {
        // Create an intent to start the RemindPasswordActivity
        Intent intent = new Intent(this, EnterEmailActivity.class);
        startActivity(intent);
    }
}


