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
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentAddYourOwnProductBinding;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.product.ProductRepository;
import pl.plantoplate.repository.models.Product;
import pl.plantoplate.ui.main.ChangeCategoryOfProductFragment;

public class AddYourOwnProductFragment extends Fragment implements ChangeCategoryListener{

    private FragmentAddYourOwnProductBinding add_own_product_view;

    private SharedPreferences prefs;
    private SharedPreferences productPrefs;

    private RadioGroup choose_product_unit;
    private Button add_product_button;
    private Button change_kategory;
    private Button cancel_button;
    private TextInputEditText add_product_name;
    private TextView product_category;

    private Product product;
    private ProductRepository productRepository;

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        add_own_product_view = FragmentAddYourOwnProductBinding.inflate(inflater, container, false);

        // Get the shared preferences
        prefs = requireActivity().getSharedPreferences("prefs", 0);
        productPrefs = requireActivity().getSharedPreferences("product", 0);

        // Get the radio group
        choose_product_unit = add_own_product_view.toggleGroup;

        // init the product and set the default category
        product = new Product();
        product.setCategory("Produkty własne");

        // Set the radio group listener
        choose_product_unit.setOnCheckedChangeListener((group, checkedId) -> setProductUnit(checkedId));

        // Set add product button
        add_product_button = add_own_product_view.buttonZatwierdz;

        //Set cansel product button
        cancel_button = add_own_product_view.buttonAnuluj;

        // Set the product name button
        add_product_name = add_own_product_view.enterTheName;

        // Set the product category
        product_category = add_own_product_view.kategoria;

        // Set the button listener
        add_product_button.setOnClickListener(v -> addProduct(requireActivity().findViewById(R.id.frame_layout)));
        cancel_button.setOnClickListener(v -> replaceFragment(new ProductsDbaseFragment()));

        change_kategory = add_own_product_view.zmienKategorie;
        change_kategory.findViewById(R.id.zmien_kategorie);

        change_kategory.setOnClickListener(v -> replaceFragment(new ChangeCategoryOfProductFragment()));

        // get the product repository
        productRepository = new ProductRepository();

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

    @Override
    public void onCategoryChosen(String category) {
        product.setCategory(category);
        product_category.setText(category);
    }

    public void addProduct(View v) {
        String category = productPrefs.getString("category", "");
        if (!category.isEmpty()) {
            product.setCategory(category);
        }

        // Set up product
        String product_name = Objects.requireNonNull(add_product_name.getText()).toString();
        product.setName(product_name);

        // Get the token
        String token = "Bearer " + prefs.getString("token", "");
        productRepository.addProduct(token, product, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> response) {
                Snackbar.make(v, "Produkt został dodany", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(String errorMessage) {
                Snackbar.make(v, errorMessage, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String failureMessage) {
                Snackbar.make(v, failureMessage, Snackbar.LENGTH_LONG).show();
            }
        });

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