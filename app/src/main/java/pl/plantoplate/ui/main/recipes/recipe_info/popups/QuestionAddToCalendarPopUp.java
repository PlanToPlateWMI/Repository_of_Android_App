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
package pl.plantoplate.ui.main.recipes.recipe_info.popups;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import pl.plantoplate.databinding.NewPopUpQuestionAddToCalendarBinding;

public class QuestionAddToCalendarPopUp extends DialogFragment {

    private TextView acceptButton;
    private TextView cancelButton;
    private View.OnClickListener listener;

    public QuestionAddToCalendarPopUp() {
        listener = v -> {
        };
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        NewPopUpQuestionAddToCalendarBinding binding = NewPopUpQuestionAddToCalendarBinding.inflate(getLayoutInflater());
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);

        setupViews(binding);
        setClicklisteners();
        return dialog;
    }

    public void setupViews(NewPopUpQuestionAddToCalendarBinding binding){
        acceptButton = binding.buttonYes;
        cancelButton = binding.buttonNo;
    }

    private void setClicklisteners() {
        acceptButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Trwa planowanie...", Toast.LENGTH_SHORT).show();
            listener.onClick(v);
            dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Przepis nie został dodany do kalendarza", Toast.LENGTH_SHORT).show();
            dismiss();
        });
    }

    public void setOnAcceptButtonClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
