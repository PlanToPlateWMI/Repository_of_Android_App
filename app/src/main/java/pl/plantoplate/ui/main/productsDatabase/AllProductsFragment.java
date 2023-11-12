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

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.databinding.FragmentWszystkieBinding;
import pl.plantoplate.ui.main.popUps.ModifyProductPopUp;
import pl.plantoplate.ui.main.productsDatabase.viewModels.AllProductsViewModel;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;
import pl.plantoplate.data.remote.models.product.ProductCategory;
import pl.plantoplate.ui.main.recyclerViews.adapters.CategoryAdapter;
import pl.plantoplate.tools.CategorySorter;

/**
 * This fragment is responsible for displaying all products in the database.
 */
public class AllProductsFragment extends Fragment implements SearchView.OnQueryTextListener {

    private FragmentWszystkieBinding fragmentWszystkieBinding;
    private SearchView productsSearchView;
    private AllProductsViewModel allProductsViewModel;
    private CategoryAdapter categoryAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentWszystkieBinding = FragmentWszystkieBinding.inflate(inflater, container, false);
        productsSearchView = requireActivity().findViewById(R.id.search);

        setUpViewModel();
        setUpRecyclerView();
        return fragmentWszystkieBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        allProductsViewModel.fetchAllProducts();
        productsSearchView.setOnQueryTextListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        productsSearchView.clearFocus();
        productsSearchView.setQuery("", false);
        productsSearchView.setOnQueryTextListener(null);
    }

    public void addProduct(Product product){
        if ("storage".equals(requireArguments().getString("comesFrom"))) {
            allProductsViewModel.addProductToStorage(product);
        } else {
            allProductsViewModel.addProductToShoppingList(product);
        }
    }

    public void showAddProductPopup(Product product) {
        product.setAmount(0);
        ModifyProductPopUp addToCartPopUp = new ModifyProductPopUp(requireContext(), product);
        addToCartPopUp.acceptButton.setOnClickListener(v -> {
            String quantityValue = Objects.requireNonNull(addToCartPopUp.quantity.getText()).toString();
            if (quantityValue.isEmpty()) {
                addToCartPopUp.quantity.setError("Podaj ilość");
                return;
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
        productsSearchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        Optional<ArrayList<ProductCategory>> products = Optional.ofNullable(allProductsViewModel.getAllProducts().getValue());
        ArrayList<Product> filteredProducts = CategorySorter.filterCategoriesBySearch(products.orElse(new ArrayList<>()), query);
        ArrayList<ProductCategory> filteredList = CategorySorter.sortCategoriesByProduct(filteredProducts);
        categoryAdapter.setCategoriesList(filteredList);
        return false;
    }

    public void setUpViewModel() {
        allProductsViewModel = new ViewModelProvider(requireParentFragment()).get(AllProductsViewModel.class);
        allProductsViewModel.getAllProducts().observe(getViewLifecycleOwner(), products ->
                categoryAdapter.setCategoriesList(products));
    }

    public void setUpRecyclerView() {
        RecyclerView categoryRecyclerView = fragmentWszystkieBinding.categoryRecyclerView;
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        categoryAdapter = new CategoryAdapter(new ArrayList<>(), R.layout.item_wszystkie_produkt, R.layout.item_category_baza);
        categoryAdapter.setUpItemButtons(new SetupItemButtons() {
            @Override
            public void setupAddToShoppingListButtonClick(View v, Product product) {
                v.setOnClickListener(view -> {
                    product.setAmount(1.0f);
                    addProduct(product);
                });
            }

            @Override
            public void setupProductItemClick(View v, Product product) {
                v.setOnClickListener(view -> showAddProductPopup(product));
            }
        });
        categoryRecyclerView.setAdapter(categoryAdapter);
    }
}