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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentWlasneBinding;
import pl.plantoplate.requests.products.Product;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.listAdapters.category.CategorySorter;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.listAdapters.product.ProductAllAdapter;


public class OwnProductsFragment extends Fragment {

    private FragmentWlasneBinding fragmentWlasneBinding;

    private FloatingActionButton floatingActionButton_wlasne;
    private RecyclerView ownProductsRecyclerView;
    private TextView welcomeTextView;

    private ArrayList<Product> groupProductsList;

    public OwnProductsFragment(ArrayList<Product> groupProductsList) {
        if (groupProductsList == null) {
            this.groupProductsList = new ArrayList<>();
        } else {
            this.groupProductsList = groupProductsList;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentWlasneBinding = FragmentWlasneBinding.inflate(inflater, container, false);

        floatingActionButton_wlasne = fragmentWlasneBinding.floatingActionButtonWlasne;
        welcomeTextView = fragmentWlasneBinding.welcomeWlasne;

        floatingActionButton_wlasne.setOnClickListener(v -> replaceFragment(new AddYourOwnProductFragment()));

        setUpRecyclerView();

        if (groupProductsList.isEmpty()) {
            welcomeTextView.setVisibility(View.VISIBLE);
        } else {
            welcomeTextView.setVisibility(View.GONE);
        }

        return fragmentWlasneBinding.getRoot();
    }

    public void setUpRecyclerView() {
        List<Product> ownProducts = CategorySorter.sortProductsByName(groupProductsList);
        ownProductsRecyclerView = fragmentWlasneBinding.productsOwnRecyclerView;
        ownProductsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ProductAllAdapter productAdapter = new ProductAllAdapter(ownProducts);
        ownProductsRecyclerView.setAdapter(productAdapter);
    }

    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}