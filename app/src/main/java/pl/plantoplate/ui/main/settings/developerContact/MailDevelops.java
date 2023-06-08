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

package pl.plantoplate.ui.main.settings.developerContact;

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

/**
 * Class responsible for sending an email to the developers
 */
public class MailDevelops extends Fragment {

    private FragmentDeveloperBinding fragmentDeveloperBinding;
    private TextView dev_adres;
    private Button button_zatwierdz;


    /**
     * Create the view
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        fragmentDeveloperBinding = FragmentDeveloperBinding.inflate(inflater, container, false);

        button_zatwierdz = fragmentDeveloperBinding.buttonZatwierdz;

        button_zatwierdz.setOnClickListener(this::sendMail);

        return fragmentDeveloperBinding.getRoot();
    }

    /**
     * Send an email to the developers
     * @param view
     */
    public void sendMail(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "plantoplatemobileapp@gmail.com" });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Plantoplate - question");
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, ""));
    }

}
