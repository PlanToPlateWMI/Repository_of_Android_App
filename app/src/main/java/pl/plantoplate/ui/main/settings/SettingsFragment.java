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
package pl.plantoplate.ui.main.settings;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pl.plantoplate.databinding.FragmentSettingsBinding;
import pl.plantoplate.ui.main.settings.groupCodeGeneration.GroupCodeTypeActivity;

/**
 * A fragment that displays the app settings and allows the user to change them.
 */
public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding settings_view;

    private Button generate_group_code_button;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout using the View Binding Library
        settings_view = FragmentSettingsBinding.inflate(inflater, container, false);

        // Get the button for generating group code
        generate_group_code_button = settings_view.buttonWygenerowanieKodu;

        // Set the onClickListener for the button
        generate_group_code_button.setOnClickListener(this::chooseGroupCodeType);

        return settings_view.getRoot();
    }

    /**
     * Starts the GroupCodeTypeActivity.
     * @param view The view object that was clicked.
     */
    public void chooseGroupCodeType(View view) {
        Intent intent = new Intent(this.getContext(), GroupCodeTypeActivity.class);
        startActivity(intent);
    }

}