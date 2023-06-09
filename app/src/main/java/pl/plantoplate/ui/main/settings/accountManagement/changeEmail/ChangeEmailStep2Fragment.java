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

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputLayout;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentEmailChange2Binding;
import pl.plantoplate.databinding.FragmentEmailChangeBinding;
import pl.plantoplate.repository.remote.user.UserRepository;
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

    /**
     * Create the view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
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

        button_zatwierdz.setOnClickListener(v -> replaceFragment(new ChangeTheData()));

        userRepository = new UserRepository();

        return fragmentEmailChange2Binding.getRoot();
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
