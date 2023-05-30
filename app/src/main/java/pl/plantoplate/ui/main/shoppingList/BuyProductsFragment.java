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
import pl.plantoplate.repository.models.Product;
import pl.plantoplate.repository.models.ShoppingList;
import pl.plantoplate.ui.main.shoppingList.listAdapters.OnProductItemClickListener;
import pl.plantoplate.repository.models.Category;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.CategoryAdapter;
import pl.plantoplate.tools.CategorySorter;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.ProductsDbaseFragment;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.popups.AddToCartPopUp;

public class BuyProductsFragment extends Fragment {

    private FragmentTrzebaKupicBinding fragmentTrzebaKupicBinding;

    private FloatingActionButton plus_in_trzeba_kupic;
    private RecyclerView categoryRecyclerView;
    private TextView welcomeTextView;

    private SharedPreferences prefs;

    private ArrayList<Category> toBuyProductsList;

    private ShoppingListRepository shoppingListRepository;

    @Override
    public void onResume() {
        super.onResume();
        getShoppingList();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentTrzebaKupicBinding = FragmentTrzebaKupicBinding.inflate(inflater, container, false);
        plus_in_trzeba_kupic = fragmentTrzebaKupicBinding.plusInTrzebaKupic;
        plus_in_trzeba_kupic.setOnClickListener(v -> replaceFragment(new ProductsDbaseFragment()));

        // get shared preferences
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        // get shopping list repository
        shoppingListRepository = new ShoppingListRepository();

        setUpRecyclerView();
        return fragmentTrzebaKupicBinding.getRoot();
    }

    public void getShoppingList(){
        String token = "Bearer " + prefs.getString("token", "");

        shoppingListRepository.getToBuyShoppingList(token, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> shopList) {
                toBuyProductsList = CategorySorter.sortCategoriesByProduct(shopList);

                // update recycler view
                CategoryAdapter categoryAdapter = (CategoryAdapter) categoryRecyclerView.getAdapter();
                Objects.requireNonNull(categoryAdapter).setCategoriesList(toBuyProductsList);
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

    public void moveProductToBought(Product product){
        String token = "Bearer " + prefs.getString("token", "");
        shoppingListRepository.changeProductStateInShopList(token, product.getId(), new ResponseCallback<ShoppingList>() {
            @Override
            public void onSuccess(ShoppingList shoppingList) {
                toBuyProductsList = CategorySorter.sortCategoriesByProduct(shoppingList.getToBuy());

                // update recycler view
                CategoryAdapter categoryAdapter = (CategoryAdapter) categoryRecyclerView.getAdapter();
                Objects.requireNonNull(categoryAdapter).setCategoriesList(toBuyProductsList);

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
                toBuyProductsList = CategorySorter.sortCategoriesByProduct(shopList);

                // update recycler view
                CategoryAdapter categoryAdapter = (CategoryAdapter) categoryRecyclerView.getAdapter();
                Objects.requireNonNull(categoryAdapter).setCategoriesList(toBuyProductsList);
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

    public void changeProductAmount(Product product){
        String token = "Bearer " + prefs.getString("token", "");
        shoppingListRepository.changeProductAmountInShopList(token, product.getId(), product, new ResponseCallback<ArrayList<Product>>() {
            @Override
            public void onSuccess(ArrayList<Product> shopList) {
                toBuyProductsList = CategorySorter.sortCategoriesByProduct(shopList);

                // update recycler view
                CategoryAdapter categoryAdapter = (CategoryAdapter) categoryRecyclerView.getAdapter();
                Objects.requireNonNull(categoryAdapter).setCategoriesList(toBuyProductsList);
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

    public void showAddProductPopup(Product product) {
        AddToCartPopUp addToCartPopUp = new AddToCartPopUp(requireContext(), product);
        addToCartPopUp.acceptButton.setOnClickListener(v -> {
            String quantityValue = Objects.requireNonNull(addToCartPopUp.quantity.getText()).toString();
            if (quantityValue.isEmpty()) {
                addToCartPopUp.quantity.setError("Podaj ilość");
                return;
            }
            if (quantityValue.endsWith(".")) {
                // Remove dot at the end
                quantityValue = quantityValue.substring(0, quantityValue.length() - 1);
            }
            float quantity = BigDecimal.valueOf(Float.parseFloat(quantityValue)).setScale(3, RoundingMode.HALF_UP).floatValue();
            product.setAmount(quantity);
            System.out.println(product.getAmount());
            changeProductAmount(product);
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ShoppingListFragment()).commit();
            addToCartPopUp.dismiss();
        });
        addToCartPopUp.show();
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

    private void setUpRecyclerView() {
        if (toBuyProductsList == null) {
            toBuyProductsList = new ArrayList<>();
        }

        categoryRecyclerView = fragmentTrzebaKupicBinding.productsOwnRecyclerView;
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CategoryAdapter categoryAdapter = new CategoryAdapter(toBuyProductsList, R.layout.item_trzeba_kupic);
        categoryAdapter.setOnProductItemClickListener(new OnProductItemClickListener() {
            @Override
            public void onDeleteProductButtonClick(View v, Product product) {
                showDeleteProductPopup(product);
            }

            @Override
            public void onCheckShoppingListButtonClick(View v, Product product) {
                moveProductToBought(product);
            }

            @Override
            public void onProductItemClick(View v, Product product) {
                showAddProductPopup(product);
            }
        });
        categoryRecyclerView.setAdapter(categoryAdapter);
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
        transaction.addToBackStack(null);
        transaction.commit();
    }
}