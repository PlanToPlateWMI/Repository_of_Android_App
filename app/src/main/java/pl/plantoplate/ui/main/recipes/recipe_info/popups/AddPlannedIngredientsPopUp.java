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
import pl.plantoplate.databinding.NewPopUpQuestionAddToShopingListBinding;

public class AddPlannedIngredientsPopUp extends DialogFragment {

    private TextView acceptButton;
    private TextView cancelButton;
    private View.OnClickListener listener;

    public AddPlannedIngredientsPopUp() {
        listener = v -> {
        };
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        NewPopUpQuestionAddToShopingListBinding binding = NewPopUpQuestionAddToShopingListBinding.inflate(getLayoutInflater());
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);

        setupViews(binding);
        setClickListeners();
        return dialog;
    }

    public void setupViews(NewPopUpQuestionAddToShopingListBinding binding){
        acceptButton = binding.buttonYes;
        cancelButton = binding.buttonNo;
    }

    public void setClickListeners(){
        cancelButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Produkty nie zostały dodane do listy zakupów", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        acceptButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Dodawanie do listy zakupów...", Toast.LENGTH_SHORT).show();
            listener.onClick(v);
            dismiss();
        });
    }

    public void setOnAcceptButtonClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
