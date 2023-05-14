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
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentAddYourOwnProductBinding;
import pl.plantoplate.requests.products.Product;
import pl.plantoplate.ui.main.ChangeCategoryOfProductFragment;

public class AddYourOwnProductFragment extends Fragment {

    private FragmentAddYourOwnProductBinding add_own_product_view;

    private RadioGroup choose_product_unit;
    private Button add_product_button;
    private Button change_kategory;
    private TextInputEditText add_product_name;

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

        // Set the product name button
        add_product_name = add_own_product_view.enterTheName;

        // Set the button listener
        add_product_button.setOnClickListener(this::addProduct);

        change_kategory = add_own_product_view.zmienKategorie;
        change_kategory.findViewById(R.id.zmien_kategorie);

        change_kategory.setOnClickListener(v -> replaceFragment(new ChangeCategoryOfProductFragment()));


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
        // Get the product name
        String product_name = Objects.requireNonNull(add_product_name.getText()).toString();

        // Set the product name
        product.setName(product_name);

        //TODO: Add the product to the database using ApiClient

        // Go back to the products database fragment
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}