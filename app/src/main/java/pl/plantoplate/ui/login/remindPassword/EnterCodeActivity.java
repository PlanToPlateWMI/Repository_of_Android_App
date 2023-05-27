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

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.ResponseBody;
import pl.plantoplate.databinding.RemindPassword2Binding;
import pl.plantoplate.requests.RetrofitClient;
import pl.plantoplate.requests.remindPassword.ResendCodeCallback;
import retrofit2.Call;

/**
 * This activity is responsible for handling the user input of the code sent to the user's email
 * for password reset purposes. It also handles the resend code button.
 */
public class EnterCodeActivity extends AppCompatActivity {

    private RemindPassword2Binding change_password_view;

    private TextView title;
    private TextInputEditText enter_code_field;
    private Button confirm_button;
    private TextView resend_code_button;

    private SharedPreferences prefs;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using view binding
        change_password_view = RemindPassword2Binding.inflate(getLayoutInflater());
        setContentView(change_password_view.getRoot());

        // Define the views
        title = change_password_view.skorzystajZKodu;
        enter_code_field = change_password_view.wprowadzKod;
        confirm_button = change_password_view.buttonZatwierdzenieLink;
        resend_code_button = change_password_view.wyLijPono;

        // Set confirm button listener
        confirm_button.setOnClickListener(this::checkCode);

        // Set resend code button listener
        resend_code_button.setOnClickListener(this::resendCode);

        // Get the shared preferences
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        // Set the title
        String email = prefs.getString("email", "");
        title.setText(title.getText().toString() + "\n" + email);

    }

    /**
     * This method is called when the user clicks the confirm button. It checks if the code entered
     * by the user is correct and if so, it starts the ChangePasswordActivity.
     * @param view The view that was clicked.
     */
    public void checkCode(View view) {
        String entered_code = enter_code_field.getText() != null ? enter_code_field.getText().toString(): "";
        String correct_code = getIntent().getStringExtra("code");
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
        // Create a new Email object with the email from the shared preferences.
        String email = prefs.getString("email", "");

        // Create a new retrofit call to send the user data to the server.
        Call<ResponseBody> myCall = RetrofitClient.getInstance().getApi().getConfirmCode(email);

        // Enqueue the call with a custom callback that handles the response.
        myCall.enqueue(new ResendCodeCallback(view, email));
    }
}
