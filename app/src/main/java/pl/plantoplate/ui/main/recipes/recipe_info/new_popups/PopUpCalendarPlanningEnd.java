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
package pl.plantoplate.ui.main.recipes.recipe_info.new_popups;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.models.meal.MealPlanNew;
import pl.plantoplate.data.remote.repository.MealRepository;
import pl.plantoplate.databinding.NewPopUpQuestionSynhronizationOnBinding;

/**
 * Class for displaying pop up window with question about synchronization.
 */
public class PopUpCalendarPlanningEnd extends DialogFragment {

    private CompositeDisposable compositeDisposable;
    private CheckBox checkBox;
    private TextView acceptButton;
    private TextView cancelButton;
    private SharedPreferences prefs;
    private MealPlanNew addMealProducts;

    public PopUpCalendarPlanningEnd() {
    }

    public PopUpCalendarPlanningEnd(MealPlanNew addMealProducts) {
        this.addMealProducts = addMealProducts;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        NewPopUpQuestionSynhronizationOnBinding binding = NewPopUpQuestionSynhronizationOnBinding.inflate(getLayoutInflater());
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE);
        compositeDisposable = new CompositeDisposable();

        setupViews(binding);
        setClickListeners();
        return dialog;
    }

    public void setupViews(NewPopUpQuestionSynhronizationOnBinding binding){
        checkBox = binding.checkBox;
        acceptButton = binding.buttonYes;
        cancelButton = binding.buttonNo;
    }
    public void setClickListeners(){
        cancelButton.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "Produkty nie zostały dodany do listy zakupów", Toast.LENGTH_SHORT).show();
            dismiss();
        });

        acceptButton.setOnClickListener(v -> {
            if(checkBox.isChecked()){
                Toast.makeText(requireContext(), "Synchronizacja została włączona", Toast.LENGTH_SHORT).show();
                addMealProducts.setIsSynchronize(true);
            } else {
                Toast.makeText(requireContext(), "Synchronizacja nie została włączona", Toast.LENGTH_SHORT).show();
                addMealProducts.setIsSynchronize(false);
            }
            planMeal();
        });
    }

    public void planMeal(){
        String token = "Bearer " + prefs.getString("token", "");
        MealRepository mealRepository = new MealRepository();
        Disposable disposable = mealRepository.planMealV2(token, addMealProducts)
                .subscribe(mealPlan -> {
                    Toast.makeText(requireContext(), "Produkty zostały dodane do listy zakupów", Toast.LENGTH_SHORT).show();
                    dismiss();
                }, throwable -> Toast.makeText(requireContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
        compositeDisposable.add(disposable);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }
}