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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentKupioneBinding;
import pl.plantoplate.repository.remote.models.Product;
import pl.plantoplate.ui.main.shoppingList.listAdapters.SetupItemButtons;
import pl.plantoplate.tools.CategorySorter;
import pl.plantoplate.ui.main.shoppingList.listAdapters.product.ProductAdapter;
import pl.plantoplate.ui.main.shoppingList.viewModels.ShoppingListViewModel;
import pl.plantoplate.ui.main.storage.StorageFragment;

/**
 * This fragment is responsible for displaying bought products.
 */
public class BoughtProductsFragment extends Fragment {

    private FragmentKupioneBinding fragmentKupioneBinding;
    private ShoppingListViewModel shoppingListViewModel;

    private RecyclerView productsRecyclerView;
    private FloatingActionButton moveToStorageButton;

    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shoppingListViewModel = new ViewModelProvider(requireParentFragment()).get(ShoppingListViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        shoppingListViewModel.fetchBoughtProducts();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        fragmentKupioneBinding = FragmentKupioneBinding.inflate(inflater, container, false);
        moveToStorageButton = fragmentKupioneBinding.floatingActionButton;

        // get shared preferences
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        // set up move to storage button
        moveToStorageButton.setOnClickListener(v -> showMoveProductToStoragePopUp());

        setUpViewModel();
        setUpRecyclerView();
        return fragmentKupioneBinding.getRoot();
    }

    public void setUpViewModel() {
        // get to buy products
        shoppingListViewModel.getBoughtProducts().observe(getViewLifecycleOwner(), boughtProducts -> {

            // update recycler view
            ProductAdapter productAdapter = (ProductAdapter) productsRecyclerView.getAdapter();
            Objects.requireNonNull(productAdapter).setProductsList(CategorySorter.sortProductsByName(boughtProducts));
        });

        // get success message
        shoppingListViewModel.getBoughtProductsOnSuccessOperation().observe(getViewLifecycleOwner(), successMessage -> {
            requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), successMessage, Toast.LENGTH_SHORT).show());
        });

        // get error message
        shoppingListViewModel.getBoughtProductsOnErrorOperation().observe(getViewLifecycleOwner(), errorMessage -> {
            requireActivity().runOnUiThread(() -> Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show());
        });

    }

    private void setUpRecyclerView() {
        productsRecyclerView = fragmentKupioneBinding.categoryRecyclerView;
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ProductAdapter productAdapter = new ProductAdapter(new ArrayList<>(), R.layout.item_kupione);
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
                v.setOnClickListener(view -> shoppingListViewModel.moveProductToBuy(product));
            }
        });
        productsRecyclerView.setAdapter(productAdapter);
    }

    public void showDeleteProductPopup(Product product) {
        Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.new_pop_up_delete_product_from_shopping_list);

        TextView acceptButton = dialog.findViewById(R.id.button_yes);
        TextView cancelButton = dialog.findViewById(R.id.button_no);

        acceptButton.setOnClickListener(v -> {
            shoppingListViewModel.deleteProductFromList(product, "bought");
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    public void showMoveProductToStoragePopUp(){
        if (Objects.requireNonNull(shoppingListViewModel.getBoughtProducts().getValue()).isEmpty())
        {
            Toast.makeText(requireContext(), "Nie ma nic do przeniesienia", Toast.LENGTH_SHORT).show();
            return;
        }
        Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.new_pop_up_add_to_storage);

        TextView acceptButton = dialog.findViewById(R.id.button_yes);
        TextView cancelButton = dialog.findViewById(R.id.button_no);

        acceptButton.setOnClickListener(v -> {
            shoppingListViewModel.moveProductsToStorage();

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