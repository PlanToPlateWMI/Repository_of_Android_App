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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import pl.plantoplate.databinding.FragmentWszystkieBinding;
import pl.plantoplate.requests.products.Product;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.listAdapters.category.Category;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.listAdapters.category.CategoryAdapter;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.listAdapters.category.CategorySorter;

public class AllProductsFragment extends Fragment {

    private FragmentWszystkieBinding fragmentWszystkieBinding;

    private RecyclerView categoryRecyclerView;
    private TextView welcomeTextView;

    private ArrayList<Product> allProductsList;

    public AllProductsFragment(ArrayList<Product> generalProductsList, ArrayList<Product> groupProductsList) {
        ArrayList<Product> allProductsList = new ArrayList<>();
        allProductsList.addAll(generalProductsList);
        allProductsList.addAll(groupProductsList);

        this.allProductsList = allProductsList;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentWszystkieBinding = FragmentWszystkieBinding.inflate(inflater, container, false);

        welcomeTextView = fragmentWszystkieBinding.textView3;

        setUpRecyclerView();

        if (allProductsList.isEmpty()) {
            welcomeTextView.setVisibility(View.VISIBLE);
        } else {
            welcomeTextView.setVisibility(View.GONE);
        }

        return fragmentWszystkieBinding.getRoot();
    }

    public void setUpRecyclerView() {
        // sort products by category
        List<Category> categories = CategorySorter.sortCategoriesByProduct(allProductsList);

        // set up recycler view
        categoryRecyclerView = fragmentWszystkieBinding.categoryRecyclerView;
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CategoryAdapter categoryAdapter = new CategoryAdapter(categories);
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

}