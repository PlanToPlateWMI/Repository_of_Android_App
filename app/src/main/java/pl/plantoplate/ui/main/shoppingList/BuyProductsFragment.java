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

package pl.plantoplate.ui.main.shoppingList;

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

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentTrzebaKupicBinding;
import pl.plantoplate.requests.products.Product;
import pl.plantoplate.ui.main.shoppingList.listAdapters.OnProductItemClickListener;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.Category;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.CategoryAdapter;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.CategorySorter;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.ProductsDbaseFragment;

public class BuyProductsFragment extends Fragment {

    private FragmentTrzebaKupicBinding fragmentTrzebaKupicBinding;
    private FloatingActionButton plus_in_trzeba_kupic;

    private RecyclerView categoryRecyclerView;
    private TextView welcomeTextView;

    private ArrayList<Category> toBuyProductsList;

    public BuyProductsFragment(ArrayList<Product> toBuyProductsList) {
        if (toBuyProductsList == null) {
            this.toBuyProductsList = new ArrayList<>();
            return;
        }
        this.toBuyProductsList = CategorySorter.sortCategoriesByProduct(toBuyProductsList);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentTrzebaKupicBinding = FragmentTrzebaKupicBinding.inflate(inflater, container, false);
        plus_in_trzeba_kupic = fragmentTrzebaKupicBinding.plusInTrzebaKupic;
        plus_in_trzeba_kupic.setOnClickListener(v -> replaceFragment(new ProductsDbaseFragment()));

        setUpRecyclerView();
        return fragmentTrzebaKupicBinding.getRoot();
    }

    private void setUpRecyclerView() {
        categoryRecyclerView = fragmentTrzebaKupicBinding.productsOwnRecyclerView;
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CategoryAdapter categoryAdapter = new CategoryAdapter(toBuyProductsList, R.layout.item_kupione);
        categoryAdapter.setOnProductItemClickListener(new OnProductItemClickListener() {
            @Override
            public void onDeleteProductButtonClick(View v, Product product) {

            }

            @Override
            public void onCheckShoppingListButtonClick(View v, Product product) {

            }
        });
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    /**
     * Replaces the current fragment with the specified fragment
     *
     * @param fragment the fragment to replace the current fragment with
     */
    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}