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
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentSettingsBinding;
import pl.plantoplate.databinding.FragmentStorageBinding;
import pl.plantoplate.ui.login.LoginActivity;
import pl.plantoplate.tools.ApplicationState;
import pl.plantoplate.tools.ApplicationStateController;
import pl.plantoplate.ui.main.settings.groupCodeGeneration.ChangeTheData;
import pl.plantoplate.ui.main.settings.groupCodeGeneration.GroupCodeTypeActivity;
import pl.plantoplate.ui.main.settings.groupCodeGeneration.SettingsFragmentInside;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.ProductsDbaseFragment;
import pl.plantoplate.ui.main.storage.StorageFragment;
import pl.plantoplate.ui.main.storage.StorageInsideFragment;

/**
 * A fragment that displays the app settings and allows the user to change them.
 */
public class SettingsFragment extends Fragment{

    private FragmentSettingsBinding fragmentSettingsBinding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater, container, false);
        replaceFragment(new SettingsFragmentInside());
        return fragmentSettingsBinding.getRoot();
    }

    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.settings_default, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

}