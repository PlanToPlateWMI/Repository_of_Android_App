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
package com.example.planetoplate_app;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.planetoplate_app.databinding.RegisterActivityBinding;
import com.example.planetoplate_app.tools.EmailValidator;
import com.google.android.material.snackbar.Snackbar;

/**
 * An activity for user registration.
 */
public class Register extends AppCompatActivity {

    private RegisterActivityBinding register_view;
    private EditText enter_name;
    private EditText enter_email;
    private EditText enter_password;
    private CheckBox apply_policy;
    private Button register_button;

    /**
     * Called when the activity is first created.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being
     *                               shut down then this Bundle contains the data it most recently
     *                               supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @SuppressLint({"ResourceType", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        setContentView(R.layout.register_activity);
=======

        // Inflate the layout using the View Binding Library
        register_view = RegisterActivityBinding.inflate(getLayoutInflater());
        setContentView(register_view.getRoot());

        // Find the views in the layout
        enter_name = register_view.enterName;
        enter_email = register_view.enterEmail;
        enter_password = register_view.enterPassword;
        apply_policy = register_view.checkboxWyrazamZgode;
        register_button = register_view.buttonZalozKonto;

        // Set the click listener for the register button
        register_button.setOnClickListener(this::validateUserInfo);
>>>>>>> 404e1de47e59331723cbf61f54c2ac904c8e6264
    }

    /**
     * Get the user's information from the input fields.
     *
     * @param v The view that was clicked.
     * @return A UserInfo object containing the user's information.
     */
    public UserInfo getUserInfo(View v){
        String name = String.valueOf(enter_name.getText());
        String email = String.valueOf(enter_email.getText());
        String password = String.valueOf(enter_password.getText());
        return new UserInfo(name, email, password);
    }

    /**
     * Validate the user's information and show a Snackbar message if any errors are found.
     *
     * @param view The view that was clicked.
     */
    public void validateUserInfo(View view){
        UserInfo info = getUserInfo(view);

        if(info.getName() == null || info.getName().isEmpty()){
            Snackbar.make(view, "Wprowadż imię użytkownika!", Snackbar.LENGTH_LONG).show();
            return;
        }

        if(!EmailValidator.isEmail(info.getEmail())){
            Snackbar.make(view, "Email jest niepoprawny!", Snackbar.LENGTH_LONG).show();
            //TODO check if email exists in database
            return;
        }

        if(info.getPassword() == null || info.getPassword().length() < 7){
            Snackbar.make(view, "Hasło musi być długie (co najmniej 7 znaków)", Snackbar.LENGTH_LONG).show();
            return;
        }

        if (apply_policy.isChecked()){
            Snackbar.make(view, "Udana rejestracja!", Snackbar.LENGTH_LONG).show();
            //TODO send data to server
        }
        else{
            Snackbar.make(view, "Musisz wyrazić zgodę na przetwarzanie danych", Snackbar.LENGTH_LONG).show();
        }
    }
}
