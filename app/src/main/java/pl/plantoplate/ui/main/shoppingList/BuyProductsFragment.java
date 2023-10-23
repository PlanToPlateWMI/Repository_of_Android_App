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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.Category;
import pl.plantoplate.data.remote.models.Product;
import pl.plantoplate.databinding.FragmentTrzebaKupicBinding;
import pl.plantoplate.tools.CategorySorter;
import pl.plantoplate.ui.main.recyclerViews.adapters.CategoryAdapter;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;
import pl.plantoplate.ui.main.productsDatabase.ProductsDbaseFragment;
import pl.plantoplate.ui.main.productsDatabase.popups.DeleteProductPopUp;
import pl.plantoplate.ui.main.productsDatabase.popups.ModifyProductPopUp;
import pl.plantoplate.ui.main.shoppingList.viewModels.ToBuyProductsListViewModel;
import timber.log.Timber;

/**
 * This fragment is responsible for displaying the shopping list.
 */
public class BuyProductsFragment extends Fragment {

    private FragmentTrzebaKupicBinding fragmentTrzebaKupicBinding;
    private ToBuyProductsListViewModel toBuyProductsListViewModel;

    private FloatingActionButton plus_in_trzeba_kupic;
    private RecyclerView categoryRecyclerView;

    private SharedPreferences prefs;

    private CategoryAdapter categoryAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toBuyProductsListViewModel = new ViewModelProvider(requireParentFragment()).get(ToBuyProductsListViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        toBuyProductsListViewModel.fetchUserInfo();
        toBuyProductsListViewModel.fetchToBuyProducts();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Timber.d("onCreate() called");

        // Inflate the layout for this fragment
        fragmentTrzebaKupicBinding = FragmentTrzebaKupicBinding.inflate(inflater, container, false);
        plus_in_trzeba_kupic = fragmentTrzebaKupicBinding.plusInTrzebaKupic;
        categoryRecyclerView = fragmentTrzebaKupicBinding.productsOwnRecyclerView;
        plus_in_trzeba_kupic.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("comesFrom", "shoppingList");
            Fragment fragment = new ProductsDbaseFragment();
            fragment.setArguments(args);
            replaceFragment(fragment);
        });

        // get shared preferences
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        setUpViewModel();
        setUpRecyclerView();
        return fragmentTrzebaKupicBinding.getRoot();
    }

    /**
     * Shows a popup dialog for adding a product to the shopping list.
     *
     * @param product The product to be added to the shopping list.
     */
    public void showAddProductPopup(Product product) {
        ModifyProductPopUp addToCartPopUp = new ModifyProductPopUp(requireContext(), product);
        addToCartPopUp.acceptButton.setOnClickListener(v -> {
            Optional<CharSequence> optionalQuantity = Optional.ofNullable(addToCartPopUp.quantity.getText());

            if (optionalQuantity.map(CharSequence::toString).orElse("").isEmpty()) {
                addToCartPopUp.quantity.setError("Podaj ilość");
                return;
            }

            float quantity = BigDecimal.valueOf(Float.parseFloat(optionalQuantity.get().toString()))
                    .setScale(3, RoundingMode.HALF_UP)
                    .floatValue();

            product.setAmount(quantity);
            toBuyProductsListViewModel.changeProductAmount(product);
            addToCartPopUp.dismiss();
        });
        addToCartPopUp.show();
    }

    /**
     * Set up the RecyclerView for displaying the products in the shopping list.
     * Configures the layout manager, adapter, and item buttons for the RecyclerView.
     * The visibility of the delete product button depends on the user role.
     */
    private void setUpRecyclerView() {
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<Category> categories = CategorySorter.sortCategoriesByProduct(Objects.requireNonNull(toBuyProductsListViewModel.getToBuyProducts().getValue()));
        categoryAdapter = new CategoryAdapter(categories, R.layout.item_trzeba_kupic, R.layout.item_category_lista);
        categoryAdapter.setUpItemButtons(new SetupItemButtons() {
            @Override
            public void setupDeleteProductButtonClick(View v, Product product) {
                String role = prefs.getString("role", "");
                if(role.equals("ROLE_ADMIN")) {
                    v.setOnClickListener(view -> new DeleteProductPopUp(requireContext(),
                            view1 -> toBuyProductsListViewModel.deleteProductFromList(product)).show());
                }
                else{
                    //set visibility none
                    v.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void setupCheckShoppingListButtonClick(View v, Product product) {
                v.setOnClickListener(view -> toBuyProductsListViewModel.moveProductToBought(product));
            }

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
        toBuyProductsListViewModel.getUserInfo().observe(getViewLifecycleOwner(), userInfo -> {
        });

        // get to buy products
        toBuyProductsListViewModel.getToBuyProducts().observe(getViewLifecycleOwner(), toBuyProducts ->{
                    if(toBuyProducts.isEmpty()){
                        fragmentTrzebaKupicBinding.textViewTrzebaKupic.setText(R.string.wprowadzenie_lista_zakupow_trzeba_kupic);
                    }
                    else{
                        fragmentTrzebaKupicBinding.textViewTrzebaKupic.setText("");
                    }
                    categoryAdapter.setCategoriesList(CategorySorter.sortCategoriesByProduct(toBuyProducts));
                });

        // get response message
        toBuyProductsListViewModel.getResponseMessage().observe(getViewLifecycleOwner(), responseMessage ->
                Toast.makeText(requireActivity(), responseMessage, Toast.LENGTH_SHORT).show());

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