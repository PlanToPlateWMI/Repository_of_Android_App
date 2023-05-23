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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentTrzebaKupicBinding;
import pl.plantoplate.requests.BaseCallback;
import pl.plantoplate.requests.RetrofitClient;
import pl.plantoplate.requests.products.DeleteProductCallback;
import pl.plantoplate.requests.products.Product;
import pl.plantoplate.requests.shoppingList.GetShopListCallback;
import pl.plantoplate.requests.shoppingList.ShopListCallback;
import pl.plantoplate.requests.shoppingList.ShoppingList;
import pl.plantoplate.ui.main.shoppingList.listAdapters.OnProductItemClickListener;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.Category;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.CategoryAdapter;
import pl.plantoplate.tools.CategorySorter;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.ProductsDbaseFragment;
import retrofit2.Call;

public class BuyProductsFragment extends Fragment implements ShopListCallback {

    private FragmentTrzebaKupicBinding fragmentTrzebaKupicBinding;

    private FloatingActionButton plus_in_trzeba_kupic;
    private RecyclerView categoryRecyclerView;
    private TextView welcomeTextView;

    private SharedPreferences prefs;

    private ArrayList<Category> toBuyProductsList;

    @Override
    public void onStart() {
        super.onStart();

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

        setUpRecyclerView();
        return fragmentTrzebaKupicBinding.getRoot();
    }

    /**
     * Get the shopping list from the server
     */
    private void getShoppingList() {
        String token = "Bearer " + prefs.getString("token", "");

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().getShoppingList(token);

        call.enqueue(new GetShopListCallback(requireActivity().findViewById(R.id.frame_layout), this, getContext()));
    }

    @Override
    public void onShoppingListReceived(ShoppingList shopList) {
        this.toBuyProductsList = CategorySorter.sortCategoriesByProduct(shopList.getToBuy());

        // print categories and products
        for (Category category : toBuyProductsList) {
            System.out.println(category.getName());
            for (Product product : category.getProducts()) {
                System.out.println(product.getName());
            }
        }

        // update recycler view
        CategoryAdapter categoryAdapter = (CategoryAdapter) categoryRecyclerView.getAdapter();
        Objects.requireNonNull(categoryAdapter).setCategoriesList(toBuyProductsList);
    }

    public void moveProductToBought(Product product){
        String token = "Bearer " + prefs.getString("token", "");
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().changeProductStateInShopList(token, product.getId());

        call.enqueue(new BaseCallback(requireActivity().findViewById(R.id.frame_layout)) {
            @Override
            public void handleSuccessResponse(String response) {
                getShoppingList();
            }

            @Override
            public void handleErrorResponse(int code) {

            }
        });
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

    public void deleteProductFromList(Product product) {
        // send request to delete product from database

        String token = "Bearer " + prefs.getString("token", "");

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().deleteProductFromShopList(token, product.getId());

        call.enqueue(new DeleteProductCallback(requireActivity().findViewById(R.id.frame_layout)));

        // delete product from list
        for (Category category : toBuyProductsList) {
            if (category.getProducts().contains(product)) {
                category.getProducts().remove(product);
                // if category is empty, remove it from list
                if (category.getProducts().isEmpty()) {
                    toBuyProductsList.remove(category);
                }
                break;
            }
        }

        // update recycler view
        CategoryAdapter categoryAdapter = (CategoryAdapter) categoryRecyclerView.getAdapter();
        Objects.requireNonNull(categoryAdapter).setCategoriesList(toBuyProductsList);
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

    public void changeProductAmount(Product product){
        String token = "Bearer " + prefs.getString("token", "");
        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().changeProductAmountInShopList(token, product.getId(), product);

        call.enqueue(new BaseCallback(requireActivity().findViewById(R.id.frame_layout)) {
            @Override
            public void handleSuccessResponse(String response) {
                getShoppingList();
            }

            @Override
            public void handleErrorResponse(int code) {

            }
        });
    }

    public void showAddProductPopup(Product product) {
        Dialog dialog = new Dialog(getContext());
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.pop_up_change_in_product_quantity);

        ImageView plusButton = dialog.findViewById(R.id.plus);
        ImageView minusButton = dialog.findViewById(R.id.minus);
        TextInputEditText quantityTextView = dialog.findViewById(R.id.ilosc);
        ImageView closeButton = dialog.findViewById(R.id.close);
        TextView productUnitTextView = dialog.findViewById(R.id.jednostki_miary_napisac);
        Button acceptButton = dialog.findViewById(R.id.zatwierdzenie);

        quantityTextView.setText("0");
        productUnitTextView.setText(product.getUnit());
        quantityTextView.setText(String.valueOf(product.getAmount()));

        closeButton.setOnClickListener(v -> dialog.dismiss());

        plusButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(Objects.requireNonNull(quantityTextView.getText()).toString());
            quantity++;
            quantityTextView.setText(String.valueOf(quantity));
        });

        minusButton.setOnClickListener(v -> {
            int quantity = Integer.parseInt(Objects.requireNonNull(quantityTextView.getText()).toString());
            if (quantity > 1) {
                quantity--;
                quantityTextView.setText(String.valueOf(quantity));
            }
        });

        acceptButton.setOnClickListener(v -> {
            product.setAmount(Integer.parseInt(Objects.requireNonNull(quantityTextView.getText()).toString()));
            changeProductAmount(product);
            requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new ShoppingListFragment()).commit();
            dialog.dismiss();
        });

        dialog.show();
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