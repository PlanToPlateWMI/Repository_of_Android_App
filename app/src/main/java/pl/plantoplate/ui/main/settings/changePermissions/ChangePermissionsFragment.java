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

/**
 * This fragment is responsible for changing the permissions of the user.
 */
package pl.plantoplate.ui.main.settings.changePermissions;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;

import pl.plantoplate.databinding.FragmentNameChangeBinding;
import pl.plantoplate.databinding.FragmentPermissionsChangeBinding;
import pl.plantoplate.repository.remote.user.UserRepository;

public class ChangePermissionsFragment extends Fragment {

    private FragmentPermissionsChangeBinding fragmentPermissionsChangeBinding;
    private SharedPreferences prefs;
    private UserRepository userRepository;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentPermissionsChangeBinding = FragmentPermissionsChangeBinding.inflate(inflater, container, false);

        userRepository = new UserRepository();

        // get shared preferences object
        // prefs = requireActivity().getSharedPreferences("prefs", AppCompatActivity.MODE_PRIVATE);

        return fragmentPermissionsChangeBinding.getRoot();
    }


}
