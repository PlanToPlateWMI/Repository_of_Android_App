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
import pl.plantoplate.databinding.FragmentPasswordChangeBinding;
import pl.plantoplate.tools.SCryptStretcher;


/**
 * This fragment is used to change the password.
 */
public class PasswordChangeOldPassword extends Fragment {

    private FragmentPasswordChangeBinding fragmentPasswordChangeBinding;

    private UserRepository userRepository;

    private Button button_zatwierdz;

    private TextInputLayout wprowadz_stare_haslo;

    private SharedPreferences prefs;

    /**
     * Create the view
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container This is the parent view that the fragment's UI should be attached to
     * @param savedInstanceState This fragment is being re-constructed from a previous saved state as given here
     * @return Return the View for the fragment's UI, or null
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout using the View Binding Library
        fragmentPasswordChangeBinding = FragmentPasswordChangeBinding.inflate(inflater, container, false);

        // Get the button
        button_zatwierdz = fragmentPasswordChangeBinding.buttonZatwierdz;

        // Get the text input
        wprowadz_stare_haslo = fragmentPasswordChangeBinding.wprowadzStareHaslo;

        userRepository = new UserRepository();

        prefs = requireActivity().getSharedPreferences("prefs", MODE_PRIVATE);

        button_zatwierdz.setOnClickListener(v -> validatePassword());

        return fragmentPasswordChangeBinding.getRoot();
    }

    public void validatePassword() {
        String password = Objects.requireNonNull(wprowadz_stare_haslo.getEditText()).getText().toString().trim();
        if (password.isEmpty()) {
            wprowadz_stare_haslo.setError("Pole nie może być puste");
            return;
        }

        String email = prefs.getString("email", "");

        String passwordStretched = SCryptStretcher.stretch(password, email);

        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = userRepository.validatePasswordMatch(token, passwordStretched)
                .subscribe(response -> {

                    Fragment fragment = new PasswordChangeNewPasswords();
                    replaceFragment(fragment);

                }, throwable -> {
                    Toast.makeText(requireActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
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
