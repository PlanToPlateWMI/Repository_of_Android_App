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
import com.google.android.material.textfield.TextInputEditText;
import java.util.Objects;
import pl.plantoplate.databinding.FragmentGeneratedCodeBinding;
import timber.log.Timber;

/**
 * An activity that displays the generated group code.
 */
public class GeneratedGroupCodeActivity extends Fragment {

    private TextInputEditText groupCodeEditText;
    private Button applyButton;

    /**
     * Creates the view for the activity.
     * @param inflater The layout inflater
     * @param container The container for the fragment
     * @param savedInstanceState The saved instance state
     * @return The view for this fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentGeneratedCodeBinding fragmentGeneratedCodeBinding = FragmentGeneratedCodeBinding.inflate(inflater,
                container, false);

        initViews(fragmentGeneratedCodeBinding);
        setClickListeners();
        return fragmentGeneratedCodeBinding.getRoot();
    }

    /**
     * Initializes the views for the activity.
     * @param fragmentGeneratedCodeBinding The binding for the fragment
     */
    public void initViews(FragmentGeneratedCodeBinding fragmentGeneratedCodeBinding){
        Timber.d("Initializing views...");
        groupCodeEditText = fragmentGeneratedCodeBinding.kod;
        applyButton = fragmentGeneratedCodeBinding.applyButton;

        setGroupCodeTextView();
    }

    /**
     * Sets the group code view, so that the user can see the generated group code.
     */
    public void setGroupCodeTextView() {
        String groupCode = requireArguments().getString("group_code");
        groupCodeEditText.setText(groupCode);
    }

    /**
     * Sets the click listeners for the buttons.
     */
    private void setClickListeners() {
        Timber.d("Setting click listeners...");
        applyButton.setOnClickListener(this::shareInviteCode);
    }

    /**
     * Share the group invite code with other users.
     * @param view The view
     */
    public void shareInviteCode(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_TEXT, "Możesz dołączyć do mojej grupy w aplikacji PlanToPlate. " +
                "Kod zaproszeniowy do grupy: "
                + Objects.requireNonNull(groupCodeEditText.getText()) + ".\n\n"
                + "Pobierz aplikację Plantoplate: " +
                "https://play.google.com/store/apps/details?id=pl.plantoplate&pcampaignid=web_share");
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, ""));
    }
}
