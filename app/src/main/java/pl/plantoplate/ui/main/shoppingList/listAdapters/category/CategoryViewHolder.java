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

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pl.plantoplate.R;
import pl.plantoplate.ui.main.shoppingList.listAdapters.OnProductItemClickListener;
import pl.plantoplate.ui.main.shoppingList.listAdapters.product.ProductAdapter;
import pl.plantoplate.repository.models.Category;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    private TextView categoryOfProduct;
    private RecyclerView productRecyclerView;
    private int itemType;

    public CategoryViewHolder(View itemView, int itemType) {
        super(itemView);
        this.itemType = itemType;
        categoryOfProduct = itemView.findViewById(R.id.сategory_of_product);
        productRecyclerView = itemView.findViewById(R.id.product_recycler_view);

    }

    public void bind(Category category, OnProductItemClickListener listener) {

        categoryOfProduct.setText(category.getName());
        productRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        ProductAdapter productAdapter = new ProductAdapter(category.getProducts(), this.itemType);
        productAdapter.setOnProductItemClickListener(listener);
        productRecyclerView.setAdapter(productAdapter);
    }
}