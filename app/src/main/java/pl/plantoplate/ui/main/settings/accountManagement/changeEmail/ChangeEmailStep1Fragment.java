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
import pl.plantoplate.databinding.FragmentEmailChangeBinding;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.models.Message;
import pl.plantoplate.repository.remote.user.UserRepository;
import pl.plantoplate.tools.SCryptStretcher;


/**
 * This class is responsible for changing the user's email address.
 */
public class ChangeEmailStep1Fragment extends Fragment {

    private FragmentEmailChangeBinding fragmentEmailChangeBinding;

    private UserRepository userRepository;
    private Button button_zatwierdz;
    private TextInputLayout wprowadz_haslo;

    private SharedPreferences sharedPreferences;

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
        fragmentEmailChangeBinding = FragmentEmailChangeBinding.inflate(inflater, container, false);

        // Get the button
        button_zatwierdz = fragmentEmailChangeBinding.buttonZatwierdz;

        // Get the text input
        wprowadz_haslo = fragmentEmailChangeBinding.wprowadzHaslo;

        button_zatwierdz.setOnClickListener(v -> validatePassword());

        userRepository = new UserRepository();

        sharedPreferences = requireActivity().getSharedPreferences("prefs", 0);

        return fragmentEmailChangeBinding.getRoot();
    }

    public void validatePassword() {
        String password = Objects.requireNonNull(wprowadz_haslo.getEditText()).getText().toString().trim();
        if (password.isEmpty()) {
            wprowadz_haslo.setError("Pole nie może być puste");
            return;
        }

        String email = sharedPreferences.getString("email", "");

        String passwordStretched = SCryptStretcher.stretch(password, email);

        String token = "Bearer " + sharedPreferences.getString("token", "");

        userRepository.validatePasswordMatch(token, passwordStretched, new ResponseCallback<Message>() {
            @Override
            public void onSuccess(Message response) {

                Bundle bundle = new Bundle();
                bundle.putString("password", password);

                Fragment fragment = new ChangeEmailStep2Fragment();
                fragment.setArguments(bundle);

                replaceFragment(fragment);
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
