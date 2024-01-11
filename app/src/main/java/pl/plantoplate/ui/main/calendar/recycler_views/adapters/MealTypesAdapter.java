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
package pl.plantoplate.ui.main.calendar.recycler_views.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import pl.plantoplate.R;
import pl.plantoplate.ui.main.calendar.recycler_views.listeners.SetupMealItem;
import pl.plantoplate.ui.main.calendar.recycler_views.models.MealTypes;
import pl.plantoplate.ui.main.calendar.recycler_views.view_holders.MealTypeViewHolder;

/**
 * Adapter class for displaying meal types in a RecyclerView.
 */
public class MealTypesAdapter extends RecyclerView.Adapter<MealTypeViewHolder>{

    private List<MealTypes> mealTypes;
    private SetupMealItem listener;

    public MealTypesAdapter() {
        this.mealTypes = new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMealTypes(List<MealTypes> mealTypes) {
        this.mealTypes = mealTypes;
        notifyDataSetChanged();
    }

    public void setUpMealItem(SetupMealItem listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public MealTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_kalendarz, parent, false);
        return new MealTypeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MealTypeViewHolder holder, int position) {
        MealTypes mealType = mealTypes.get(position);
        holder.bind(mealType, listener);
    }

    @Override
    public int getItemCount() {
        return mealTypes == null ? 0 : mealTypes.size();
    }
}