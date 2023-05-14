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

package pl.plantoplate.ui.main.shoppingList.productsDatabase;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentBazaProduktowBinding;
import pl.plantoplate.requests.RetrofitClient;
import pl.plantoplate.requests.products.GetProductsDBaseCallback;
import pl.plantoplate.requests.products.Product;
import pl.plantoplate.requests.products.ProductsListCallback;
import retrofit2.Call;

public class ProductsDbaseFragment extends Fragment implements ProductsListCallback {

    private FragmentBazaProduktowBinding bazaProduktowBinding;
    private SearchView searchView;

    private SharedPreferences prefs;

    // Products lists.
    private ArrayList<Product> generalProductsList;
    private ArrayList<Product> groupProductsList;

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        bazaProduktowBinding = FragmentBazaProduktowBinding.inflate(inflater, container, false);

        // Get the SearchView
        searchView = bazaProduktowBinding.search;

        // Get the SharedPreferences object
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        // Get products from database
        getProducts();

        bazaProduktowBinding.bottomNavigationView2.setOnItemSelectedListener(item ->{
            switch (item.getItemId()) {
                case R.id.wszystkie:
                    replaceFragment(new AllProductsFragment(generalProductsList, groupProductsList));
                    return true;
                case R.id.wlasne:
                    replaceFragment(new OwnProductsFragment(groupProductsList));
                    return true;
            }
            return false;
        });

        return bazaProduktowBinding.getRoot();
    }

    private void getProducts() {
        String token = "Bearer " + prefs.getString("token", "");

        Call<ResponseBody> call = RetrofitClient.getInstance().getApi().getProducts(token);

        call.enqueue(new GetProductsDBaseCallback(requireActivity().findViewById(R.id.frame_layout), this));
    }

    @Override
    public void onProductsListsReceived(ArrayList<Product> generalProductsList, ArrayList<Product> groupProductsList) {
        this.generalProductsList = generalProductsList;
        this.groupProductsList = groupProductsList;
        replaceFragment(new AllProductsFragment(generalProductsList, groupProductsList));
    }

    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.baza_def, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}