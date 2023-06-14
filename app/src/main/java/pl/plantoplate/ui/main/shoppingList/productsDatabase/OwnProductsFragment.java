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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentWlasneBinding;
import pl.plantoplate.repository.remote.models.Product;
import pl.plantoplate.ui.main.shoppingList.listAdapters.SetupItemButtons;
import pl.plantoplate.tools.CategorySorter;
import pl.plantoplate.ui.main.shoppingList.listAdapters.product.ProductAdapter;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.popups.ModifyProductpopUp;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.viewModels.ProductsDbaseViewModel;

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

    private ProductsDbaseViewModel productsDbaseViewModel;

    public OwnProductsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productsDbaseViewModel = new ViewModelProvider(requireParentFragment()).get(ProductsDbaseViewModel.class);
    }

    public OwnProductsFragment(String comesFrom) {

        Bundle bundle = new Bundle();
        bundle.putString("comesFrom", comesFrom);
        this.setArguments(bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        productsDbaseViewModel.fetchUserInfo();
        productsDbaseViewModel.fetchOwnProducts();
        if (searchView != null){
            searchView.setOnQueryTextListener(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (searchView != null){
            searchView.clearFocus();
            searchView.setQuery("", false);
            searchView.setOnQueryTextListener(null);
        }
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

        setUpViewModel();
        setupRecyclerView();
        return fragmentWlasneBinding.getRoot();
    }

    public void addProduct(Product product){
        String comesFrom = requireArguments().getString("comesFrom");

        switch (comesFrom) {
            case "shoppingList":
                productsDbaseViewModel.addProductToShoppingList(product, "own");
                break;
            case "storage":
                productsDbaseViewModel.addProductToStorage(product, "own");
                break;
            default:
                productsDbaseViewModel.addProductToShoppingList(product, "own");
                break;
        }
    }

    public void showAddProductPopup(Product product) {
        product.setAmount(0);
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

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        ArrayList<Product> products = Objects.requireNonNull(productsDbaseViewModel.getOwnProducts().getValue());
        ArrayList<Product> filteredProducts = CategorySorter.filterProductsBySearch(products, query);
        updateRecyclerView(filteredProducts);
        return false;
    }

    public void setUpViewModel() {
        productsDbaseViewModel.getUserInfo().observe(getViewLifecycleOwner(), userInfo -> {
            // Set the onClickListeners for the buttons
            String role = prefs.getString("role", "");

            // Setup listeners
            if(role.equals("ROLE_ADMIN")) {
                floatingActionButton_wlasne.setOnClickListener(v -> replaceFragment(new AddYourOwnProductFragment(), "addOwn"));
            }else {
                floatingActionButton_wlasne.setVisibility(View.INVISIBLE);
                floatingActionButton_wlasne.setClickable(false);
            }
        });

        // get to buy products
        productsDbaseViewModel.getOwnProducts().observe(getViewLifecycleOwner(), this::updateRecyclerView);

        // get success message
        productsDbaseViewModel.getOwnProductsOnSuccessOperation().observe(getViewLifecycleOwner(), successMessage ->
                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), successMessage, Toast.LENGTH_SHORT).show()));

        // get error message
        productsDbaseViewModel.getOwnProductsOnErrorOperation().observe(getViewLifecycleOwner(), errorMessage ->
                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()));

    }

    private void updateRecyclerView(ArrayList<Product> products) {
        if (products.isEmpty()) {
            welcomeTextView.setVisibility(View.VISIBLE);
        } else {
            welcomeTextView.setVisibility(View.GONE);
        }
        ProductAdapter productAdapter = (ProductAdapter) ownProductsRecyclerView.getAdapter();
        Objects.requireNonNull(productAdapter).setProductsList(CategorySorter.sortProductsByName(products));
    }

    private void setupRecyclerView() {
        ownProductsRecyclerView = fragmentWlasneBinding.productsOwnRecyclerView;
        ownProductsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ProductAdapter productAllAdapter = new ProductAdapter(new ArrayList<>(), R.layout.item_wlasny_produkt);
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
                    v.setOnClickListener(view -> replaceFragment(new EditOwnProductFragment(product), "editOwn"));
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

    private void replaceFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }
}
