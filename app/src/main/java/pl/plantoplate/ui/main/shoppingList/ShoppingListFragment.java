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

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentShoppingListBinding;
import pl.plantoplate.requests.RetrofitClient;
import pl.plantoplate.requests.products.Product;
import pl.plantoplate.requests.shoppingList.GetShopListCallback;
import pl.plantoplate.requests.shoppingList.ShopListCallback;
import pl.plantoplate.requests.shoppingList.ShoppingList;
import retrofit2.Call;


public class ShoppingListFragment extends Fragment implements ShopListCallback {

    private FragmentShoppingListBinding shopping_list_view;

    private SharedPreferences prefs;

    private ArrayList<Product> toBuyProductsList;
    private ArrayList<Product> boughtProductsList;

    @Override
    public void onStart() {
        super.onStart();

        // Get the SharedPreferences object
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        // Get products from database
        getShoppingList();

        //Set selected all products fragment by default on restart fragment.
        shopping_list_view.bottomNavigationView2.setSelectedItemId(R.id.trzeba_kupic);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        shopping_list_view = FragmentShoppingListBinding.inflate(inflater, container, false);

        // Set the bottom navigation view listener
        shopping_list_view.bottomNavigationView2.setOnItemSelectedListener(item ->{
            switch (item.getItemId()) {
                case R.id.kupione:
                    replaceFragment(new BoughtProductsFragment(boughtProductsList));
                    return true;
                case R.id.trzeba_kupic:
                    replaceFragment(new BuyProductsFragment(toBuyProductsList));
                    return true;
            }
            return false;
        });

        return shopping_list_view.getRoot();
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

        this.toBuyProductsList = shopList.getToBuy();
        this.boughtProductsList = shopList.getBought();
        replaceFragment(new BuyProductsFragment(toBuyProductsList));
    }

    /**
     * Replace the current fragment with the specified fragment
     * @param fragment The fragment to replace the current fragment with
     */
    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.shopping_list_default, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}