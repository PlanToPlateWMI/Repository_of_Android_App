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

package pl.plantoplate.ui.main.shoppingList.listAdapters.product;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pl.plantoplate.repository.remote.models.Product;
import pl.plantoplate.ui.main.shoppingList.listAdapters.SetupItemButtons;

public class ProductAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    private ArrayList<Product> products;
    private int itemType;

    private SetupItemButtons listener;

    public ProductAdapter(ArrayList<Product> products, int itemType) {
        this.products = products;
        this.itemType = itemType;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setProductsList(ArrayList<Product> filterlist) {
        products = filterlist;
        notifyDataSetChanged();
    }

    public void setUpItemButtons(SetupItemButtons listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(this.itemType, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.bind(products.get(position), listener);
    }

    @Override
    public int getItemCount() {
        if(products == null)
            return 0;
        return products.size();
    }
}