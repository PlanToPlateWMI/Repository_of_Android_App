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
package pl.plantoplate.ui.main.calendar.recycler_views.view_holders;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.plantoplate.databinding.ItemCategoryKalendarzBinding;
import pl.plantoplate.ui.main.calendar.recycler_views.adapters.ConcreteMealAdapter;
import pl.plantoplate.ui.main.calendar.recycler_views.listeners.SetupMealItem;
import pl.plantoplate.ui.main.calendar.recycler_views.models.MealTypes;

/**
 * ViewHolder class for displaying meal types in a RecyclerView.
 */
public class MealTypeViewHolder extends RecyclerView.ViewHolder{

    private final TextView mealTypeTextView;
    private final RecyclerView mealsRecyclerView;

    public MealTypeViewHolder(@NonNull View itemView) {
        super(itemView);

        ItemCategoryKalendarzBinding binding = ItemCategoryKalendarzBinding.bind(itemView);
        mealTypeTextView = binding.categoryOfProduct;
        mealsRecyclerView = binding.productRecyclerView;
    }

    public void bind(MealTypes mealType, SetupMealItem listener) {
        mealTypeTextView.setText(mealType.getMealType().getPolishName());

        ConcreteMealAdapter concreteMealAdapter = new ConcreteMealAdapter();
        concreteMealAdapter.setMeals(mealType.getMeals());
        concreteMealAdapter.setUpMealItem(listener);
        mealsRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mealsRecyclerView.setAdapter(concreteMealAdapter);
    }
}