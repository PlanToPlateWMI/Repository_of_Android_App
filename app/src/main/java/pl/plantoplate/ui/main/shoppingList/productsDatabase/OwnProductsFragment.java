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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentWlasneBinding;
import pl.plantoplate.requests.products.Product;
import pl.plantoplate.ui.main.shoppingList.listAdapters.OnProductItemClickListener;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.CategorySorter;
import pl.plantoplate.ui.main.shoppingList.listAdapters.product.ProductAdapter;


public class OwnProductsFragment extends Fragment implements SearchView.OnQueryTextListener{

    private FragmentWlasneBinding fragmentWlasneBinding;

    private FloatingActionButton floatingActionButton_wlasne;
    private RecyclerView ownProductsRecyclerView;
    private TextView welcomeTextView;
    private SearchView searchView;

    private ArrayList<Product> groupProductsList;

    public OwnProductsFragment(ArrayList<Product> groupProductsList) {
        if (groupProductsList == null) {
            this.groupProductsList = new ArrayList<>();
        } else {
            this.groupProductsList = CategorySorter.sortProductsByName(groupProductsList);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentWlasneBinding = FragmentWlasneBinding.inflate(inflater, container, false);

        // Get views
        floatingActionButton_wlasne = fragmentWlasneBinding.floatingActionButtonWlasne;
        welcomeTextView = fragmentWlasneBinding.welcomeWlasne;
        searchView = requireActivity().findViewById(R.id.search);


        floatingActionButton_wlasne.setOnClickListener(v -> replaceFragment(new AddYourOwnProductFragment()));
        searchView.setOnQueryTextListener(this);

        setUpRecyclerView();

        if (groupProductsList.isEmpty()) {
            welcomeTextView.setVisibility(View.VISIBLE);
        } else {
            welcomeTextView.setVisibility(View.GONE);
        }

        return fragmentWlasneBinding.getRoot();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {

        // filter products by name
        ArrayList<Product> filteredProducts = CategorySorter.filterProductsBySearch(groupProductsList, query);

        // set up recycler view
        ProductAdapter productAllAdapter = (ProductAdapter) ownProductsRecyclerView.getAdapter();
        Objects.requireNonNull(productAllAdapter).setProductsList(filteredProducts);

        return false;
    }

    public void setUpRecyclerView() {
        ownProductsRecyclerView = fragmentWlasneBinding.productsOwnRecyclerView;
        ownProductsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ProductAdapter productAllAdapter = new ProductAdapter(groupProductsList, R.layout.item_wlasny_produkt);
        productAllAdapter.setOnProductItemClickListener(new OnProductItemClickListener() {
            @Override
            public void onAddToShoppingListButtonClick(View v, Product product) {
                System.out.println(product);
            }

            @Override
            public void onEditProductButtonClick(View v, Product product) {
                System.out.println(product);
            }
        });

        ownProductsRecyclerView.setAdapter(productAllAdapter);
    }

    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}