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

package pl.plantoplate.ui.main.settings.accountManagement.changePassword;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.repository.UserRepository;
import pl.plantoplate.databinding.FragmentPasswordChange2Binding;
import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.tools.SCryptStretcher;
import pl.plantoplate.ui.login.LoginActivity;


/**
 * This fragment is used to change the password.
 */
public class PasswordChangeNewPasswords extends Fragment {

    private FragmentPasswordChange2Binding fragmentPasswordChange2Binding;

    private UserRepository userRepository;

    private Button button_zatwierdz;

    private TextInputLayout wprowadz_nowe_haslo;

    private TextInputLayout wprowadz_nowe_haslo_ponownie;

    private SharedPreferences prefs;


    /**
     * Create the view
     * @param inflater The layout inflater
     * @param container The container
     * @param savedInstanceState The saved instance state
     * @return The view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout using the View Binding Library
        fragmentPasswordChange2Binding = FragmentPasswordChange2Binding.inflate(inflater, container, false);

        // Get the button
        button_zatwierdz = fragmentPasswordChange2Binding.buttonZatwierdz;

        // Get the text input
        wprowadz_nowe_haslo = fragmentPasswordChange2Binding.wprowadzNoweHaslo;
        wprowadz_nowe_haslo_ponownie = fragmentPasswordChange2Binding.wprowadzNoweHasloPonownie;

        userRepository = new UserRepository();

        prefs = requireActivity().getSharedPreferences("prefs", MODE_PRIVATE);

        button_zatwierdz.setOnClickListener(v -> validatePasswords());

        return fragmentPasswordChange2Binding.getRoot();
    }

    public void validatePasswords(){
        String password = Objects.requireNonNull(wprowadz_nowe_haslo.getEditText()).getText().toString();
        String password2 = Objects.requireNonNull(wprowadz_nowe_haslo_ponownie.getEditText()).getText().toString();

        if (password.isEmpty()) {
            wprowadz_nowe_haslo.getEditText().setError("Wprowadź hasło");
            wprowadz_nowe_haslo.requestFocus();
            return;
        } else if (!password.equals(password2)) {
            wprowadz_nowe_haslo_ponownie.getEditText().setError("Hasła nie są takie same");
            wprowadz_nowe_haslo_ponownie.requestFocus();
            return;
        }

        if (password.length() < 7) {
            System.out.println("Hasło musi mieć co najmniej 7 znaków");
            wprowadz_nowe_haslo.getEditText().setError("Hasło musi mieć co najmniej 7 znaków");
            wprowadz_nowe_haslo.requestFocus();
            return;
        }

        String email = prefs.getString("email", "");

        password = SCryptStretcher.stretch(password, email);

        changePassword(password);
    }

    private void changePassword(String password) {

        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = userRepository.changePassword(token, password)
                .subscribe(userInfo -> {

                    Toast.makeText(requireActivity(), "Hasło zostało zmienione", Toast.LENGTH_SHORT).show();
                    exitAccount();

                }, throwable -> {

                    Toast.makeText(requireActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();

                });
    }

    /**
     * Logs the user out of the app.
     */
    public void exitAccount() {
        //delete the user's data from the shared preferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("name");
        editor.remove("email");
        editor.remove("password");
        editor.remove("role");
        editor.remove("token");
        editor.apply();

        //go back to the login screen
        Intent intent = new Intent(this.getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        // save the app state
        saveAppState(ApplicationState.LOGIN);
    }

    /**
     * Saves the app state to the shared preferences.
     * @param applicationState The app state to save.
     */
    public void saveAppState(ApplicationState applicationState) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("applicationState", applicationState.toString());
        editor.apply();
    }


    /**
     * Replaces the current fragment with the specified fragment.
     *
     * @param fragment The fragment to replace the current fragment with.
     */
    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.settings_default, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
