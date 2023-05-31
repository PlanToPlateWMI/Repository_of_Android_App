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
package pl.plantoplate.ui.main.storage;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentStorageInsideBinding;
import pl.plantoplate.repository.remote.models.Category;
import pl.plantoplate.repository.remote.models.Product;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.shoppingList.ShoppingListRepository;
import pl.plantoplate.repository.remote.storage.StorageRepository;
import pl.plantoplate.tools.CategorySorter;
import pl.plantoplate.ui.main.shoppingList.listAdapters.SetupItemButtons;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.CategoryAdapter;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.ProductsDbaseFragment;

public class StorageInsideFragment extends Fragment {

    private FragmentStorageInsideBinding fragmentStorageInsideBinding;

    private TextView storage_title;
    private FloatingActionButton plus_in_storage;
    private RecyclerView recyclerView;

    private SharedPreferences prefs;
    private StorageRepository storageRepository;

    private ArrayList<Category> storage;

    @Override
    public void onResume() {
        super.onResume();
        getStorage();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentStorageInsideBinding = FragmentStorageInsideBinding.inflate(inflater, container, false);

        plus_in_storage = fragmentStorageInsideBinding.plusInStorage;
        recyclerView = fragmentStorageInsideBinding.productsStorage;
        storage_title = fragmentStorageInsideBinding.textView4;

        plus_in_storage.setOnClickListener(this::onPlusClicked);

        // get storage repository
        storageRepository = new StorageRepository();

        // get shared preferences
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        // if storage is null, make new arraylist
        if (storage == null) {
            storage = new ArrayList<>();
        }

        setUpRecyclerView();
        return fragmentStorageInsideBinding.getRoot();
    }

    public void onPlusClicked(View view) {
        ShoppingListRepository shoppingListRepository = new ShoppingListRepository();
        String token = "Bearer " + prefs.getString("token", "");
        shoppingListRepository.getBoughtProductsIds(token, new ResponseCallback<ArrayList<Integer>>() {
            @Override
            public void onSuccess(ArrayList<Integer> productsIds) {
                if (productsIds.isEmpty()){
                    goToProductsDatabase();
                }
                else{
                   showaddFromPopUp(view, productsIds);
                }
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

    public void goToProductsDatabase(){
        replaceFragment(new ProductsDbaseFragment("storage"));
    }

    public void getStorage(){
        String token = "Bearer " + prefs.getString("token", "");
        storageRepository.getStorage(token, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> response) {
                storage = CategorySorter.sortCategoriesByProduct(response);

                if (storage.isEmpty()) {
                    storage_title.setText(R.string.spizarnia);
                } else {
                    storage_title.setText("");
                }

                // update recycler view
                CategoryAdapter categoryAdapter = (CategoryAdapter) recyclerView.getAdapter();
                Objects.requireNonNull(categoryAdapter).setCategoriesList(storage);
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

    public void moveProductsToStorage(ArrayList<Integer> productsIds){
        String token = "Bearer " + prefs.getString("token", "");
        storageRepository.transferBoughtProductsToStorage(token, productsIds, new ResponseCallback<ArrayList<Product>>(){
            @Override
            public void onSuccess(ArrayList<Product> storageProducts) {
                storage = CategorySorter.sortCategoriesByProduct(storageProducts);

                // update recycler view
                CategoryAdapter categoryAdapter = (CategoryAdapter) recyclerView.getAdapter();
                Objects.requireNonNull(categoryAdapter).setCategoriesList(storage);

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

    public void showaddFromPopUp(View view, ArrayList<Integer> productsIds) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.pop_up_add_products_from_storage_to_storage);

        Button add_from_shopping_list = dialog.findViewById(R.id.button_yes);
        Button go_to_products_database = dialog.findViewById(R.id.button_no);

        add_from_shopping_list.setOnClickListener(v -> {
            moveProductsToStorage(productsIds);
            dialog.dismiss();
        });

        go_to_products_database.setOnClickListener(v -> {
            goToProductsDatabase();
            dialog.dismiss();
        });

        dialog.show();
    }

    public void setUpRecyclerView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CategoryAdapter categoryAdapter = new CategoryAdapter(storage, R.layout.item_spizarnia);
        categoryAdapter.setUpItemButtons(new SetupItemButtons() {
            @Override
            public void setupAddToShoppingListButtonClick(View v, Product product) {
                v.setOnClickListener(view -> {

                });
            }

            @Override
            public void setupProductItemClick(View v, Product product) {
                v.setOnClickListener(view -> {
                });
            }

            //delete product from storage
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
        });
        recyclerView.setAdapter(categoryAdapter);
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
        dialog.setContentView(R.layout.pop_up_delete_from_storage);


        Button acceptButton = dialog.findViewById(R.id.button_yes);
        Button cancelButton = dialog.findViewById(R.id.button_no);


        acceptButton.setOnClickListener(v -> dialog.dismiss());
//        acceptButton.setOnClickListener(v -> {
//            deleteProductFromList(product);
//            dialog.dismiss();
//        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayoutStorage, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
