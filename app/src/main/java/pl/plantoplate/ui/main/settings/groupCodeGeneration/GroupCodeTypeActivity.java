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

package pl.plantoplate.ui.main.settings.groupCodeGeneration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentChoiceAdultOrChildBinding;
import pl.plantoplate.databinding.FragmentNameChangeBinding;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.group.GroupRepository;

/**
 * An activity that allows the user to choose the type of group code to generate.
 */
public class GroupCodeTypeActivity extends Fragment {

    private FragmentChoiceAdultOrChildBinding choose_group_code_type_view;

    private Button child_code_button;
    private Button adult_code_button;

    private SharedPreferences prefs;

    /**
     * Create the view for this fragment, get the buttons for choosing group code type and set the
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        choose_group_code_type_view = FragmentChoiceAdultOrChildBinding.inflate(inflater, container, false);


        // Get the buttons for choosing group code type
        child_code_button = choose_group_code_type_view.codeForChild;
        adult_code_button = choose_group_code_type_view.codeForAdult;

        // Get shared preferences
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        // Set the onClickListeners for the buttons
        child_code_button.setOnClickListener(this::generateChildCode);
        adult_code_button.setOnClickListener(this::generateAdultCode);

        return choose_group_code_type_view.getRoot();
    }

    /**
     * Set the User (Child) role of the user and call the generateGroupCode method.
     *
     * @param view The view that was clicked.
     */
    public void generateChildCode(View view) {
        String role = "USER";
        generateGroupCode(view, role);
    }

    /**
     * Set the ADMIN (Parent) role of the user and call the generateGroupCode method.
     *
     * @param view The view that was clicked.
     */
    public void generateAdultCode(View view) {
        String role = "ADMIN";
        generateGroupCode(view, role);
    }

    /**
     * Generate a group code for the user.
     *
     * @param view The view that was clicked.
     * @param role The role of the user.
     */
    public void generateGroupCode(View view, String role){
        String token = "Bearer " + prefs.getString("token", "");

        GroupRepository groupRepository = new GroupRepository();
        groupRepository.generateGroupCode(token, role, new ResponseCallback<String>() {

            @Override
            public void onSuccess(String groupCode) {
                Fragment fragment = new GeneratedGroupCodeActivity();
                Bundle bundle = new Bundle();
                bundle.putString("group_code", groupCode);
                fragment.setArguments(bundle);
                replaceFragment(fragment);
            }

            @Override
            public void onError(String errorMessage) {
                Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String failureMessage) {
                Snackbar.make(view, failureMessage, Snackbar.LENGTH_LONG).show();
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
