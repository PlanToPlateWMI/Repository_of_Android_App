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
package pl.plantoplate.ui.main.recyclerViews.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;
import pl.plantoplate.ui.main.recyclerViews.viewHolders.AllProductsViewHolder;
import pl.plantoplate.ui.main.recyclerViews.viewHolders.BoughtProductsViewHolder;
import pl.plantoplate.ui.main.recyclerViews.viewHolders.BuyProductsViewHolder;
import pl.plantoplate.ui.main.recyclerViews.viewHolders.OwnProductsViewHolder;
import pl.plantoplate.ui.main.recyclerViews.viewHolders.PantryProductsViewHolder;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Product> products;
    private final int itemType;

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

    @Override
    public int getItemViewType(int position) {
        return itemType;
    }

    public void setUpItemButtons(SetupItemButtons listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(itemType, parent, false);
        int itemId = itemView.getId();
        if (itemId == R.id.layoutWszystkieProdukty) {
            return new AllProductsViewHolder(itemView);
        } else if (itemId == R.id.layoutWlasneProdukty) {
            return new OwnProductsViewHolder(itemView);
        } else if (itemId == R.id.layoutTrzebaKupicProdukty) {
            return new BuyProductsViewHolder(itemView);
        } else if (itemId == R.id.layoutKupioneProdukty) {
            return new BoughtProductsViewHolder(itemView);
        } else if (itemId == R.id.item_produkt_spizarnia) {
            return new PantryProductsViewHolder(itemView);
        }
        throw new RuntimeException("There is no type that matches the type "
                                    + itemType +
                                   " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Product product = products.get(position);
        if (holder instanceof AllProductsViewHolder) {
            ((AllProductsViewHolder) holder).bind(product, listener);
        }else if (holder instanceof OwnProductsViewHolder) {
            ((OwnProductsViewHolder) holder).bind(product, listener);
        }else if (holder instanceof BuyProductsViewHolder) {
            ((BuyProductsViewHolder) holder).bind(product, listener);
        }else if (holder instanceof BoughtProductsViewHolder) {
            ((BoughtProductsViewHolder) holder).bind(product, listener);
        }else if (holder instanceof PantryProductsViewHolder) {
            ((PantryProductsViewHolder) holder).bind(product, listener);
        }
    }

    @Override
    public int getItemCount() {
        if(products == null)
            return 0;
        return products.size();
    }
}