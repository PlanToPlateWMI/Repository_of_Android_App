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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import okhttp3.ResponseBody;
import pl.plantoplate.databinding.RemindPassword1Binding;
import pl.plantoplate.requests.RetrofitClient;
import pl.plantoplate.requests.remindPassword.GetCodeCallback;
import retrofit2.Call;

/**
 * A class that is responsible for the first step of the password remind process.
 * It is responsible for getting the email from the user and sending it to the server.
 */
public class EnterEmailActivity extends AppCompatActivity {

    private RemindPassword1Binding change_password_view;

    private TextInputEditText email_field;
    private Button apply_button;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using view binding
        change_password_view = RemindPassword1Binding.inflate(getLayoutInflater());
        setContentView(change_password_view.getRoot());

        // Define the ui elements
        email_field = change_password_view.enterTheName;
        apply_button = change_password_view.buttonZatwierdz;

        // Set a click listeners for the buttons
        apply_button.setOnClickListener(this::validateEmail);

        // Get the shared preferences
        prefs = getSharedPreferences("prefs", MODE_PRIVATE);

    }

    /**
     * A method that validates the email and sends it to the server, to get a email confirmation code.
     * @param v The view that was clicked.
     */
    public void validateEmail(View v) {
        // Get the email from the text field.
        String email = email_field.getText() != null ? email_field.getText().toString() : "";

        // remove whitespaces
        email = email.trim();

        // Save the email in the shared preferences.
        prefs.edit().putString("email", email).apply();

        // Create a new retrofit call to send the user data to the server.
        Call<ResponseBody> myCall = RetrofitClient.getInstance().getApi().getConfirmCode(email);

        // Enqueue the call with a custom callback that handles the response.
        myCall.enqueue(new GetCodeCallback(v, email));
    }
}
