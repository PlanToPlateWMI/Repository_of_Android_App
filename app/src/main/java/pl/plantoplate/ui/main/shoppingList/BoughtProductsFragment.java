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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentKupioneBinding;
import pl.plantoplate.requests.RetrofitClient;
import pl.plantoplate.requests.products.DeleteProductCallback;
import pl.plantoplate.requests.products.Product;
import pl.plantoplate.requests.shoppingList.GetShopListCallback;
import pl.plantoplate.requests.shoppingList.ShopListCallback;
import pl.plantoplate.requests.shoppingList.ShoppingList;
import pl.plantoplate.ui.main.shoppingList.listAdapters.OnProductItemClickListener;
import pl.plantoplate.tools.CategorySorter;
import pl.plantoplate.ui.main.shoppingList.listAdapters.product.ProductAdapter;
import retrofit2.Call;

public class BoughtProductsFragment extends Fragment implements ShopListCallback {

    private FragmentKupioneBinding fragmentKupioneBinding;

    private RecyclerView productsRecyclerView;
    private FloatingActionButton moveToStorageButton;

    private SharedPreferences prefs;

    private ArrayList<Product> boughtProductsList;

    @Override
    public void onStart() {
        super.onStart();

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

        setUpRecyclerView();

        return fragmentKupioneBinding.getRoot();
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
        this.boughtProductsList = CategorySorter.sortProductsByName(shopList.getBought());

        // update recycler view
        ProductAdapter productAdapter = (ProductAdapter) productsRecyclerView.getAdapter();
        Objects.requireNonNull(productAdapter).setProductsList(boughtProductsList);
    }

    private void setUpRecyclerView() {
        if (boughtProductsList == null) {
            boughtProductsList = new ArrayList<>();
        }

        productsRecyclerView = fragmentKupioneBinding.categoryRecyclerView;
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ProductAdapter productAdapter = new ProductAdapter(boughtProductsList, R.layout.item_kupione);
        productAdapter.setOnProductItemClickListener(new OnProductItemClickListener() {
            @Override
            public void onDeleteProductButtonClick(View v, Product product) {
                showDeleteProductPopup(product);
            }

            @Override
            public void onCheckShoppingListButtonClick(View v, Product product) {
                System.out.println("Check shopping list button clicked");
            }
        });
        productsRecyclerView.setAdapter(productAdapter);
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
        boughtProductsList.remove(product);

        // update recycler view
        ProductAdapter productAdapter = (ProductAdapter) productsRecyclerView.getAdapter();
        Objects.requireNonNull(productAdapter).setProductsList(boughtProductsList);
    }
}