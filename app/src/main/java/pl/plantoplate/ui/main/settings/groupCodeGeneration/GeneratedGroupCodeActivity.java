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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentGeneratedCodeBinding;
import pl.plantoplate.databinding.FragmentNameChangeBinding;
import pl.plantoplate.ui.main.ActivityMain;

/**
 * An activity that displays the generated group code.
 */
public class GeneratedGroupCodeActivity extends Fragment {

    private FragmentGeneratedCodeBinding generated_code_view;

    private TextInputEditText group_code;
    private Button apply_button;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        generated_code_view = FragmentGeneratedCodeBinding.inflate(inflater, container, false);

        // Get the TextView for the group code
        group_code = generated_code_view.kod;

        // Get the button for accepting the code
        apply_button = generated_code_view.applyButton;

        // Get the button for accepting the code
        apply_button.setOnClickListener(this::applyCode);

        setGroupCodeView();

        return generated_code_view.getRoot();
    }

    /**
     * Sets the group code view, so that the user can see the generated group code.
     */
    public void setGroupCodeView() {
        String groupCode = requireArguments().getString("group_code");

        // set the group code text view
        group_code.setText(groupCode);
    }

    /**
     * Starts the MainActivity.
     * @param view The view object that was clicked.
     */
    private void applyCode(View view) {
        replaceFragment(new SettingsFragmentInside());
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
