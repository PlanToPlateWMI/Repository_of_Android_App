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

package pl.plantoplate.ui.main.settings.accountManagement.changeEmail;

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

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentEmailChange2Binding;
import pl.plantoplate.databinding.FragmentEmailChangeBinding;
import pl.plantoplate.repository.local.models.User;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.models.JwtResponse;
import pl.plantoplate.repository.remote.models.UserInfo;
import pl.plantoplate.repository.remote.user.UserRepository;
import pl.plantoplate.tools.SCryptStretcher;
import pl.plantoplate.ui.main.settings.SettingsFragment;
import pl.plantoplate.ui.main.settings.accountManagement.ChangeTheData;


/**
 * This class is responsible for changing the email address of the user.
 * It is used in the settings menu.
 */
public class ChangeEmailStep2Fragment extends Fragment {

    private UserRepository userRepository;
    private FragmentEmailChange2Binding fragmentEmailChange2Binding;

    private Button button_zatwierdz;

    private TextInputLayout wprowadz_nowy_email;

    private TextInputLayout wprowadz_nowy_email_ponownie;

    private SharedPreferences prefs;

    /**
     * Create the view
     * @param inflater The layout inflater
     * @param container The container
     * @param savedInstanceState The saved instance state
     * @return The view of the fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout using the View Binding Library
        fragmentEmailChange2Binding = FragmentEmailChange2Binding.inflate(inflater, container, false);

        // Get the button
        button_zatwierdz = fragmentEmailChange2Binding.buttonZatwierdz;

        // Get the text input
        wprowadz_nowy_email = fragmentEmailChange2Binding.wprowadzNowyEmail;
        wprowadz_nowy_email_ponownie = fragmentEmailChange2Binding.wprowadzNowyEmailPonownie;

        button_zatwierdz.setOnClickListener(v -> validateEmail());

        userRepository = new UserRepository();

        // get the shared preferences
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        return fragmentEmailChange2Binding.getRoot();
    }

    public void validateEmail(){

        String newEmail = Objects.requireNonNull(wprowadz_nowy_email.getEditText()).getText().toString().trim();
        String newEmailAgain = Objects.requireNonNull(wprowadz_nowy_email_ponownie.getEditText()).getText().toString().trim();

        if(newEmail.isEmpty()){
            wprowadz_nowy_email.setError("Pole nie może być puste");
            wprowadz_nowy_email.requestFocus();
            return;
        }

        if(newEmailAgain.isEmpty()){
            wprowadz_nowy_email_ponownie.setError("Pole nie może być puste");
            wprowadz_nowy_email_ponownie.requestFocus();
            return;
        }

        if(!newEmail.equals(newEmailAgain)){
            wprowadz_nowy_email_ponownie.setError("Adresy email nie są takie same");
            wprowadz_nowy_email_ponownie.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()){
            wprowadz_nowy_email.setError("Wprowadź poprawny adres email");
            wprowadz_nowy_email.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmailAgain).matches()){
            wprowadz_nowy_email_ponownie.setError("Wprowadź poprawny adres email");
            wprowadz_nowy_email_ponownie.requestFocus();
            return;
        }

        setNewPassword(newEmail);
    }

    public void setNewPassword(String newEmail){
        String token = "Bearer " + prefs.getString("token", "");

        String newPassword = SCryptStretcher.stretch(requireArguments().getString("password"), newEmail);

        userRepository.changePassword(token, newPassword, new ResponseCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {
                UserInfo userInfo = new UserInfo();
                userInfo.setEmail(newEmail);
                userInfo.setPassword(newPassword);

                setNewEmail(token, userInfo);
            }

            @Override
            public void onError(String errorMessage) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onFailure(String failureMessage) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), failureMessage, Toast.LENGTH_SHORT).show());
            }
        });
    }

    public void setNewEmail(String token, UserInfo userInfo){

        userRepository.changeEmail(token, userInfo, new ResponseCallback<JwtResponse>() {
            @Override
            public void onSuccess(JwtResponse jwt) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Email został zmieniony", Toast.LENGTH_LONG).show();
                });

                // save new email and role in shared preferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("email", userInfo.getEmail());
                editor.putString("role", jwt.getRole());
                editor.putString("token", jwt.getToken());
                editor.apply();

                replaceFragment(new ChangeTheData());
            }

            @Override
            public void onError(String errorMessage) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onFailure(String failureMessage) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), failureMessage, Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    /**
     * Replaces the current fragment with the specified fragment.
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
