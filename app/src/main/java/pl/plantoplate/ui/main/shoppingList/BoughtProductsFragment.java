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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.databinding.FragmentKupioneBinding;
import pl.plantoplate.utils.CategorySorter;
import pl.plantoplate.ui.main.recyclerViews.adapters.ProductAdapter;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;
import pl.plantoplate.ui.main.popUps.DeleteProductPopUp;
import pl.plantoplate.ui.main.shoppingList.viewModels.BoughtProductsListViewModel;
import pl.plantoplate.ui.main.storage.StorageFragment;

/**
 * This fragment is responsible for displaying bought products.
 */
public class BoughtProductsFragment extends Fragment {

    private BoughtProductsListViewModel boughtProductsListViewModel;
    private RecyclerView productsRecyclerView;
    private FloatingActionButton moveToStorageButton;
    private TextView titleTextView;
    private SharedPreferences prefs;
    private ProductAdapter productListAdapter;

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
        FragmentKupioneBinding fragmentKupioneBinding = FragmentKupioneBinding.inflate(inflater, container, false);
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        initViews(fragmentKupioneBinding);
        setClickListeners();
        setUpViewModel();
        setUpRecyclerView();
        return fragmentKupioneBinding.getRoot();
    }

    /**
     * This method creates a pop up dialog for moving products to storage. It inflates the layout
     * for the dialog, sets up the dialog buttons, and displays the dialog.
     */
    private void initViews(FragmentKupioneBinding fragmentKupioneBinding) {
        moveToStorageButton = fragmentKupioneBinding.floatingActionButton;
        productsRecyclerView = fragmentKupioneBinding.categoryRecyclerView;
        titleTextView = fragmentKupioneBinding.textViewKupione;
    }

    /**
        * This method is called when the move to storage button is clicked. It creates a pop up dialog
     */
    private void setClickListeners() {
        moveToStorageButton.setOnClickListener(v -> showMoveProductToStoragePopUp());
    }

    @Override
    public void onResume() {
        super.onResume();
        boughtProductsListViewModel.fetchUserInfo();
        boughtProductsListViewModel.fetchBoughtProducts();
    }

    /**
     * Sets up the ViewModel observers to listen for changes in the bought products, success messages,
     * and error messages. Updates the RecyclerView with the bought products and displays toast messages
     * for success and error operations.
     */
    public void setUpViewModel() {

        String role = prefs.getString("role", "");

        boughtProductsListViewModel = new ViewModelProvider(requireParentFragment()).get(BoughtProductsListViewModel.class);
        boughtProductsListViewModel.getUserInfo().observe(getViewLifecycleOwner(), userInfo -> {
        });
        boughtProductsListViewModel.getBoughtProducts().observe(getViewLifecycleOwner(), boughtProducts -> {
            if(boughtProducts.isEmpty()){
                moveToStorageButton.setVisibility(View.INVISIBLE);
                moveToStorageButton.setOnClickListener(v -> showMoveProductToStoragePopUp());
                titleTextView.setText(R.string.wprowadzenie_lista_zakupow_kupione);
            }
            else if(!role.equals("ROLE_ADMIN")){
                moveToStorageButton.setVisibility(View.INVISIBLE);
                moveToStorageButton.setOnClickListener(v -> showMoveProductToStoragePopUp());
                titleTextView.setText("");
            }
            else{
                moveToStorageButton.setVisibility(View.VISIBLE);
                moveToStorageButton.setOnClickListener(v -> showMoveProductToStoragePopUp());
                titleTextView.setText("");
            }
            productListAdapter.setProductsList(CategorySorter.sortProductsByName(boughtProducts));
        });
        boughtProductsListViewModel.getResponseMessage().observe(getViewLifecycleOwner(), responseMessage ->
                Toast.makeText(requireActivity(), responseMessage, Toast.LENGTH_SHORT).show());

    }

    /**
     * Sets up the RecyclerView to display the bought products. It configures the layout manager,
     * creates a ProductAdapter, and sets up item buttons for deleting products and moving them back
     * to the shopping list. The adapter is then set to the RecyclerView.
     */
    private void setUpRecyclerView() {
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<Product> boughtProducts = boughtProductsListViewModel.getBoughtProducts().getValue();
        productListAdapter = new ProductAdapter(boughtProducts, R.layout.item_kupione);

        productListAdapter.setUpItemButtons(new SetupItemButtons() {
            @Override
            public void setupDeleteProductButtonClick(View v, Product product) {
                String role = prefs.getString("role", "");
                if (role.equals("ROLE_ADMIN")) {
                    v.setOnClickListener(view -> new DeleteProductPopUp(requireContext(),
                            R.layout.new_pop_up_delete_product_from_shopping_list,
                            view1 -> boughtProductsListViewModel.deleteProductFromList(product))
                            .show());
                } else {
                    v.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void setupCheckShoppingListButtonClick(View v, Product product) {
                v.setOnClickListener(view -> boughtProductsListViewModel.moveProductToBuy(product));
            }
        });

        productsRecyclerView.setAdapter(productListAdapter);

    }

    /**
     * Shows a move product to storage popup dialog.
     * The popup dialog allows the user to confirm the move of products from the bought list to the storage.
     * If there are no products in the bought list, a toast message is displayed.
     */
    public void showMoveProductToStoragePopUp(){
        Dialog dialog = new Dialog(requireContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.new_pop_up_add_to_storage);

        TextView acceptButton = dialog.findViewById(R.id.button_yes);
        TextView cancelButton = dialog.findViewById(R.id.button_no);

        acceptButton.setOnClickListener(v -> {
            boughtProductsListViewModel.moveProductsToStorage();

            ((BottomNavigationView) requireActivity()
                    .findViewById(R.id.bottomNavigationView)).setSelectedItemId(R.id.cottage);

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