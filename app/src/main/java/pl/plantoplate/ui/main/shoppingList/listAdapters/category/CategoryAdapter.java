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

package pl.plantoplate.ui.main.shoppingList.listAdapters.category;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pl.plantoplate.R;
import pl.plantoplate.ui.main.shoppingList.listAdapters.SetupItemButtons;
import pl.plantoplate.repository.remote.models.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryViewHolder> {
    private ArrayList<Category> categories;
    private int productItemType;
    private int categoryItemType;
    private SetupItemButtons listener;

    public CategoryAdapter(ArrayList<Category> categories, int productItemType, int categoryItemType) {
        this.categories = categories;
        this.productItemType = productItemType;
        this.categoryItemType = categoryItemType;
    }

    public void setUpItemButtons(SetupItemButtons listener) {
        this.listener = listener;
    }

    // method for filtering our recyclerview items.
    @SuppressLint("NotifyDataSetChanged")
    public void setCategoriesList(ArrayList<Category> filterlist) {

        categories = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(this.categoryItemType, parent, false);
        return new CategoryViewHolder(itemView, this.productItemType);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(categories.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
