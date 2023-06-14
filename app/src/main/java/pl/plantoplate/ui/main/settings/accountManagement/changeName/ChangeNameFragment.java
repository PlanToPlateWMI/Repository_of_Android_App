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

package pl.plantoplate.ui.main.settings.accountManagement.changeName;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import pl.plantoplate.databinding.FragmentNameChangeBinding;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.models.UserInfo;
import pl.plantoplate.repository.remote.user.UserRepository;

/**
 * This fragment is responsible for changing the name of the user.
 */
public class ChangeNameFragment extends Fragment {

    private FragmentNameChangeBinding fragmentNameChangeBinding;

    private Button button_zatwierdz;
    private TextInputLayout wprowadz_imie;

    private SharedPreferences prefs;
    private UserRepository userRepository;

    /**
     * Create the view for this fragment, get the buttons for choosing group code type and set the
     * @param inflater The layout inflater
     * @param container The container
     * @param savedInstanceState The saved instance state
     * @return The view for this fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentNameChangeBinding = FragmentNameChangeBinding.inflate(inflater, container, false);

        button_zatwierdz = fragmentNameChangeBinding.buttonZatwierdz;
        wprowadz_imie = fragmentNameChangeBinding.wprowadzImie;

        userRepository = new UserRepository();

        // get shared preferences object
        prefs = requireActivity().getSharedPreferences("prefs", AppCompatActivity.MODE_PRIVATE);
//        String username = prefs.getString("username", "");
//        wprowadz_imie.getEditText().setText(username);

        // set listener for the button
        button_zatwierdz.setOnClickListener(this::onAcceptName);

        return fragmentNameChangeBinding.getRoot();
    }

    /**
     * This method is called when the user clicks the button to accept the name.
     * @param view
     */
    public void onAcceptName(View view) {
        String username = String.valueOf(Objects.requireNonNull(wprowadz_imie.getEditText()).getText());
        if (username.isEmpty()) {
            wprowadz_imie.setError("Pole nie może być puste");
            return;
        }

        String token = "Bearer " + prefs.getString("token", "");

        userRepository.changeUsername(token, username, new ResponseCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo response) {
                requireActivity().runOnUiThread(() -> {
                    prefs.edit().putString("username", username).apply();
                    Toast.makeText(requireActivity(), "Imię zostało zmienione na: " + username, Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                });

                // pop the fragment from the back stack
                requireActivity().getSupportFragmentManager().popBackStack();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String failureMessage) {
                Toast.makeText(requireActivity(), failureMessage, Toast.LENGTH_SHORT).show();
            }
        });

    }

}