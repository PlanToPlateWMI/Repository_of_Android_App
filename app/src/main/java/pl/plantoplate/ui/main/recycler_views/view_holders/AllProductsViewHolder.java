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
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.ui.main.recycler_views.listeners.SetupItemButtons;

/**
 * This class is responsible for setting up the products in the recycler view.
 */
public class AllProductsViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private final TextView unit;
    private final AppCompatImageView addToShoppingListButton;
    private final LinearLayout layout;

    public AllProductsViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.nazwaProduktuWszystkie);
        unit = itemView.findViewById(R.id.jednostkiMiaryWszsystkie);
        addToShoppingListButton = itemView.findViewById(R.id.addBazaWszystkie);
        layout = itemView.findViewById(R.id.layoutWszystkieProdukty);
    }

    public void bind(Product product, SetupItemButtons listener) {
        name.setText(product.getName());
        unit.setText(product.getUnit().toLowerCase());
        listener.setupAddToShoppingListButtonClick(addToShoppingListButton, product);
        listener.setupProductItemClick(layout, product);
    }
}
