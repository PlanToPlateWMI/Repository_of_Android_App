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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentWszystkieBinding;
import pl.plantoplate.requests.products.Product;
import pl.plantoplate.ui.main.shoppingList.listAdapters.OnProductItemClickListener;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.Category;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.CategoryAdapter;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.CategorySorter;

public class AllProductsFragment extends Fragment implements SearchView.OnQueryTextListener {

    private FragmentWszystkieBinding fragmentWszystkieBinding;

    private RecyclerView categoryRecyclerView;
    private TextView welcomeTextView;
    private SearchView searchView;

    private ArrayList<Category> allProductsList;

    public AllProductsFragment(ArrayList<Product> generalProductsList, ArrayList<Product> groupProductsList) {
        if (generalProductsList == null) {
            generalProductsList = new ArrayList<>();
        }
        if (groupProductsList == null) {
            groupProductsList = new ArrayList<>();
        }
        if (generalProductsList.isEmpty() && groupProductsList.isEmpty()) {
            allProductsList = new ArrayList<>();
            return;
        }
        ArrayList<Product> allProductsList = new ArrayList<>();
        allProductsList.addAll(generalProductsList);
        allProductsList.addAll(groupProductsList);

        this.allProductsList = CategorySorter.sortCategoriesByProduct(allProductsList);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentWszystkieBinding = FragmentWszystkieBinding.inflate(inflater, container, false);

        welcomeTextView = fragmentWszystkieBinding.textView3;
        searchView = requireActivity().findViewById(R.id.search);

        // set listener for search view
        searchView.setOnQueryTextListener(this);

        setUpRecyclerView();

        if (allProductsList.isEmpty()) {
            welcomeTextView.setVisibility(View.VISIBLE);
        } else {
            welcomeTextView.setVisibility(View.GONE);
        }

        return fragmentWszystkieBinding.getRoot();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {

        // filter products by name
        ArrayList<Product> filteredProducts = CategorySorter.filterCategoriesBySearch(allProductsList, query);

        // sort products by category
        ArrayList<Category> filteredList = CategorySorter.sortCategoriesByProduct(filteredProducts);

        // set up recycler view
        CategoryAdapter categoryAdapter = (CategoryAdapter) categoryRecyclerView.getAdapter();
        Objects.requireNonNull(categoryAdapter).setCategoriesList(filteredList);

        return false;
    }

    public void setUpRecyclerView() {

        // set up recycler view
        categoryRecyclerView = fragmentWszystkieBinding.categoryRecyclerView;
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CategoryAdapter categoryAdapter = new CategoryAdapter(allProductsList, R.layout.item_wszystkie_produkt);
        categoryAdapter.setOnProductItemClickListener(new OnProductItemClickListener() {
            @Override
            public void onAddToShoppingListButtonClick(View v, Product product) {
                System.out.println(product);
            }
        });
        categoryRecyclerView.setAdapter(categoryAdapter);
    }
}