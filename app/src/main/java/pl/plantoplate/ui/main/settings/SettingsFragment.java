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

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentSettingsBinding;

/**
 * A fragment that displays the app settings and allows the user to change them.
 */
public class SettingsFragment extends Fragment{

    private FragmentSettingsBinding fragmentSettingsBinding;


    /**
     * Creates the view for the fragment.
     *
     * @param inflater the layout inflater
     * @param container the view group container
     * @param savedInstanceState the saved instance state
     * @return the view for the fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater, container, false);
        replaceFragment(new SettingsInsideFragment());
        return fragmentSettingsBinding.getRoot();
    }

    /**
     * Replaces the current fragment with the specified fragment.
     *
     * @param fragment the fragment to replace the current fragment with
     */
    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.settings_default, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

}