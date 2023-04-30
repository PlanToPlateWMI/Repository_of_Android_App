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
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import pl.plantoplate.databinding.LoginPageBinding;
import pl.plantoplate.ui.registration.RegisterActivity;


/**
 * An activity that allows users to log in to their account.
 */
public class LoginActivity extends AppCompatActivity {

    // Declare variables for views in the layout using view binding
    private LoginPageBinding login_view;
    private Button create_account_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using view binding
        login_view = LoginPageBinding.inflate(getLayoutInflater());
        setContentView(login_view.getRoot());

        // Get a reference to the create account button using view binding
        create_account_button = login_view.buttonZalozKonto;

        // Set a click listener for the create account button using a lambda expression
        create_account_button.setOnClickListener(v -> createAccount());

    }

    // Method to handle sign in button click
    public void signIn(){
        // TODO: Implement sign in logic
    }

    // Method to handle create account button click
    public void createAccount() {
        // Create an intent to start the RegisterActivity
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}


