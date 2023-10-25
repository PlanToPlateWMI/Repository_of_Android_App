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
import timber.log.Timber;

/**
 * A fragment that displays the app settings and allows the user to change them.
 */
public class SettingsFragment extends Fragment{
    /**
     * Creates the view for the fragment.
     *
     * @param inflater the layout inflater for the fragment
     * @param container the view group container for the fragment
     * @param savedInstanceState the saved instance state for the fragment
     * @return the view for the fragment as a View object.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timber.d("onCreate() called");
        FragmentSettingsBinding fragmentSettingsBinding = FragmentSettingsBinding.inflate(inflater, container, false);
        replaceFragment(new SettingsInsideFragment());
        return fragmentSettingsBinding.getRoot();
    }

    /**
     * Replaces the current fragment with the specified fragment.
     *
     * @param fragment the fragment to replace the current fragment with
     */
    private void replaceFragment(Fragment fragment) {
        Timber.d("Replacing fragment with tag: %s", "SETTINGS_INSIDE");
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.settings_default, fragment);
        transaction.addToBackStack("SETTINGS_INSIDE");
        transaction.commit();
    }
}