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

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentChangeSelectorBinding;

/**
 * Fragment that allows the user to change the data.
 */
public class ChangeTheData extends Fragment {

    private FragmentChangeSelectorBinding fragmentChangeSelectorBinding;

    private Button zmiana_imienia;
    private Button zmiana_emaila;
    private Button zmiana_hasla;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentChangeSelectorBinding = FragmentChangeSelectorBinding.inflate(inflater, container, false);

        zmiana_imienia = fragmentChangeSelectorBinding.zmianaImienia;
        zmiana_emaila = fragmentChangeSelectorBinding.zmianaEmail;
        zmiana_hasla = fragmentChangeSelectorBinding.zmianaHasla;

        zmiana_imienia.setOnClickListener(v -> replaceFragment(new ChangeName()));

        return fragmentChangeSelectorBinding.getRoot();
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
