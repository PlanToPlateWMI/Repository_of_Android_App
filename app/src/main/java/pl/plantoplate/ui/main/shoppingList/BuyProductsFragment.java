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

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentTrzebaKupicBinding;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.ProductsDbaseFragment;

public class BuyProductsFragment extends Fragment {

    private FragmentTrzebaKupicBinding fragmentTrzebaKupicBinding;
    private FloatingActionButton plus_in_trzeba_kupic;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentTrzebaKupicBinding = FragmentTrzebaKupicBinding.inflate(inflater, container, false);
        plus_in_trzeba_kupic = fragmentTrzebaKupicBinding.plusInTrzebaKupic;
        plus_in_trzeba_kupic.setOnClickListener(v -> replaceFragment(new ProductsDbaseFragment()));
        return fragmentTrzebaKupicBinding.getRoot();
        //return inflater.inflate(R.layout.fragment_trzeba_kupic, container, false);
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