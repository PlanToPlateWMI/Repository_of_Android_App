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
import android.widget.ImageView;

import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentBazaProduktowBinding;
public class ProductsDbaseFragment extends Fragment {

    private FragmentBazaProduktowBinding bazaProduktowBinding;
    private SearchView searchView;
    private ImageView back;

    private SharedPreferences prefs;

    public ProductsDbaseFragment(String comesFrom) {
        Bundle args = new Bundle();
        args.putString("comesFrom", comesFrom);
        setArguments(args);
    }

    public ProductsDbaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();

        // Get the SharedPreferences object
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        //Set selected all products fragment by default on restart fragment.
        bazaProduktowBinding.bottomNavigationView2.setSelectedItemId(R.id.wszystkie);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        bazaProduktowBinding = FragmentBazaProduktowBinding.inflate(inflater, container, false);

        back = bazaProduktowBinding.back;

        // Get the SearchView
        searchView = bazaProduktowBinding.search;

        bazaProduktowBinding.bottomNavigationView2.setOnItemSelectedListener(item ->{
            switch (item.getItemId()) {
                case R.id.wszystkie:
                    replaceFragment(new AllProductsFragment(requireArguments().getString("comesFrom")));
                    return true;
                case R.id.wlasne:
                    replaceFragment(new OwnProductsFragment(requireArguments().getString("comesFrom")));
                    return true;
            }
            return false;
        });

        back.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return bazaProduktowBinding.getRoot();
    }

    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.baza_def, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}