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
package pl.plantoplate.ui.main.settings.account_management.delete_account;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import pl.plantoplate.databinding.FragmentRemoveAccountBinding;

/**
 * A fragment that allows the user to delete their account.
 */
public class DeleteAccountFragment extends Fragment {

    private Button acceptButton;
    private SharedPreferences prefs;

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
        FragmentRemoveAccountBinding fragmentRemoveAccountBinding  =
                FragmentRemoveAccountBinding.inflate(inflater, container, false);
        prefs = requireActivity().getSharedPreferences("prefs", MODE_PRIVATE);

        initViews(fragmentRemoveAccountBinding);
        setClickListeners();
        return fragmentRemoveAccountBinding.getRoot();
    }

    /**
     * Initialize the views
     * @param fragmentRemoveAccountBinding The binding for the fragment
     */
    public void initViews(FragmentRemoveAccountBinding fragmentRemoveAccountBinding) {
        acceptButton = fragmentRemoveAccountBinding.buttonZatwierdz;
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
        String email = prefs.getString("email", "");

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "plantoplatemobileapp@gmail.com" });
        intent.putExtra(Intent.EXTRA_SUBJECT, "Plantoplate: usuwanie konta");
        intent.putExtra(Intent.EXTRA_TEXT, "Szanowni Państwo, \n\n" +
                "Zwracam się z prośbą o usunięcie mojego konta z aplikacji Plantoplate. \n\n" +
                "Email: \n\n" + email + "\n\n" +
                "Pozdrawiam. \n");
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, ""));
    }

}
