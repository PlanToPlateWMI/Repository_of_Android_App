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
import android.widget.ProgressBar;
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

import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.Product;
import pl.plantoplate.data.remote.repository.ShoppingListRepository;
import pl.plantoplate.databinding.FragmentStorageInsideBinding;
import pl.plantoplate.ui.main.productsDatabase.popups.DeleteProductPopUp;
import pl.plantoplate.ui.main.shoppingList.listAdapters.SetupItemButtons;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.CategoryAdapter;
import pl.plantoplate.ui.main.productsDatabase.ProductsDbaseFragment;
import pl.plantoplate.ui.main.productsDatabase.popups.ModifyProductPopUp;
import timber.log.Timber;

/**
 * This fragment is responsible for displaying the storage.
 */
public class StorageMainFragment extends Fragment {

    private FragmentStorageInsideBinding fragmentStorageInsideBinding;
    private StorageViewModel storageViewModel;
    private FloatingActionButton plus_in_storage;
    private RecyclerView recyclerView;
    //private ProgressBar progressBar;

    private SharedPreferences prefs;

    @Override
    public void onResume() {
        super.onResume();
        storageViewModel.fetchUserInfo();
        storageViewModel.fetchStorageProducts();
        //progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * Called when the fragment should create its view hierarchy.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Timber.d("onCreate() called");

        // Inflate the layout for this fragment
        fragmentStorageInsideBinding = FragmentStorageInsideBinding.inflate(inflater, container, false);
        //progressBar = requireActivity().findViewById(R.id.progressBar);
        //progressBar.setVisibility(View.VISIBLE);

        // Get views
        plus_in_storage = fragmentStorageInsideBinding.plusInStorage;
        recyclerView = fragmentStorageInsideBinding.productsStorage;

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

    /**
     * Called when the "plus" button is clicked.
     *
     * @param view The view that was clicked.
     */
    public void onPlusClicked(View view) {
        ShoppingListRepository shoppingListRepository = new ShoppingListRepository();
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = shoppingListRepository.getBoughtProductsIds(token)
                .subscribe(productsIds -> {
                    if (productsIds.isEmpty()) {
                        Bundle args = new Bundle();
                        args.putString("comesFrom", "storage");
                        Fragment fragment = new ProductsDbaseFragment();
                        fragment.setArguments(args);
                        replaceFragment(fragment);
                    } else {
                        showaddFromPopUp(productsIds);
                    }
                }, throwable -> {
                    Toast.makeText(requireActivity(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    /**
     * Shows a custom pop-up dialog for adding products from storage to storage.
     *
     * @param productsIds The list of product IDs to add.
     */
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
            Bundle args = new Bundle();
            args.putString("comesFrom", "storage");
            Fragment fragment = new ProductsDbaseFragment();
            fragment.setArguments(args);
            replaceFragment(fragment);
            dialog.dismiss();
        });

        dialog.show();
    }

    /**
     * Shows a pop-up dialog for modifying a product.
     *
     * @param product The product to be modified.
     */
    public void showModifyProductPopup(Product product) {
        ModifyProductPopUp modifyProductPopUp = new ModifyProductPopUp(requireContext(), product);
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

    /**
     * Set up the view model for the storage.
     * This method initializes the storage view model, observes different data from the view model,
     * and updates the UI accordingly.
     */
    public void setUpViewModel() {
        // get storage view model
        storageViewModel = new ViewModelProvider(this).get(StorageViewModel.class);

        storageViewModel.getUserInfo().observe(getViewLifecycleOwner(), userInfo -> {
        });

        // get storage
        storageViewModel.getStorageProducts().observe(getViewLifecycleOwner(), storageProducts -> {

            Timber.e("Storage products: %s", storageProducts);
            if (storageProducts.isEmpty()) {
                Timber.e("Storage is empty");
                fragmentStorageInsideBinding.textView4.setText(R.string.wprowadzenie_spizarnia);
            } else {
                fragmentStorageInsideBinding.textView4.setText("");
            }

            // update recycler view
            CategoryAdapter categoryAdapter = (CategoryAdapter) recyclerView.getAdapter();
            Objects.requireNonNull(categoryAdapter).setCategoriesList(storageProducts);
            //progressBar.setVisibility(View.INVISIBLE);
        });

        storageViewModel.getResponseMessage().observe(getViewLifecycleOwner(), responseMessage ->
                Toast.makeText(requireActivity(), responseMessage, Toast.LENGTH_SHORT).show());

    }

    /**
     * Set up the recycler view for displaying storage products.
     * This method configures the recycler view with a layout manager,
     * an adapter, and item button setups.
     */
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

            @Override
            public void setupDeleteProductButtonClick(View v, Product product) {
                String role = prefs.getString("role", "");
                if(role.equals("ROLE_ADMIN")) {
                    v.setOnClickListener(view -> new DeleteProductPopUp(requireContext(),
                                            view1 -> storageViewModel.deleteProductFromStorage(product)).show());
                }
                else{
                    //set visibility none
                    v.setVisibility(View.INVISIBLE);
                }
            }
        });
        recyclerView.setAdapter(categoryAdapter);
    }

    /**
     * Replaces the current fragment with the specified fragment.
     *
     * @param fragment The fragment to be replaced.
     */
    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutStorage, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
