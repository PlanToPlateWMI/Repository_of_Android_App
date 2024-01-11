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
package pl.plantoplate.ui.main.recipes.recycler_views.view_holders;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.plantoplate.R;
import pl.plantoplate.ui.main.recipes.recycler_views.RecipeCategory;
import pl.plantoplate.ui.main.recipes.recycler_views.adapters.RecipeAdapter;
import pl.plantoplate.ui.main.recipes.recycler_views.listeners.SetupRecipeButtons;

public class RecipeCategoryViewHolder extends RecyclerView.ViewHolder {

    private final TextView recipeCategoryTextView;
    private final RecyclerView recipesRecyclerView;

    public RecipeCategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        recipeCategoryTextView = itemView.findViewById(R.id.сategory_of_product);
        recipesRecyclerView = itemView.findViewById(R.id.product_recycler_view);
    }

    public void bind(RecipeCategory recipeCategory, SetupRecipeButtons listener) {
        recipeCategoryTextView.setText(recipeCategory.getName());
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        RecipeAdapter recipeAdapter = new RecipeAdapter(recipeCategory.getRecipes());
        recipeAdapter.setUpRecipeButtons(listener);
        recipesRecyclerView.setAdapter(recipeAdapter);
    }
}
