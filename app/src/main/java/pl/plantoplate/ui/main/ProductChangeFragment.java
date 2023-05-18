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

package pl.plantoplate.ui.main;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentProductChangeBinding;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.ChangeCategoryListener;

public class ProductChangeFragment extends Fragment implements ChangeCategoryListener {

    private FragmentProductChangeBinding fragmentProductChangeBinding;

    private Button button_gr;
    private Button button_szt;
    private Button button_l;
    private Button button_kg;

    private Button change_kategory;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        fragmentProductChangeBinding = FragmentProductChangeBinding.inflate(inflater, container, false);

        button_gr = fragmentProductChangeBinding.buttonGr;
        button_gr.findViewById(R.id.button_gr);

        button_szt = fragmentProductChangeBinding.buttonSzt;
        button_szt.findViewById(R.id.button_szt);

        button_kg = fragmentProductChangeBinding.buttonKg;
        button_kg.findViewById(R.id.button_kg);

        button_l = fragmentProductChangeBinding.buttonL;
        button_l.findViewById(R.id.button_l);

        change_kategory = fragmentProductChangeBinding.zmienKategorie;
        change_kategory.findViewById(R.id.zmien_kategorie);

        change_kategory.setOnClickListener(v -> replaceFragment(new ChangeCategoryOfProductFragment()));

        return fragmentProductChangeBinding.getRoot();
        //return inflater.inflate(R.layout.fragment_add_your_own_product, container, false);
    }

    @Override
    public void onCategoryChosen(String category) {

    }

    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}