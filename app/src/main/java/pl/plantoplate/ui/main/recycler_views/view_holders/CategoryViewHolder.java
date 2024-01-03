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
package pl.plantoplate.ui.main.recycler_views.view_holders;

import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.product.ProductCategory;
import pl.plantoplate.ui.main.recycler_views.adapters.ProductAdapter;
import pl.plantoplate.ui.main.recycler_views.listeners.SetupItemButtons;

public class CategoryViewHolder extends RecyclerView.ViewHolder {
    private final int itemType;
    private final TextView categoryOfProduct;
    private final RecyclerView productRecyclerView;

    public CategoryViewHolder(View itemView, int itemType) {
        super(itemView);
        this.itemType = itemType;
        categoryOfProduct = itemView.findViewById(R.id.сategory_of_product);
        productRecyclerView = itemView.findViewById(R.id.product_recycler_view);

    }

    public void bind(ProductCategory category, SetupItemButtons listener) {
        categoryOfProduct.setText(category.getName());
        productRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext()));
        ProductAdapter productAdapter = new ProductAdapter(category.getProducts(), this.itemType);
        productAdapter.setUpItemButtons(listener);
        productRecyclerView.setAdapter(productAdapter);
    }
}