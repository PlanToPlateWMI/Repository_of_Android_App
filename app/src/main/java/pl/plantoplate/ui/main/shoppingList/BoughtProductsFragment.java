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

    /**
     * Called when the fragment is being created.
     * This method initializes the ShoppingListViewModel by obtaining it from the parent fragment's ViewModelProvider.
     *
     * @param savedInstanceState The saved instance state bundle.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shoppingListViewModel = new ViewModelProvider(requireParentFragment()).get(ShoppingListViewModel.class);
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This method is responsible for fetching the bought products by calling the fetchBoughtProducts method in the ShoppingListViewModel.
     * This ensures that the latest data is displayed when the fragment is resumed.
     */
    @Override
    public void onResume() {
        super.onResume();
        shoppingListViewModel.fetchBoughtProducts();
    }

    /**
     * Called to create the view hierarchy associated with the fragment.
     * This method inflates the layout for the fragment, sets up the move to storage button,
     * and initializes the ViewModel and RecyclerView.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container          The parent view that the fragment's UI should be attached to
     * @param savedInstanceState A Bundle object containing the saved instance state of the fragment
     * @return                   The inflated root view of the fragment
     */
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

    /**
     * Sets up the ViewModel observers to listen for changes in the bought products, success messages,
     * and error messages. Updates the RecyclerView with the bought products and displays toast messages
     * for success and error operations.
     */
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

    /**
     * Sets up the RecyclerView to display the bought products. It configures the layout manager,
     * creates a ProductAdapter, and sets up item buttons for deleting products and moving them back
     * to the shopping list. The adapter is then set to the RecyclerView.
     */
    private void setUpRecyclerView() {
        productsRecyclerView = fragmentKupioneBinding.categoryRecyclerView;
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ProductAdapter productAdapter = new ProductAdapter(new ArrayList<>(), R.layout.item_kupione);
        productAdapter.setUpItemButtons(new SetupItemButtons() {
            /**
             * Sets up the delete product button click listener for a specific product item in the RecyclerView.
             * If the user has the role of "ROLE_ADMIN", clicking the button will show a delete product popup
             * for the corresponding product. Otherwise, the button's visibility is set to `View.INVISIBLE`.
             *
             * @param v       The delete product button view.
             * @param product The product associated with the button.
             */
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

            /**
             * Sets up the check shopping list button click listener for a specific product item in the RecyclerView.
             * Clicking the button will trigger the movement of the product back to the shopping list, as handled by the
             * associated ViewModel.
             *
             * @param v       The check shopping list button view.
             * @param product The product associated with the button.
             */
            @Override
            public void setupCheckShoppingListButtonClick(View v, Product product) {
                v.setOnClickListener(view -> shoppingListViewModel.moveProductToBuy(product));
            }
        });
        productsRecyclerView.setAdapter(productAdapter);
    }

    /**
     * Shows a delete product popup dialog for the specified product.
     * The popup dialog allows the user to confirm the deletion of the product from the shopping list.
     *
     * @param product The product to be deleted.
     */
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

    /**
     * Shows a move product to storage popup dialog.
     * The popup dialog allows the user to confirm the move of products from the bought list to the storage.
     * If there are no products in the bought list, a toast message is displayed.
     */
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

    /**
     * Replaces the current fragment with the specified fragment
     *
     * @param fragment the fragment to replace the current fragment with
     */
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}