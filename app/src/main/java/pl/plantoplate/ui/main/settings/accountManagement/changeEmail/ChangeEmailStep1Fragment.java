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
import pl.plantoplate.databinding.FragmentEmailChangeBinding;
import pl.plantoplate.repository.remote.user.UserRepository;


/**
 * This class is responsible for changing the user's email address.
 */
public class ChangeEmailStep1Fragment extends Fragment {

    private FragmentEmailChangeBinding fragmentEmailChangeBinding;
    private UserRepository userRepository;
    private Button button_zatwierdz;
    private TextInputLayout wprowadz_haslo;

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
        fragmentEmailChangeBinding = FragmentEmailChangeBinding.inflate(inflater, container, false);

        // Get the button
        button_zatwierdz = fragmentEmailChangeBinding.buttonZatwierdz;

        // Get the text input
        wprowadz_haslo = fragmentEmailChangeBinding.wprowadzHaslo;

        button_zatwierdz.setOnClickListener(v -> replaceFragment(new ChangeEmailStep2Fragment()));

        userRepository = new UserRepository();

        return fragmentEmailChangeBinding.getRoot();
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
