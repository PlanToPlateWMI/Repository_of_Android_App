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
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentProductChangeBinding;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.product.ProductRepository;
import pl.plantoplate.repository.remote.models.Product;
import pl.plantoplate.ui.main.ChangeCategoryOfProductFragment;

/**
 * This fragment is used to edit the product.
 */
public class EditOwnProductFragment extends Fragment implements ChangeCategoryListener{

    private FragmentProductChangeBinding fragmentProductChangeBinding;

    private SharedPreferences prefs;
    private SharedPreferences productPrefs;

    private RadioGroup choose_product_unit;
    private Button add_product_button;
    private Button change_kategory;
    private Button cancel_button;
    private Button delete_button;
    private TextInputEditText add_product_name;
    private TextView product_category;

    private Product product;
    private ProductRepository productRepository;

    public EditOwnProductFragment(Product product) {
        this.product = product;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set the product name
        add_product_name.setText(product.getName());

        // Set the product unit
        switch (product.getUnit()) {
            case "SZT":
                choose_product_unit.check(R.id.button_szt);
                break;

            case "L":
                choose_product_unit.check(R.id.button_l);
                break;

            case "KG":
                choose_product_unit.check(R.id.button_kg);
                break;

            case "GR":
                choose_product_unit.check(R.id.button_gr);
                break;
            case "ML":
                choose_product_unit.check(R.id.button_ml);
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        fragmentProductChangeBinding = FragmentProductChangeBinding.inflate(inflater, container, false);

        // Get the shared preferences
        prefs = requireActivity().getSharedPreferences("prefs", 0);
        productPrefs = requireActivity().getSharedPreferences("product", 0);

        // Get the radio group
        choose_product_unit = fragmentProductChangeBinding.toggleGroup;

        // Set the radio group listener
        choose_product_unit.setOnCheckedChangeListener((group, checkedId) -> setProductUnit(checkedId));

        // Set add product button
        add_product_button = fragmentProductChangeBinding.buttonZatwierdz;

        //Set cansel product button
        cancel_button = fragmentProductChangeBinding.buttonAnuluj;

        // Set the product name button
        add_product_name = fragmentProductChangeBinding.enterTheName;

        // Set the product category
        product_category = fragmentProductChangeBinding.kategoriaNapis;
        product_category.setText(product.getCategory());

        // Set the button listener
        add_product_button.setOnClickListener(v -> applyProductChange(requireActivity().findViewById(R.id.frame_layout)));
        cancel_button.setOnClickListener(v -> getParentFragmentManager().popBackStack());

        change_kategory = fragmentProductChangeBinding.zmienKategorie;

        change_kategory.setOnClickListener(v -> replaceFragment(new ChangeCategoryOfProductFragment(this)));

        delete_button = fragmentProductChangeBinding.buttonUsun;
        delete_button.setOnClickListener(v -> showConfirmDeleteProductPopUp(requireActivity().findViewById(R.id.frame_layout)));



        // Get the product repository
        productRepository = new ProductRepository();


        return fragmentProductChangeBinding.getRoot();
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

    public void saveProduct(View view){

        // Get the token
        String token = "Bearer " + prefs.getString("token", "");
        productRepository.modifyProduct(token, product.getId(), product, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> response) {
                Snackbar.make(view, "Pomyślnie zmieniono produkt.", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(String errorMessage) {
                Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String failureMessage) {
                Snackbar.make(view, failureMessage, Snackbar.LENGTH_LONG).show();
            }
        });

        // Go back to the products database fragment
        requireActivity().getSupportFragmentManager().popBackStack();
    }

    public void applyProductChange(View view) {

        // Get the product name
        String product_name = Objects.requireNonNull(add_product_name.getText()).toString();

        // Set the product name
        product.setName(product_name);
        //String category = productPrefs.getString("category", "");
//        if (category.isEmpty()) {
//            category = product.getCategory();
//        }
        //product.setCategory(category);

        // Save the product
        saveProduct(view);
    }

    public void showConfirmDeleteProductPopUp(View view) {
        //add delay!!!!
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.new_pop_up_delete_product_from_database);

        Button acceptButton = dialog.findViewById(R.id.button_yes);
        Button cancelButton = dialog.findViewById(R.id.button_no);

        acceptButton.setOnClickListener(v -> {
            deleteProduct(view);
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void deleteProduct(View view) {

        // Get the token
        String token = "Bearer " + prefs.getString("token", "");
        productRepository.deleteProduct(token, product.getId(), new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> response) {
                Snackbar.make(view, "Pomyślnie usunięto produkt.", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(String errorMessage) {
                Snackbar.make(view, errorMessage, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String failureMessage) {
                Snackbar.make(view, failureMessage, Snackbar.LENGTH_LONG).show();
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

    @Override
    public void onCategoryChosen(String category) {
        product.setCategory(category);
        //product_category.setText(category);
    }
}
