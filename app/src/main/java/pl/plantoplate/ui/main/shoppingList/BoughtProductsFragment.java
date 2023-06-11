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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentKupioneBinding;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.shoppingList.ShoppingListRepository;
import pl.plantoplate.repository.remote.models.Product;
import pl.plantoplate.repository.remote.models.ShoppingList;
import pl.plantoplate.repository.remote.storage.StorageRepository;
import pl.plantoplate.ui.main.shoppingList.listAdapters.SetupItemButtons;
import pl.plantoplate.tools.CategorySorter;
import pl.plantoplate.ui.main.shoppingList.listAdapters.product.ProductAdapter;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.AddYourOwnProductFragment;
import pl.plantoplate.ui.main.storage.StorageFragment;

/**
 * This fragment is responsible for displaying bought products.
 */
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

        setUpRecyclerView();

        return fragmentKupioneBinding.getRoot();
    }

    public void getShoppingList(){
        String token = "Bearer " + prefs.getString("token", "");

        shoppingListRepository.getBoughtShoppingList(token, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> shopList) {
                boughtProductsList = CategorySorter.sortProductsByName(shopList);

                // Setup listeners
                if(boughtProductsList.isEmpty()) {
                    moveToStorageButton.setVisibility(View.INVISIBLE);
                    moveToStorageButton.setClickable(false);
                }else{
                    moveToStorageButton.setOnClickListener(v -> showMoveProductToStoragePopUp());
                    moveToStorageButton.setVisibility(View.VISIBLE);
                    moveToStorageButton.setClickable(true);
                }

                // update recycler view
                ProductAdapter productAdapter = (ProductAdapter) productsRecyclerView.getAdapter();
                Objects.requireNonNull(productAdapter).setProductsList(boughtProductsList);
            }

            @Override
            public void onError(String errorMessage) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(String failureMessage) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), failureMessage, Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    public void deleteProductFromList(Product product){
        String token = "Bearer " + prefs.getString("token", "");
        shoppingListRepository.deleteProductFromShopList(token, product.getId(), new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> shopList) {
                boughtProductsList = CategorySorter.sortProductsByName(shopList);

                // Setup listeners
                if(boughtProductsList.isEmpty()) {
                    moveToStorageButton.setVisibility(View.INVISIBLE);
                    moveToStorageButton.setClickable(false);
                }else{
                    moveToStorageButton.setOnClickListener(v -> showMoveProductToStoragePopUp());
                    moveToStorageButton.setVisibility(View.VISIBLE);
                    moveToStorageButton.setClickable(true);
                }

                // update recycler view
                ProductAdapter productAdapter = (ProductAdapter) productsRecyclerView.getAdapter();
                Objects.requireNonNull(productAdapter).setProductsList(boughtProductsList);
            }

            @Override
            public void onError(String errorMessage) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(String failureMessage) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), failureMessage, Toast.LENGTH_SHORT).show());
                }
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
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(String failureMessage) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), failureMessage, Toast.LENGTH_SHORT).show());
                }
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
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(),"Produkty zostały przeniesione do spiżarni" ,
                            Toast.LENGTH_LONG).show());
                }
            }

            @Override
            public void onError(String errorMessage) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onFailure(String failureMessage) {
                if (isAdded()) {
                    requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), failureMessage, Toast.LENGTH_SHORT).show());
                }
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
        productAdapter.setUpItemButtons(new SetupItemButtons() {
            @Override
            public void setupDeleteProductButtonClick(View v, Product product) {
                String role = prefs.getString("role", "");
                if(role.equals("ROLE_ADMIN")) {
                    v.setOnClickListener(view -> showDeleteProductPopup(product));
                }
                else{
                    //set visibility none
                    v.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void setupCheckShoppingListButtonClick(View v, Product product) {
                v.setOnClickListener(view -> moveProductToBuy(product));
            }
        });
        productsRecyclerView.setAdapter(productAdapter);
    }

    public void showDeleteProductPopup(Product product) {
        Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        //add delay!!!!
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dialog.setContentView(R.layout.new_pop_up_delete_product_from_shopping_list);

        TextView acceptButton = dialog.findViewById(R.id.button_yes);
        TextView cancelButton = dialog.findViewById(R.id.button_no);

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
        dialog.setContentView(R.layout.new_pop_up_add_to_storage);

        TextView acceptButton = dialog.findViewById(R.id.button_yes);
        TextView cancelButton = dialog.findViewById(R.id.button_no);

        acceptButton.setOnClickListener(v -> {

            moveProductsToStorage();

            replaceFragment(new StorageFragment());
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        shoppingListRepository.cancelCalls();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}