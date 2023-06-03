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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import pl.plantoplate.databinding.FragmentChangeSelectorBinding;
import pl.plantoplate.databinding.FragmentDeveloperBinding;


public class MailDevelops extends Fragment {

    private FragmentDeveloperBinding fragmentDeveloperBinding;
    private TextView dev_adres;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentDeveloperBinding = FragmentDeveloperBinding.inflate(inflater, container, false);

        dev_adres = fragmentDeveloperBinding.devAdres;

        return fragmentDeveloperBinding.getRoot();
    }

}
