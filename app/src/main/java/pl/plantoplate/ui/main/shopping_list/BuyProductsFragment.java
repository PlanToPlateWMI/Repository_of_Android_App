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
package pl.plantoplate.ui.main.shopping_list;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.product.ProductCategory;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.databinding.FragmentTrzebaKupicBinding;
import pl.plantoplate.ui.main.recycler_views.adapters.CategoryAdapter;
import pl.plantoplate.ui.main.recycler_views.listeners.SetupItemButtons;
import pl.plantoplate.ui.main.products_database.ProductsDbaseFragment;
import pl.plantoplate.ui.main.popups.DeleteProductPopUp;
import pl.plantoplate.ui.main.popups.ModifyProductPopUp;
import pl.plantoplate.ui.main.shopping_list.view_models.ToBuyProductsListViewModel;

/**
 * This fragment is responsible for displaying the shopping list.
 */
public class BuyProductsFragment extends Fragment {

    private ToBuyProductsListViewModel toBuyProductsListViewModel;
    private FloatingActionButton addToCartButton;
    private RecyclerView categoryRecyclerView;
    private TextView titleTextView;
    private SharedPreferences prefs;
    private CategoryAdapter categoryAdapter;

    @Override
    public void onResume() {
        super.onResume();
        toBuyProductsListViewModel.fetchUserInfo();
        toBuyProductsListViewModel.fetchToBuyProducts();
    }

    /**
     * Method called on fragment view creation.
     * Method initialize fragment view and setup swipe pager and bottom navigation.
     *
     * @param inflater layout inflater that can be used to inflate any views in the fragment.
     * @param container view group container that will contain the fragment.
     * @param savedInstanceState saved instance state of fragment.
     * @return root view of fragment.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentTrzebaKupicBinding fragmentTrzebaKupicBinding = FragmentTrzebaKupicBinding.inflate(inflater, container, false);
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        initViews(fragmentTrzebaKupicBinding);
        setClickListeners();
        setUpViewModel();
        setUpRecyclerView();
        return fragmentTrzebaKupicBinding.getRoot();
    }

    /**
     * Replaces the current fragment with the given one.
     */
    private void initViews(FragmentTrzebaKupicBinding fragmentTrzebaKupicBinding) {
        addToCartButton = fragmentTrzebaKupicBinding.plusInTrzebaKupic;
        categoryRecyclerView = fragmentTrzebaKupicBinding.productsOwnRecyclerView;
        titleTextView = fragmentTrzebaKupicBinding.textViewTrzebaKupic;
    }

    /**
     * Sets up the ViewModel for this fragment.
     * Observes the LiveData for the shopping list and the user info.
     */
    private void setClickListeners() {
        addToCartButton.setOnClickListener(v -> {
            Bundle args = new Bundle();
            args.putString("comesFrom", "shoppingList");
            Fragment fragment = new ProductsDbaseFragment();
            fragment.setArguments(args);
            replaceFragment(fragment);
        });
    }

    /**
     * Shows a popup dialog for modifying the product.
     *
     * @param product The product to be added to the shopping list.
     */
    public void showModifyProductPopup(Product product) {
        ModifyProductPopUp addToCartPopUp = new ModifyProductPopUp(requireContext(), product);
        addToCartPopUp.setOnAcceptButtonClickListener(v -> {
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
        List<ProductCategory> categories = toBuyProductsListViewModel.getToBuyProducts().getValue();
        categoryAdapter = new CategoryAdapter(categories, R.layout.item_trzeba_kupic, R.layout.item_category_lista);
        categoryAdapter.setUpItemButtons(new SetupItemButtons() {
            @Override
            public void setupDeleteProductButtonClick(View v, Product product) {
                String role = prefs.getString("role", "");
                if(role.equals("ROLE_ADMIN")) {
                    v.setOnClickListener(view -> new DeleteProductPopUp(requireContext(),
                            R.layout.new_pop_up_delete_product_from_shopping_list,
                            view1 -> toBuyProductsListViewModel.deleteProductFromList(product)).show());
                }
                else{
                    v.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void setupCheckShoppingListButtonClick(View v, Product product) {
                v.setOnClickListener(view -> toBuyProductsListViewModel.moveProductToBought(product));
            }

            @Override
            public void setupProductItemClick(View v, Product product) {
                v.setOnClickListener(view -> showModifyProductPopup(product));
            }
        });
        categoryRecyclerView.setAdapter(categoryAdapter);
    }

    /**
     * Set up the ViewModel observers for the shopping list.
     * This method observes the changes in the ViewModel and updates the UI accordingly.
     */
    public void setUpViewModel() {
        toBuyProductsListViewModel = new ViewModelProvider(this).get(ToBuyProductsListViewModel.class);
        toBuyProductsListViewModel.getUserInfo().observe(getViewLifecycleOwner(), userInfo -> {
        });
        toBuyProductsListViewModel.getToBuyProducts().observe(getViewLifecycleOwner(), toBuyProducts ->{
                    if(toBuyProducts.isEmpty()){
                        titleTextView.setText(R.string.wprowadzenie_lista_zakupow_trzeba_kupic);
                    }
                    else{
                        titleTextView.setText("");
                    }
                    categoryAdapter.setCategoriesList(toBuyProducts);
                });
        toBuyProductsListViewModel.getResponseMessage().observe(getViewLifecycleOwner(), responseMessage ->
                Toast.makeText(requireActivity(), responseMessage, Toast.LENGTH_SHORT).show());

    }

    /**
     * Replaces the current fragment with the specified fragment
     *
     * @param fragment the fragment to replace the current fragment with
     */
    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack("trzebaKupicFragment");
        transaction.commit();
    }
}