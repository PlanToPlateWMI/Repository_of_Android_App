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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentGeneratedCodeBinding;

/**
 * An activity that displays the generated group code.
 */
public class GeneratedGroupCodeActivity extends Fragment {

    private FragmentGeneratedCodeBinding generated_code_view;

    private TextInputEditText group_code;
    private Button przekarz_button;

    /**
     * Creates the view for the activity.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        generated_code_view = FragmentGeneratedCodeBinding.inflate(inflater, container, false);

        // Get the TextView for the group code
        group_code = generated_code_view.kod;

        // Get the button for accepting the code
        przekarz_button = generated_code_view.applyButton;

        // Get the button for accepting the code
        przekarz_button.setOnClickListener(this::sendCode);

        setGroupCodeView();

        return generated_code_view.getRoot();
    }

    /**
     * Sets the group code view, so that the user can see the generated group code.
     */
    public void setGroupCodeView() {
        String groupCode = requireArguments().getString("group_code");

        // set the group code text view
        group_code.setText(groupCode);
    }

//    /**
//     * Starts the MainActivity.
//     * @param view The view object that was clicked.
//     */
//    private void applyCode(View view) {
//        replaceFragment(new SettingsInsideFragment());
//    }

    /**
     * Send a code to anouther user.
     * @param view
     */
    public void sendCode(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_TEXT, "Możesz dołączyć do mojej grupy w aplikacji PlanToPlate. Kod zaproszeniowy do grupy: "
                + Objects.requireNonNull(group_code.getText()).toString() + ".\n\n"
                + "Pobierz aplikację Plantoplate: tu_musi_byc_link_do_aplikacji");
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, ""));
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
