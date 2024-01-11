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
package pl.plantoplate.ui.main.calendar.meal_info.recycler_views.view_holders;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.math.BigDecimal;
import java.math.RoundingMode;
import pl.plantoplate.data.remote.models.recipe.Ingredient;
import pl.plantoplate.databinding.ItemSkladnikForCalendarBinding;

/**
 * This class is responsible for setting up the ingredients in RecyclerView.
 */
public class MealIngredientsViewHolder extends RecyclerView.ViewHolder {
    private final TextView ingredientName;
    private final TextView ingredientAmount;

    public MealIngredientsViewHolder(@NonNull View itemView) {
        super(itemView);

        ItemSkladnikForCalendarBinding itemSkladnikForCalendarBinding = ItemSkladnikForCalendarBinding.bind(itemView);
        ingredientName = itemSkladnikForCalendarBinding.textViewSkladnik;
        ingredientAmount = itemSkladnikForCalendarBinding.textViewIloscSkladnika;

    }

    public void bind(Ingredient ingredient) {
        float amount = BigDecimal.valueOf(Float.parseFloat(String.valueOf(ingredient.getQuantity())))
                .setScale(3, RoundingMode.HALF_UP)
                .floatValue();
        ingredientName.setText(ingredient.getIngredientName());
        String ingredientAmountText = amount + " " + ingredient.getUnit().toLowerCase();
        ingredientAmount.setText(ingredientAmountText);
    }
}