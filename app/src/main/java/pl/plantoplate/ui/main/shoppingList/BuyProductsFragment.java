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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentTrzebaKupicBinding;
import pl.plantoplate.repository.remote.ResponseCallback;
import pl.plantoplate.repository.remote.shoppingList.ShoppingListRepository;
import pl.plantoplate.repository.remote.models.Product;
import pl.plantoplate.repository.remote.models.ShoppingList;
import pl.plantoplate.ui.main.shoppingList.listAdapters.SetupItemButtons;
import pl.plantoplate.repository.remote.models.Category;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.CategoryAdapter;
import pl.plantoplate.tools.CategorySorter;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.ProductsDbaseFragment;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.popups.ModifyProductpopUp;
import pl.plantoplate.ui.main.shoppingList.viewModels.ShoppingListViewModel;
import pl.plantoplate.ui.main.storage.StorageViewModel;

/**
 * This fragment is responsible for displaying the shopping list.
 */
public class BuyProductsFragment extends Fragment {

    private FragmentTrzebaKupicBinding fragmentTrzebaKupicBinding;
    private ShoppingListViewModel shoppingListViewModel;

    private FloatingActionButton plus_in_trzeba_kupic;
    private RecyclerView categoryRecyclerView;

    private SharedPreferences prefs;

    /**
     * Called when the fragment is being created.
     * This method is called after the parent fragment's {@link #onCreateView} has returned.
     * It is recommended to initialize any necessary resources or variables in this method.
     * In this case, the method creates an instance of the {@link ShoppingListViewModel} and associates it with the parent fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shoppingListViewModel = new ViewModelProvider(requireParentFragment()).get(ShoppingListViewModel.class);
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This method is called after the fragment has been resumed from a paused state.
     * It is recommended to perform any necessary UI updates or data fetching in this method.
     * In this case, the method fetches the "to-buy" products using the {@link ShoppingListViewModel}.
     */
    @Override
    public void onResume() {
        super.onResume();
        shoppingListViewModel.fetchUserInfo();
        shoppingListViewModel.fetchToBuyProducts();
    }

    /**
     * Called to create the view hierarchy associated with the fragment.
     * This method is responsible for inflating the fragment's layout, initializing UI elements,
     * setting up the RecyclerView, setting up the ViewModel, and returning the root view of the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState A Bundle containing any saved state information.
     * @return The root view of the fragment.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentTrzebaKupicBinding = FragmentTrzebaKupicBinding.inflate(inflater, container, false);
        plus_in_trzeba_kupic = fragmentTrzebaKupicBinding.plusInTrzebaKupic;
        plus_in_trzeba_kupic.setOnClickListener(v -> replaceFragment(new ProductsDbaseFragment("shoppingList")));

        // get shared preferences
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        setUpRecyclerView();
        setUpViewModel();
        return fragmentTrzebaKupicBinding.getRoot();
    }

    /**
     * Shows a popup dialog for adding a product to the shopping list.
     *
     * @param product The product to be added to the shopping list.
     */
    public void showAddProductPopup(Product product) {
        ModifyProductpopUp addToCartPopUp = new ModifyProductpopUp(requireContext(), product);
        addToCartPopUp.acceptButton.setOnClickListener(v -> {
            String quantityValue = Objects.requireNonNull(addToCartPopUp.quantity.getText()).toString();
            if (quantityValue.isEmpty()) {
                addToCartPopUp.quantity.setError("Podaj ilość");
                return;
            }
            float quantity = BigDecimal.valueOf(Float.parseFloat(quantityValue)).setScale(3, RoundingMode.HALF_UP).floatValue();
            product.setAmount(quantity);

            shoppingListViewModel.changeProductAmount(product, "toBuy");
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ShoppingListFragment()).commit();
            addToCartPopUp.dismiss();
        });
        addToCartPopUp.show();
    }

    /**
     * Shows a popup dialog for deleting a product from the shopping list.
     *
     * @param product The product to be deleted from the shopping list.
     */
    public void showDeleteProductPopup(Product product) {
        Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        dialog.setContentView(R.layout.new_pop_up_delete_product_from_shopping_list);

        TextView acceptButton = dialog.findViewById(R.id.button_yes);
        TextView cancelButton = dialog.findViewById(R.id.button_no);

        acceptButton.setOnClickListener(v -> {
            shoppingListViewModel.deleteProductFromList(product, "toBuy");
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    /**
     * Set up the RecyclerView for displaying the products in the shopping list.
     * Configures the layout manager, adapter, and item buttons for the RecyclerView.
     * The visibility of the delete product button depends on the user role.
     */
    private void setUpRecyclerView() {
        categoryRecyclerView = fragmentTrzebaKupicBinding.productsOwnRecyclerView;
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CategoryAdapter categoryAdapter = new CategoryAdapter(new ArrayList<>(), R.layout.item_trzeba_kupic, R.layout.item_category_lista);
        categoryAdapter.setUpItemButtons(new SetupItemButtons() {

            /**
             * Set up the click listener for the delete product button in the shopping list item.
             * The visibility of the button depends on the user's role.
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
             * Set up the click listener for the check shopping list button in the shopping list item.
             *
             * @param v       The check shopping list button view.
             * @param product The product associated with the button.
             */
            @Override
            public void setupCheckShoppingListButtonClick(View v, Product product) {
                v.setOnClickListener(view -> shoppingListViewModel.moveProductToBought(product));
            }

            /**
             * Set up the click listener for the product item in the shopping list.
             *
             * @param v       The product item view.
             * @param product The product associated with the item.
             */
            @Override
            public void setupProductItemClick(View v, Product product) {
                v.setOnClickListener(view -> showAddProductPopup(product));
            }
        });
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    /**
     * Set up the ViewModel observers for the shopping list.
     *
     * This method observes the changes in the ViewModel and updates the UI accordingly.
     */
    public void setUpViewModel() {
        shoppingListViewModel.getUserInfo().observe(getViewLifecycleOwner(), userInfo -> {
        });

        // get to buy products
        shoppingListViewModel.getToBuyProducts().observe(getViewLifecycleOwner(), toBuyProducts -> {
            // update recycler view
            CategoryAdapter categoryAdapter = (CategoryAdapter) categoryRecyclerView.getAdapter();
            Objects.requireNonNull(categoryAdapter).setCategoriesList(CategorySorter.sortCategoriesByProduct(toBuyProducts));
        });

        // get success message
        shoppingListViewModel.getToBuyProductsOnSuccessOperation().observe(getViewLifecycleOwner(), successMessage -> {
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), successMessage, Toast.LENGTH_SHORT).show());
            }
        });

        // get error message
        shoppingListViewModel.getToBuyProductsOnErrorOperation().observe(getViewLifecycleOwner(), errorMessage -> {
            if (isAdded()) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireActivity(), errorMessage, Toast.LENGTH_SHORT).show());
            }
        });

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
        transaction.addToBackStack("trzebaKupicFragment");
        transaction.commit();
    }
}