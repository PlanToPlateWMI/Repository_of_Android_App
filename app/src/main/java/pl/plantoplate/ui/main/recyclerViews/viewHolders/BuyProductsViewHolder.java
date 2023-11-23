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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;

public class BuyProductsViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private final TextView unit;
    private final ImageView checkShoppingListButton;
    private final ImageView deleteProductButton;
    private final LinearLayout layout;

    public BuyProductsViewHolder(View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.nazwaProduktu_trzebaKupic);
        unit = itemView.findViewById(R.id.jednostkiMiary_trzebaKupic);
        checkShoppingListButton = itemView.findViewById(R.id.iconCheckbox_trzebaKupic);
        deleteProductButton = itemView.findViewById(R.id.iconDelete_trzebaKupic);
        layout = itemView.findViewById(R.id.layoutTrzebaKupicProdukty);
    }

    public void bind(Product product, SetupItemButtons listener) {
        name.setText(product.getName());
        float amount = BigDecimal.valueOf(Float.parseFloat(String.valueOf(product.getAmount())))
                .setScale(3, RoundingMode.HALF_UP)
                .floatValue();
        String unitText = amount + " " + product.getUnit().toLowerCase();
        unit.setText(unitText);
        listener.setupCheckShoppingListButtonClick(checkShoppingListButton, product);
        listener.setupDeleteProductButtonClick(deleteProductButton, product);
        listener.setupProductItemClick(layout, product);
    }
}