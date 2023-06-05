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


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentAddYourOwnProductBinding;
import pl.plantoplate.databinding.FragmentChangeCategoryBinding;
import pl.plantoplate.ui.customViewes.RadioGridGroup;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.ChangeCategoryListener;
import pl.plantoplate.ui.main.shoppingList.productsDatabase.EditOwnProductFragment;

/**
 * This fragment is used to change category of product.
 */
public class ChangeCategoryOfProductFragment extends Fragment {

    private FragmentChangeCategoryBinding fragmentChangeCategoryBinding;

    private SharedPreferences productPrefs;

    private RadioGridGroup categoriesRadioGroup;
    private Button accept_button;
    private Button cancel_button;

    private ChangeCategoryListener changeCategoryListener;

    public ChangeCategoryOfProductFragment(ChangeCategoryListener changeCategoryListener) {
        this.changeCategoryListener = changeCategoryListener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        fragmentChangeCategoryBinding = FragmentChangeCategoryBinding.inflate(inflater, container, false);

        // Get product from shared preferences
        productPrefs = requireActivity().getSharedPreferences("product", 0);

        // Get references to views
        categoriesRadioGroup = fragmentChangeCategoryBinding.radioGroupNowaKategoria;
        accept_button = fragmentChangeCategoryBinding.buttonZatwierdz;
        cancel_button = fragmentChangeCategoryBinding.buttonAnuluj;

        // Set onClickListeners for buttons
        accept_button.setOnClickListener(v -> applyChanges());
        cancel_button.setOnClickListener(v -> cancelChanges());


        return fragmentChangeCategoryBinding.getRoot();
    }

    public void applyChanges() {
        RadioButton checkedRadioButton = categoriesRadioGroup.getCheckedRadioButton();
        if (checkedRadioButton == null) {
            Snackbar.make(requireActivity().findViewById(R.id.frame_layout), "Wybierz kategorię!", Snackbar.LENGTH_SHORT).show();
            return;
        }

        changeCategoryListener.onCategoryChosen(checkedRadioButton.getText().toString());

        // Close fragment
        getParentFragmentManager().popBackStack();
    }

    public void cancelChanges() {
        // Close fragment
        getParentFragmentManager().popBackStack();
    }


}
