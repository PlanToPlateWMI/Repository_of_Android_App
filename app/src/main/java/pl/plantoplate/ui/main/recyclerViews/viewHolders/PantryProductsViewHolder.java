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
package pl.plantoplate.ui.main.recyclerViews.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.Product;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;

public class PantryProductsViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private final TextView unit;
    private final ImageView deleteProductButton;
    private final LinearLayout layout;

    public PantryProductsViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.nazwaProduktu_spizarnia);
        unit = itemView.findViewById(R.id.jednostkiMiary_spizarnia);
        deleteProductButton = itemView.findViewById(R.id.iconDelete_spizarnia);
        layout = itemView.findViewById(R.id.item_produkt_spizarnia);
    }

    public void bind(Product product, SetupItemButtons listener) {
        name.setText(product.getName());
        String unitText = product.getAmount() + " " + product.getUnit().toLowerCase();
        unit.setText(unitText);
        listener.setupProductItemClick(layout, product);
        listener.setupDeleteProductButtonClick(deleteProductButton, product);
    }
}