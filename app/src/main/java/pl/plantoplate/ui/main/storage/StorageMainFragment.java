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

package pl.plantoplate.ui.main.storage;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentStorageInsideBinding;
import pl.plantoplate.repository.remote.models.Product;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.shoppingList.ShoppingListRepository;
import pl.plantoplate.ui.main.shoppingList.listAdapters.SetupItemButtons;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.CategoryAdapter;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.ProductsDbaseFragment;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.popups.ModifyProductpopUp;

/**
 * This fragment is responsible for displaying the storage.
 */
public class StorageMainFragment extends Fragment {

    private FragmentStorageInsideBinding fragmentStorageInsideBinding;
    private StorageViewModel storageViewModel;

    private TextView storage_title;
    private FloatingActionButton plus_in_storage;
    private RecyclerView recyclerView;

    private SharedPreferences prefs;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        fragmentStorageInsideBinding = FragmentStorageInsideBinding.inflate(inflater, container, false);

        // Get views
        plus_in_storage = fragmentStorageInsideBinding.plusInStorage;
        recyclerView = fragmentStorageInsideBinding.productsStorage;
        storage_title = fragmentStorageInsideBinding.textView4;

        // Set up click listeners
        plus_in_storage.setOnClickListener(this::onPlusClicked);

        // Set up recycler view
        setUpRecyclerView();
        // Set up view model
        setUpViewModel();

        // Get shared preferences
        prefs = requireActivity().getSharedPreferences("prefs", 0);
        return fragmentStorageInsideBinding.getRoot();
    }

    public void onPlusClicked(View view) {
        ShoppingListRepository shoppingListRepository = new ShoppingListRepository();
        String token = "Bearer " + prefs.getString("token", "");
        shoppingListRepository.getBoughtProductsIds(token, new ResponseCallback<ArrayList<Integer>>() {
            @Override
            public void onSuccess(ArrayList<Integer> productsIds) {
                if (productsIds.isEmpty()){
                    replaceFragment(new ProductsDbaseFragment("storage"));
                }
                else{
                   showaddFromPopUp(productsIds);
                }
            }

            @Override
            public void onError(String errorMessage) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(String failureMessage) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), failureMessage, Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    public void showaddFromPopUp(ArrayList<Integer> productsIds) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.new_pop_up_add_products_from_storage_to_storage);

        TextView add_from_shopping_list = dialog.findViewById(R.id.button_yes);
        TextView go_to_products_database = dialog.findViewById(R.id.button_no);

        add_from_shopping_list.setOnClickListener(v -> {
            storageViewModel.moveProductsToStorage(productsIds);
            dialog.dismiss();
        });

        go_to_products_database.setOnClickListener(v -> {
            replaceFragment(new ProductsDbaseFragment("storage"));
            dialog.dismiss();
        });

        dialog.show();
    }

    public void showModifyProductPopup(Product product) {
        ModifyProductpopUp modifyProductPopUp = new ModifyProductpopUp(requireContext(), product);
        modifyProductPopUp.acceptButton.setOnClickListener(v -> {
            String quantityValue = Objects.requireNonNull(modifyProductPopUp.quantity.getText()).toString();
            if (quantityValue.isEmpty()) {
                modifyProductPopUp.quantity.setError("Podaj ilość");
                return;
            }
            if (quantityValue.endsWith(".")) {
                // Remove dot at the end
                quantityValue = quantityValue.substring(0, quantityValue.length() - 1);
            }
            float quantity = BigDecimal.valueOf(Float.parseFloat(quantityValue)).setScale(3, RoundingMode.HALF_UP).floatValue();
            product.setAmount(quantity);
            storageViewModel.changeProductAmount(product);
            modifyProductPopUp.dismiss();
        });
        modifyProductPopUp.show();
    }


    public void showDeleteProductPopup(Product product) {
        Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        //add delay!!!!
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dialog.setContentView(R.layout.new_pop_up_delete_from_storage);

        TextView acceptButton = dialog.findViewById(R.id.button_yes);
        TextView cancelButton = dialog.findViewById(R.id.button_no);

        acceptButton.setOnClickListener(v -> dialog.dismiss());
        acceptButton.setOnClickListener(v -> {
            storageViewModel.deleteProductFromStorage(product);
           dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public void setUpViewModel() {
        // get storage view model
        storageViewModel = new ViewModelProvider(this).get(StorageViewModel.class);

        // get storage title
        storageViewModel.getStorageTitle().observe(getViewLifecycleOwner(), storageTitle -> storage_title.setText(storageTitle));

        // get success message
        storageViewModel.getSuccess().observe(getViewLifecycleOwner(), successMessage -> {
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), successMessage, Toast.LENGTH_SHORT).show());
            }
        });

        // get error message
        storageViewModel.getError().observe(getViewLifecycleOwner(), errorMessage -> {
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show());
            }
        });

        // get storage
        storageViewModel.getStorageProducts().observe(getViewLifecycleOwner(), storageProducts -> {

            // update recycler view
            CategoryAdapter categoryAdapter = (CategoryAdapter) recyclerView.getAdapter();
            Objects.requireNonNull(categoryAdapter).setCategoriesList(storageProducts);
        });

    }

    public void setUpRecyclerView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CategoryAdapter categoryAdapter = new CategoryAdapter(new ArrayList<>(), R.layout.item_spizarnia, R.layout.item_category_spizarnia);
        categoryAdapter.setUpItemButtons(new SetupItemButtons() {
            @Override
            public void setupAddToShoppingListButtonClick(View v, Product product) {
                v.setOnClickListener(view -> {
                });
            }

            @Override
            public void setupProductItemClick(View v, Product product) {
                v.setOnClickListener(view -> showModifyProductPopup(product));
            }

            //delete product from storage
            @Override
            public void setupDeleteProductButtonClick(View v, Product product) {
                String role = prefs.getString("role", "");
                if(role.equals("ROLE_ADMIN")) {
                    v.setOnClickListener(view -> showDeleteProductPopup(product));
                }
                else{
                    //set visibility none
                    v.setVisibility(View.INVISIBLE);
                }
            }
        });
        recyclerView.setAdapter(categoryAdapter);
    }

    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutStorage, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
