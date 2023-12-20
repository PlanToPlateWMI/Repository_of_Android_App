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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import pl.plantoplate.databinding.FragmentDeveloperBinding;

/**
 * Class responsible for sending an email to the developers
 */
public class MailDevelops extends Fragment {

    private Button acceptButton;

    /**
     * Create the view
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container  If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState  If non-null, this fragment is being re-constructed from a previous saved state as given here.
     * @return the view of the fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentDeveloperBinding fragmentDeveloperBinding = FragmentDeveloperBinding.inflate(inflater, container, false);

        initViews(fragmentDeveloperBinding);
        setClickListeners();
        return fragmentDeveloperBinding.getRoot();
    }

    /**
     * Initialize the views
     * @param fragmentDeveloperBinding The binding object
     */
    public void initViews(FragmentDeveloperBinding fragmentDeveloperBinding) {
        acceptButton = fragmentDeveloperBinding.buttonZatwierdz;
    }

    /**
     * Set the click listeners
     */
    public void setClickListeners() {
        acceptButton.setOnClickListener(v -> sendMail());
    }

    /**
     * Send an email to the developers
     */
    public void sendMail() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "plantoplatemobileapp@gmail.com" });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Plantoplate - question");
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, ""));
    }
}
