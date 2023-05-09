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
package pl.plantoplate.ui.main.shoplist;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pl.plantoplate.R;
import pl.plantoplate.databinding.ActivityMainForFragmentsBinding;
import pl.plantoplate.databinding.FragmentShoppingListBinding;
import pl.plantoplate.ui.main.ActivityMain;

public class ShoppingListFragment extends Fragment {

    private FragmentShoppingListBinding shopping_list_view;

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        shopping_list_view = FragmentShoppingListBinding.inflate(inflater, container, false);
        replaceFragment(new TrzebaKupicFragment());

        shopping_list_view.bottomNavigationView2.setOnItemSelectedListener(item ->{
            switch (item.getItemId()) {
                case R.id.kupione:
                    replaceFragment(new KupioneFragment());
                    return true;
                case R.id.trzeba_kupic:
                    replaceFragment(new TrzebaKupicFragment());
                    return true;
            }
            return false;
        });

        return shopping_list_view.getRoot();
    }

    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.shopping_list_default, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}