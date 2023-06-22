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

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentWszystkieBinding;
import pl.plantoplate.repository.remote.models.Product;
import pl.plantoplate.ui.main.shoppingList.listAdapters.SetupItemButtons;
import pl.plantoplate.repository.remote.models.Category;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.CategoryAdapter;
import pl.plantoplate.tools.CategorySorter;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.popups.ModifyProductpopUp;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.viewModels.ProductsDbaseViewModel;

/**
 * This fragment is responsible for displaying all products in the database.
 */
public class AllProductsFragment extends Fragment implements SearchView.OnQueryTextListener {

    private FragmentWszystkieBinding fragmentWszystkieBinding;

    private RecyclerView categoryRecyclerView;
    private TextView welcomeTextView;
    private SearchView searchView;

    private ProductsDbaseViewModel productsDbaseViewModel;

    public AllProductsFragment() {
    }

    public AllProductsFragment(String comesFrom) {

        Bundle bundle = new Bundle();
        bundle.putString("comesFrom", comesFrom);
        this.setArguments(bundle);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productsDbaseViewModel = new ViewModelProvider(requireParentFragment()).get(ProductsDbaseViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        productsDbaseViewModel.fetchUserInfo();
        productsDbaseViewModel.fetchAllProducts();
        if (searchView != null) {
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
        fragmentWszystkieBinding = FragmentWszystkieBinding.inflate(inflater, container, false);

        // get views
        welcomeTextView = fragmentWszystkieBinding.textView3;
        searchView = requireActivity().findViewById(R.id.search);

        // set up recycler view
        setUpViewModel();
        setUpRecyclerView();

        return fragmentWszystkieBinding.getRoot();
    }

    public void addProduct(Product product){
        String comesFrom = requireArguments().getString("comesFrom");

        switch (comesFrom) {
            case "shoppingList":
                productsDbaseViewModel.addProductToShoppingList(product, "all");
                break;
            case "storage":
                productsDbaseViewModel.addProductToStorage(product, "all");
                break;
            default:
                productsDbaseViewModel.addProductToShoppingList(product, "all");
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
        ArrayList<Category> products = CategorySorter.sortCategoriesByProduct(Objects.requireNonNull(productsDbaseViewModel.getAllProducts().getValue()));
        ArrayList<Product> filteredProducts = CategorySorter.filterCategoriesBySearch(products, query);
        ArrayList<Category> filteredList = CategorySorter.sortCategoriesByProduct(filteredProducts);
        updateRecyclerView(filteredList);
        return false;
    }

    public void setUpViewModel() {

        productsDbaseViewModel.getUserInfo().observe(getViewLifecycleOwner(), userInfo -> {
        });

        // get to buy products
        productsDbaseViewModel.getAllProducts().observe(getViewLifecycleOwner(), products -> {
            ArrayList<Category> categories = CategorySorter.sortCategoriesByProduct(products);
            updateRecyclerView(categories);
        });

//        // get success message
//        productsDbaseViewModel.getAllProductsOnSuccessOperation().observe(getViewLifecycleOwner(), successMessage ->
//                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), successMessage, Toast.LENGTH_SHORT).show()));
//
//        // get error message
//        productsDbaseViewModel.getAllProductsOnErrorOperation().observe(getViewLifecycleOwner(), errorMessage ->
//                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()));

    }

    private void updateRecyclerView(ArrayList<Category> products) {
        if (products.isEmpty()) {
            welcomeTextView.setVisibility(View.VISIBLE);
        } else {
            welcomeTextView.setVisibility(View.GONE);
        }
        CategoryAdapter categoryAdapter = (CategoryAdapter) categoryRecyclerView.getAdapter();
        Objects.requireNonNull(categoryAdapter).setCategoriesList(products);
    }

    public void setUpRecyclerView() {
        categoryRecyclerView = fragmentWszystkieBinding.categoryRecyclerView;
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CategoryAdapter categoryAdapter = new CategoryAdapter(new ArrayList<>(), R.layout.item_wszystkie_produkt, R.layout.item_category_baza);
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
