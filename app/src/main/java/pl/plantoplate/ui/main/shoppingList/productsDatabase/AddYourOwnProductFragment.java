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

package pl.plantoplate.ui.main.shoppingList.productsDatabase;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentAddYourOwnProductBinding;
import pl.plantoplate.requests.products.Product;

public class AddYourOwnProductFragment extends Fragment {

    private FragmentAddYourOwnProductBinding add_own_product_view;

    private RadioGroup choose_product_unit;
    private Button add_product_button;

    private Product product;

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        add_own_product_view = FragmentAddYourOwnProductBinding.inflate(inflater, container, false);

        // Get the radio group
        choose_product_unit = add_own_product_view.toggleGroup;

        // init the product and set the default category
        product = new Product();
        product.setCategory("Dodane przeze mnie");

        // Set the radio group listener
        choose_product_unit.setOnCheckedChangeListener((group, checkedId) -> setProductUnit(checkedId));

        // Set add product button
        add_product_button = add_own_product_view.buttonZatwierdz;

        // Set the button listener
        add_product_button.setOnClickListener(this::addProduct);


        return add_own_product_view.getRoot();
    }

    @SuppressLint("NonConstantResourceId")
    public void setProductUnit(int checkedId) {
        switch (checkedId) {
            case R.id.button_szt:
                product.setUnit("SZT");
                break;

            case R.id.button_l:
                product.setUnit("L");
                break;

            case R.id.button_kg:
                product.setUnit("KG");
                break;

            case R.id.button_gr:
                product.setUnit("GR");
                break;
        }
    }

    public void addProduct(View v) {

    }

}