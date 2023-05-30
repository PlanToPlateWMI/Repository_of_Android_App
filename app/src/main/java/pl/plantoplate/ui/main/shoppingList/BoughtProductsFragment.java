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

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentKupioneBinding;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.shoppingList.ShoppingListRepository;
import pl.plantoplate.repository.models.Product;
import pl.plantoplate.repository.models.ShoppingList;
import pl.plantoplate.repository.remote.storage.StorageRepository;
import pl.plantoplate.ui.main.shoppingList.listAdapters.OnProductItemClickListener;
import pl.plantoplate.tools.CategorySorter;
import pl.plantoplate.ui.main.shoppingList.listAdapters.product.ProductAdapter;
import pl.plantoplate.ui.main.storage.StorageFragment;

public class BoughtProductsFragment extends Fragment {

    private FragmentKupioneBinding fragmentKupioneBinding;

    private RecyclerView productsRecyclerView;
    private FloatingActionButton moveToStorageButton;

    private SharedPreferences prefs;
    private ShoppingListRepository shoppingListRepository;
    private StorageRepository storageRepository;

    private ArrayList<Product> boughtProductsList;

    @Override
    public void onResume() {
        super.onResume();
        getShoppingList();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        fragmentKupioneBinding = FragmentKupioneBinding.inflate(inflater, container, false);
        moveToStorageButton = fragmentKupioneBinding.floatingActionButton;

        // get shared preferences
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        // get shopping list repository
        shoppingListRepository = new ShoppingListRepository();

        // get storage repository
        storageRepository = new StorageRepository();

        // set listener for floating action button
        moveToStorageButton.setOnClickListener(v -> showMoveProductToStoragePopUp());

        setUpRecyclerView();

        return fragmentKupioneBinding.getRoot();
    }

    public void getShoppingList(){
        String token = "Bearer " + prefs.getString("token", "");

        shoppingListRepository.getBoughtShoppingList(token, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> shopList) {
                boughtProductsList = CategorySorter.sortProductsByName(shopList);

                // update recycler view
                ProductAdapter productAdapter = (ProductAdapter) productsRecyclerView.getAdapter();
                Objects.requireNonNull(productAdapter).setProductsList(boughtProductsList);
            }

            @Override
            public void onError(String errorMessage) {
                Snackbar.make(requireActivity().findViewById(R.id.frame_layout), errorMessage, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String failureMessage) {
                Snackbar.make(requireActivity().findViewById(R.id.frame_layout), failureMessage, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void deleteProductFromList(Product product){
        String token = "Bearer " + prefs.getString("token", "");
        shoppingListRepository.deleteProductFromShopList(token, product.getId(), new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> shopList) {
                boughtProductsList = CategorySorter.sortProductsByName(shopList);

                // update recycler view
                ProductAdapter productAdapter = (ProductAdapter) productsRecyclerView.getAdapter();
                Objects.requireNonNull(productAdapter).setProductsList(boughtProductsList);
            }

            @Override
            public void onError(String errorMessage) {
                Snackbar.make(requireActivity().findViewById(R.id.frame_layout), errorMessage, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String failureMessage) {
                Snackbar.make(requireActivity().findViewById(R.id.frame_layout), failureMessage, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void moveProductToBuy(Product product){
        String token = "Bearer " + prefs.getString("token", "");
        shoppingListRepository.changeProductStateInShopList(token, product.getId(), new ResponseCallback<ShoppingList>() {
            @Override
            public void onSuccess(ShoppingList shoppingList) {
                boughtProductsList = CategorySorter.sortProductsByName(shoppingList.getBought());

                // update recycler view
                ProductAdapter productAdapter = (ProductAdapter) productsRecyclerView.getAdapter();
                Objects.requireNonNull(productAdapter).setProductsList(boughtProductsList);

            }

            @Override
            public void onError(String errorMessage) {
                Snackbar.make(requireActivity().findViewById(R.id.frame_layout), errorMessage, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String failureMessage) {
                Snackbar.make(requireActivity().findViewById(R.id.frame_layout), failureMessage, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void moveProductsToStorage(){
        ArrayList<Integer> productsIds = new ArrayList<>();
        for (Product storageProduct : boughtProductsList) {
            productsIds.add(storageProduct.getId());
        }

        String token = "Bearer " + prefs.getString("token", "");
        storageRepository.transferBoughtProductsToStorage(token, productsIds, new ResponseCallback<ArrayList<Product>>(){
            @Override
            public void onSuccess(ArrayList<Product> response) {
                Snackbar.make(requireActivity().findViewById(R.id.frame_layout), "Produkty zostały przeniesione do spiżarni", Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(String errorMessage) {
                Snackbar.make(requireActivity().findViewById(R.id.frame_layout), errorMessage, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(String failureMessage) {
                Snackbar.make(requireActivity().findViewById(R.id.frame_layout), failureMessage, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void setUpRecyclerView() {
        if (boughtProductsList == null) {
            boughtProductsList = new ArrayList<>();
        }

        productsRecyclerView = fragmentKupioneBinding.categoryRecyclerView;
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ProductAdapter productAdapter = new ProductAdapter(boughtProductsList, R.layout.item_kupione);
        productAdapter.setOnProductItemClickListener(new OnProductItemClickListener() {
            @Override
            public void onDeleteProductButtonClick(View v, Product product) {
                showDeleteProductPopup(product);
            }

            @Override
            public void onCheckShoppingListButtonClick(View v, Product product) {
                moveProductToBuy(product);
            }
        });
        productsRecyclerView.setAdapter(productAdapter);
    }

    public void showDeleteProductPopup(Product product) {
        Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.pop_up_delete_product_from_shopping_list);


        Button acceptButton = dialog.findViewById(R.id.button_yes);
        Button cancelButton = dialog.findViewById(R.id.button_no);

        acceptButton.setOnClickListener(v -> {
            deleteProductFromList(product);
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void showMoveProductToStoragePopUp(){
        Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.pop_up_add_to_storage);

        Button acceptButton = dialog.findViewById(R.id.button_yes);
        Button cancelButton = dialog.findViewById(R.id.button_no);

        acceptButton.setOnClickListener(v -> {

            moveProductsToStorage();

            replaceFragment(new StorageFragment());
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}