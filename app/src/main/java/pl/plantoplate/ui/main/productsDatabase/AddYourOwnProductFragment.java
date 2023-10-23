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

package pl.plantoplate.ui.main.productsDatabase;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.repository.ProductRepository;
import pl.plantoplate.data.remote.models.Product;
import pl.plantoplate.databinding.FragmentAddYourOwnProductBinding;
import pl.plantoplate.ui.main.ChangeCategoryOfProductFragment;

/**
 * This fragment is responsible for adding a new product to the database.
 */
public class AddYourOwnProductFragment extends Fragment implements ChangeCategoryListener{

    private FragmentAddYourOwnProductBinding add_own_product_view;

    private SharedPreferences prefs;
    private SharedPreferences productPrefs;

    private RadioGroup choose_product_unit;
    private Button add_product_button;
    private Button change_kategory;
    private TextInputEditText add_product_name;
    private TextView product_category;

    private Product product;
    private ProductRepository productRepository;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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

        // Set the product category
        product_category = add_own_product_view.kategoriaNapisac;

        // init the product and set the default category
        if (product == null) {
            product = new Product();
            product.setCategory("Produkty własne");
            product_category.setText(product.getCategory());
        }
        else{
            product_category.setText(product.getCategory());
        }

        // Set the radio group listener
        choose_product_unit.setOnCheckedChangeListener((group, checkedId) -> setProductUnit(checkedId));

        // Set add product button
        add_product_button = add_own_product_view.buttonZatwierdz;

        // Set the product name button
        add_product_name = add_own_product_view.enterTheName;

        // Set the button listener
        add_product_button.setOnClickListener(v -> addProduct());

        change_kategory = add_own_product_view.zmienKategorie;
        change_kategory.findViewById(R.id.zmien_kategorie);

        change_kategory.setOnClickListener(v -> replaceFragment(new ChangeCategoryOfProductFragment(this)));

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
            case R.id.button_ml:
                product.setUnit("ML");
                break;
        }
    }

    @Override
    public void onCategoryChosen(String category) {
        product.setCategory(category);
        product_category.setText(category);
    }

    public void addProduct() {
        // Set up product
        String product_name = Objects.requireNonNull(add_product_name.getText()).toString();
        product.setName(product_name);

        // Get the token
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = productRepository.addProduct(token, product)
                .subscribe(
                        response -> {
                            Toast.makeText(requireActivity().getApplicationContext(), "Produkt został dodany",
                                    Toast.LENGTH_SHORT).show();

                            // Go back to the products database fragment
                            requireActivity().getSupportFragmentManager().popBackStack("addOwn", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        },
                        error -> Toast.makeText(requireActivity(), error.getMessage(), Toast.LENGTH_SHORT).show()
                );
    }


    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}