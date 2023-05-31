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
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import pl.plantoplate.R;
import pl.plantoplate.repository.remote.models.Product;
import pl.plantoplate.ui.main.shoppingList.listAdapters.SetupItemButtons;

public class ProductViewHolder extends RecyclerView.ViewHolder{
    private int itemType;
    private TextView name;
    private TextView unit;

    private AppCompatImageView addToShoppingListButton;
    private ImageView deleteProductButton;
    private ImageView editProductButton;
    private ImageView checkShoppingListButton;

    private LinearLayout layout;

    @SuppressLint("NonConstantResourceId")
    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemType = itemView.getId();
        switch (this.itemType) {
            case R.id.layoutWszystkieProdukty:
                name = itemView.findViewById(R.id.nazwaProduktuWszystkie);
                unit = itemView.findViewById(R.id.jednostkiMiaryWszsystkie);
                addToShoppingListButton = itemView.findViewById(R.id.addBazaWszystkie);
                layout = itemView.findViewById(R.id.layoutWszystkieProdukty);
                break;
            case R.id.layoutWlasneProdukty:
                name = itemView.findViewById(R.id.nazwaProduktu_wlasny);
                unit = itemView.findViewById(R.id.jednostkiMiary_wlasny);
                addToShoppingListButton = itemView.findViewById(R.id.addBazaWlasne);
                editProductButton = itemView.findViewById(R.id.iconEdit_wlasny);
                layout = itemView.findViewById(R.id.layoutWlasneProdukty);
                break;
            case R.id.layoutTrzebaKupicProdukty:
                System.out.println("layoutTrzebaKupicProdukty");
                name = itemView.findViewById(R.id.nazwaProduktu_trzebaKupic);
                unit = itemView.findViewById(R.id.jednostkiMiary_trzebaKupic);
                checkShoppingListButton = itemView.findViewById(R.id.iconCheckbox_trzebaKupic);
                deleteProductButton = itemView.findViewById(R.id.iconDelete_trzebaKupic);
                layout = itemView.findViewById(R.id.layoutTrzebaKupicProdukty);
                break;
            case R.id.layoutKupioneProdukty:
                name = itemView.findViewById(R.id.nazwaProduktu_kupione);
                unit = itemView.findViewById(R.id.jednostkiMiary_kupione);
                checkShoppingListButton = itemView.findViewById(R.id.iconUncheck_kupione);
                deleteProductButton = itemView.findViewById(R.id.iconDelete_kupione);
                break;
            case R.id.item_produkt_spizarnia:
                name = itemView.findViewById(R.id.nazwaProduktu_spizarnia);
                unit = itemView.findViewById(R.id.jednostkiMiary_spizarnia);
                deleteProductButton = itemView.findViewById(R.id.iconDelete_spizarnia);
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void bind(Product product, SetupItemButtons listener) {
        name.setText(product.getName());
        String unitText = product.getAmount() + " " + product.getUnit();
        unit.setText(product.getUnit());
        switch(this.itemType) {
            case R.id.layoutWszystkieProdukty:
                listener.setupAddToShoppingListButtonClick(addToShoppingListButton, product);
                listener.setupProductItemClick(layout, product);
                //addToShoppingListButton.setOnClickListener(v-> listener.onAddToShoppingListButtonClick(v, product));
                //layout.setOnClickListener(v -> listener.onProductItemClick(v, product));
                break;
            case R.id.layoutWlasneProdukty:
                listener.setupAddToShoppingListButtonClick(addToShoppingListButton, product);
                listener.setupEditProductButtonClick(editProductButton, product);
                listener.setupProductItemClick(layout, product);
                //addToShoppingListButton.setOnClickListener(v -> listener.onAddToShoppingListButtonClick(v, product));
                //
                //editProductButton.setOnClickListener(v -> listener.onEditProductButtonClick(v, product));
                //
                //layout.setOnClickListener(v -> listener.onProductItemClick(v, product));
                break;
            case R.id.layoutTrzebaKupicProdukty:
                listener.setupCheckShoppingListButtonClick(checkShoppingListButton, product);
                listener.setupDeleteProductButtonClick(deleteProductButton, product);
                listener.setupProductItemClick(layout, product);
                //checkShoppingListButton.setOnClickListener(v -> listener.onCheckShoppingListButtonClick(v, product));
                //
                //deleteProductButton.setOnClickListener(v -> listener.onDeleteProductButtonClick(v, product));
                //
                //layout.setOnClickListener(v -> listener.onProductItemClick(v, product));
                unit.setText(unitText);
            case R.id.layoutKupioneProdukty:
                listener.setupCheckShoppingListButtonClick(checkShoppingListButton, product);
                listener.setupDeleteProductButtonClick(deleteProductButton, product);
                //checkShoppingListButton.setOnClickListener(v -> listener.onCheckShoppingListButtonClick(v, product));
                //
                //deleteProductButton.setOnClickListener(v -> listener.onDeleteProductButtonClick(v, product));
                //
                unit.setText(unitText);
                break;
            case R.id.item_produkt_spizarnia:
                //
                listener.setupDeleteProductButtonClick(deleteProductButton, product);
                //deleteProductButton.setOnClickListener(v -> listener.onDeleteProductButtonClick(v, product));
                //
                unit.setText(unitText);
                break;
        }
    }
}
