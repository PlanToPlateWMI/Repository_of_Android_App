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
package pl.plantoplate.ui.main.recipes.recipe_info.recycler_views.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import pl.plantoplate.R;
import pl.plantoplate.ui.main.recipes.recipe_info.recycler_views.view_holders.RecipeStepsViewHolder;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsViewHolder> {

    private List<String> steps;

    public RecipeStepsAdapter(List<String> steps) {
        this.steps = steps;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setStepsList(List<String> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeStepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_krok, parent, false);
        return new RecipeStepsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsViewHolder holder, int position) {
        String step = steps.get(position);
        holder.bind(step);
    }

    @Override
    public int getItemCount() {
        return steps == null ? 0 : steps.size();
    }
}

