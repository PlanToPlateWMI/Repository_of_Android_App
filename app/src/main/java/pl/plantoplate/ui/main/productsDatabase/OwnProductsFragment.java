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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.databinding.FragmentWlasneBinding;
import pl.plantoplate.ui.main.popUps.ModifyProductPopUp;
import pl.plantoplate.ui.main.productsDatabase.viewModels.OwnProductsViewModel;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;
import pl.plantoplate.tools.CategorySorter;
import pl.plantoplate.ui.main.recyclerViews.adapters.ProductAdapter;
import timber.log.Timber;

/**
 * This fragment is responsible for displaying the products that the user has added to the database.
 */
public class OwnProductsFragment extends Fragment implements SearchView.OnQueryTextListener {

    private FragmentWlasneBinding fragmentWlasneBinding;
    private FloatingActionButton createNewProductButton;
    private TextView welcomeTextView;
    private SearchView productsSearchView;
    private SharedPreferences prefs;
    private OwnProductsViewModel ownProductsViewModel;
    private ProductAdapter productAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentWlasneBinding = FragmentWlasneBinding.inflate(inflater, container, false);
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        initViews(fragmentWlasneBinding);
        setUpViewModel();
        setupRecyclerView();
        return fragmentWlasneBinding.getRoot();
    }

    public void initViews(FragmentWlasneBinding fragmentWlasneBinding){
        createNewProductButton = fragmentWlasneBinding.floatingActionButtonWlasne;
        welcomeTextView = fragmentWlasneBinding.welcomeWlasne;
        productsSearchView = requireActivity().findViewById(R.id.search);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (productsSearchView != null){
            productsSearchView.clearFocus();
            productsSearchView.setQuery("", false);
            productsSearchView.setOnQueryTextListener(null);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ownProductsViewModel.fetchUserInfo();
        ownProductsViewModel.fetchOwnProducts();
        if (productsSearchView != null){
            productsSearchView.setOnQueryTextListener(this);
        }
    }

    public void addProduct(Product product){
        String comesFrom = requireArguments().getString("comesFrom");

        if ("storage".equals(comesFrom)) {
            ownProductsViewModel.addProductToStorage(product);
        } else {
            ownProductsViewModel.addProductToShoppingList(product);
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
            if (quantityValue.endsWith(".")) {
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
        productsSearchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        ArrayList<Product> products = Optional.ofNullable(ownProductsViewModel.getOwnProducts().getValue()).orElse(new ArrayList<>());
        ArrayList<Product> filteredProducts = CategorySorter.filterProductsBySearch(products, query);
        updateRecyclerView(filteredProducts);
        return false;
    }

    public void setUpViewModel() {
        ownProductsViewModel = new ViewModelProvider(requireParentFragment()).get(OwnProductsViewModel.class);
        ownProductsViewModel.getUserInfo().observe(getViewLifecycleOwner(), userInfo -> {
            String role = prefs.getString("role", "");
            if(role.equals("ROLE_ADMIN")) {
                createNewProductButton.setOnClickListener(v -> replaceFragment(new AddYourOwnProductFragment()));
            }else {
                createNewProductButton.setVisibility(View.INVISIBLE);
                createNewProductButton.setClickable(false);
            }
        });
        ownProductsViewModel.getOwnProducts().observe(getViewLifecycleOwner(), this::updateRecyclerView);

    }

    private void updateRecyclerView(ArrayList<Product> products) {
        if (products.isEmpty()) {
            welcomeTextView.setText(R.string.wprowadzenie_baza_wlasne);
        } else {
            welcomeTextView.setText("");
        }
        productAdapter.setProductsList(CategorySorter.sortProductsByName(products));
    }

    private void setupRecyclerView() {
        RecyclerView ownProductsRecyclerView = fragmentWlasneBinding.productsOwnRecyclerView;
        ownProductsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        productAdapter = new ProductAdapter(new ArrayList<>(), R.layout.item_wlasny_produkt);
        productAdapter.setUpItemButtons(new SetupItemButtons() {
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
                    v.setOnClickListener(view -> {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("product", product);
                        Timber.e("product: %s", product);
                        EditOwnProductFragment fragment = new EditOwnProductFragment();
                        fragment.setArguments(bundle);
                        replaceFragment(fragment);
                    });
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
        ownProductsRecyclerView.setAdapter(productAdapter);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack("addOwn");
        transaction.commit();
    }
}