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
package pl.plantoplate.ui.main.recipes.recipe_info.recycler_views.view_holders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.math.BigDecimal;
import java.math.RoundingMode;
import pl.plantoplate.data.remote.models.recipe.Ingredient;
import pl.plantoplate.data.remote.models.user.Role;
import pl.plantoplate.databinding.ItemSkladnikBinding;

public class RecipeIngredientsViewHolder extends RecyclerView.ViewHolder {

    private final CheckBox checkBox;
    private final TextView ingredientName;
    private final TextView ingredientAmount;

    public RecipeIngredientsViewHolder(@NonNull View itemView) {
        super(itemView);

        ItemSkladnikBinding itemSkladnikBinding = ItemSkladnikBinding.bind(itemView);
        checkBox = itemSkladnikBinding.checkBox;
        ingredientName = itemSkladnikBinding.textViewSkladnik;
        ingredientAmount = itemSkladnikBinding.textViewIloscSkladnika;

    }

    public void setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        checkBox.setOnCheckedChangeListener(listener);
    }

    public void bind(Ingredient ingredient, boolean isSelected, Role role) {
        checkBox.setVisibility(role == Role.ROLE_ADMIN ? View.VISIBLE : View.GONE);
        checkBox.setChecked(isSelected);
        float amount = BigDecimal.valueOf(Float.parseFloat(String.valueOf(ingredient.getQuantity())))
                .setScale(3, RoundingMode.HALF_UP)
                .floatValue();
        ingredientName.setText(ingredient.getIngredientName());
        String ingredientAmountText = amount + " " + ingredient.getUnit().toLowerCase();
        ingredientAmount.setText(ingredientAmountText);
    }
}
