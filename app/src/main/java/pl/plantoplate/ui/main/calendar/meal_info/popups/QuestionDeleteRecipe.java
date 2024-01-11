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
package pl.plantoplate.ui.main.calendar.meal_info.popups;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.repository.MealRepository;
import pl.plantoplate.databinding.NewPopUpQuestionDeleteRecipeBinding;

public class QuestionDeleteRecipe extends DialogFragment {

    private CompositeDisposable compositeDisposable;
    private TextView acceptButton;
    private TextView cancelButton;
    private View.OnClickListener listener;

    public QuestionDeleteRecipe() {
        listener = v -> {
        };
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        NewPopUpQuestionDeleteRecipeBinding binding = NewPopUpQuestionDeleteRecipeBinding.inflate(getLayoutInflater());
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        compositeDisposable = new CompositeDisposable();

        setupViews(binding);
        setClicklisteners();
        return dialog;
    }

    public void setupViews(NewPopUpQuestionDeleteRecipeBinding binding){
        acceptButton = binding.buttonYes;
        cancelButton = binding.buttonNo;
    }

    private void setClicklisteners() {
        acceptButton.setOnClickListener(v -> deleteMealFromPlanned(v, requireArguments().getInt("mealId")));

        cancelButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Przepis nie został usunięty z kalendarza", Toast.LENGTH_SHORT).show();
            dismiss();
        });
    }

    /**
     * Deletes meal from planned meals
     * @param v view
     * @param mealId id of meal to delete
     */
    public void deleteMealFromPlanned(View v, int mealId) {
        String token = "Bearer " + requireContext().getSharedPreferences("prefs", Context.MODE_PRIVATE).getString("token", "");
        MealRepository mealRepository = new MealRepository();
        Disposable disposable = mealRepository.deleteMealById(token, mealId)
                .subscribe(
                        response -> {
                            Toast.makeText(requireContext(), "Przepis został usunięty z kalendarza", Toast.LENGTH_SHORT).show();
                            dismiss();
                            listener.onClick(v);
                        },
                        error -> Toast.makeText(requireContext(), error.getMessage(), Toast.LENGTH_SHORT).show());

        compositeDisposable.add(disposable);

    }

    public void setOnAcceptButtonClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}