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

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.recipe.Recipe;
import pl.plantoplate.ui.main.recipes.recycler_views.listeners.SetupRecipeButtons;

public class RecipeViewHolder extends RecyclerView.ViewHolder {

    private LinearLayout recipeLinearLayout;
    private ImageView recipeImageView;
    private TextView recipeNameTextView;
    private TextView recipeTimeToCookTextView;

    public RecipeViewHolder(@NonNull View itemView) {
        super(itemView);

        recipeLinearLayout = itemView.findViewById(R.id.layoutRecipeItem);
        recipeImageView = itemView.findViewById(R.id.iconUncheck_kupione);
        recipeNameTextView = itemView.findViewById(R.id.textView2);
        recipeTimeToCookTextView = itemView.findViewById(R.id.textView5);
    }

    public void bind(Recipe recipe, SetupRecipeButtons listener) {
        recipeLinearLayout.findViewById(R.id.wegeText).setVisibility(recipe.getVege() ? View.VISIBLE : View.GONE);
        if (TextUtils.isEmpty(recipe.getImage())) {
            Picasso.get()
                    .load(R.drawable.noimage)
                    .into(recipeImageView);
        } else {
            Picasso.get()
                    .load(recipe.getImage())
                    .error(R.drawable.noimage)
                    .into(recipeImageView);
        }
        recipeNameTextView.setText(recipe.getTitle());
        String cookTime = recipe.getTime() + " min";
        recipeTimeToCookTextView.setText(cookTime);

        recipeLinearLayout.setOnClickListener(v -> listener.setupOnItemClick(recipe.getId()));
    }
}
