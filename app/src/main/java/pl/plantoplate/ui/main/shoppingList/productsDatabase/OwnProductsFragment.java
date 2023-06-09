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

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentWlasneBinding;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.product.ProductRepository;
import pl.plantoplate.repository.remote.shoppingList.ShoppingListRepository;
import pl.plantoplate.repository.remote.models.Product;
import pl.plantoplate.repository.remote.storage.StorageRepository;
import pl.plantoplate.ui.main.shoppingList.listAdapters.SetupItemButtons;
import pl.plantoplate.tools.CategorySorter;
import pl.plantoplate.ui.main.shoppingList.listAdapters.product.ProductAdapter;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.popups.ModifyProductpopUp;

/**
 * This fragment is responsible for displaying the products that the user has added to the database.
 */
public class OwnProductsFragment extends Fragment implements SearchView.OnQueryTextListener {
    private FragmentWlasneBinding fragmentWlasneBinding;

    private FloatingActionButton floatingActionButton_wlasne;
    private RecyclerView ownProductsRecyclerView;
    private TextView welcomeTextView;
    private SearchView searchView;

    private SharedPreferences prefs;
    private ProductRepository productRepository;

    private ArrayList<Product> groupProductsList;


    public OwnProductsFragment() {
    }

    public OwnProductsFragment(String comesFrom) {

        Bundle bundle = new Bundle();
        bundle.putString("comesFrom", comesFrom);
        this.setArguments(bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        getProducts();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        fragmentWlasneBinding = FragmentWlasneBinding.inflate(inflater, container, false);

        // Setup views
        floatingActionButton_wlasne = fragmentWlasneBinding.floatingActionButtonWlasne;
        welcomeTextView = fragmentWlasneBinding.welcomeWlasne;
        searchView = requireActivity().findViewById(R.id.search);

        // Get the shared preferences
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        // Set the onClickListeners for the buttons
        String role = prefs.getString("role", "");

        // Setup listeners
        if(role.equals("ROLE_ADMIN")) {
            floatingActionButton_wlasne.setOnClickListener(v -> replaceFragment(new AddYourOwnProductFragment()));
        }else {
            floatingActionButton_wlasne.setVisibility(View.INVISIBLE);
            floatingActionButton_wlasne.setClickable(false);
        }
        searchView.setOnQueryTextListener(this);

        // Get product repository
        productRepository = new ProductRepository();

        setupRecyclerView();
        return fragmentWlasneBinding.getRoot();
    }

    private void getProducts(){
        String token = "Bearer " + prefs.getString("token", "");
        productRepository.getOwnProducts(token, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> products) {
                groupProductsList = CategorySorter.sortProductsByName(products);

                updateRecyclerView();
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

    public void addProduct(Product product){
        String comesFrom = "";
        if (getArguments() != null) {
            comesFrom = getArguments().getString("comesFrom");
        }
        else{
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            int backStackEntryCount = fragmentManager.getBackStackEntryCount();
            for (int i=backStackEntryCount - 1; i>0; i--) {
                FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(i);
                String name = entry.getName();
                if (Objects.equals(name, "shoppingList") || Objects.equals(name, "storage")) {
                    comesFrom = name;
                }
            }
        }
        switch (comesFrom) {
            case "shoppingList":
                addProductToShoppingList(product);
                break;
            case "storage":
                addProductToStorage(product);
                break;
            default:
                addProductToShoppingList(product);
                break;
        }
    }

    public void addProductToShoppingList(Product product) {
        String token = "Bearer " + prefs.getString("token", "");
        ShoppingListRepository shoppingListRepository = new ShoppingListRepository();
        shoppingListRepository.addProductToShopList(token, product, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> response) {
                String message = "Produkt " + product.getName() + " w ilości " + product.getAmount() + " " + product.getUnit() + " został dodany do listy zakupów";
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show());
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

    public void addProductToStorage(Product product){
        String token = "Bearer " + prefs.getString("token", "");
        StorageRepository storageRepository = new StorageRepository();
        storageRepository.addProductToStorage(token, product, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> response) {
                String message = "Produkt " + product.getName() + " w ilości " + product.getAmount() + " " + product.getUnit() + " został dodany do spiżarni";

                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show());
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        ArrayList<Product> filteredProducts = CategorySorter.filterProductsBySearch(groupProductsList, query);
        updateRecyclerView(filteredProducts);
        return false;
    }

    private void updateRecyclerView() {
        if (groupProductsList.isEmpty()) {
            welcomeTextView.setVisibility(View.VISIBLE);
        } else {
            welcomeTextView.setVisibility(View.GONE);
        }
        ProductAdapter productAdapter = (ProductAdapter) ownProductsRecyclerView.getAdapter();
        Objects.requireNonNull(productAdapter).setProductsList(this.groupProductsList);
    }

    private void updateRecyclerView(ArrayList<Product> filteredProducts) {
        ProductAdapter productAllAdapter = (ProductAdapter) ownProductsRecyclerView.getAdapter();
        Objects.requireNonNull(productAllAdapter).setProductsList(filteredProducts);
    }

    public void showAddProductPopup(Product product) {
        product.setAmount(1);
        ModifyProductpopUp addToCartPopUp = new ModifyProductpopUp(requireContext(), product);
        addToCartPopUp.acceptButton.setOnClickListener(v -> {
            String quantityValue = Objects.requireNonNull(addToCartPopUp.quantity.getText()).toString();
            if (quantityValue.isEmpty()) {
                addToCartPopUp.quantity.setError("Podaj ilość");
                return;
            }
            if (quantityValue.endsWith(".")) {
                // Remove dot at the end
                quantityValue = quantityValue.substring(0, quantityValue.length() - 1);
            }
            float quantity = BigDecimal.valueOf(Float.parseFloat(quantityValue)).setScale(3, RoundingMode.HALF_UP).floatValue();
            product.setAmount(quantity);
            addProduct(product);
            addToCartPopUp.dismiss();
        });
        addToCartPopUp.show();
    }

    private void setupRecyclerView() {
        ownProductsRecyclerView = fragmentWlasneBinding.productsOwnRecyclerView;
        ownProductsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ProductAdapter productAllAdapter = new ProductAdapter(groupProductsList, R.layout.item_wlasny_produkt);
        productAllAdapter.setUpItemButtons(new SetupItemButtons() {
            @Override
            public void setupAddToShoppingListButtonClick(View v, Product product) {
                v.setOnClickListener(view -> {
                    product.setAmount(1.0f);
                    addProduct(product);
                });
            }

            @Override
            public void setupEditProductButtonClick(View v, Product product) {
                //if admin
                String role = prefs.getString("role", "");
                if(role.equals("ROLE_ADMIN")) {
                    v.setOnClickListener(view -> replaceFragment(new EditOwnProductFragment(product)));
                }
                else{
                    //set visibility none
                    v.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void setupProductItemClick(View v, Product product) {
                v.setOnClickListener(view -> showAddProductPopup(product));
            }
        });
        ownProductsRecyclerView.setAdapter(productAllAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        productRepository.cancelCalls();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
