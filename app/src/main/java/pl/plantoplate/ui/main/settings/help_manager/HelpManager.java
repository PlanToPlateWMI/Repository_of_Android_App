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
package pl.plantoplate.ui.main.settings.help_manager;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentHelpBinding;
import timber.log.Timber;

/**
 * Class responsible for displaying the help manager
 */
public class HelpManager extends Fragment {

    private Button shoppinglist;
    private Button baseOfProducts;
    private Button storageList;
    private Button recipeOne;
    private Button recipeAll;
    private Button calendarRecipe;


    /**
     * Creates the view for the activity.
     * @param inflater The layout inflater
     * @param container The container for the fragment
     * @param savedInstanceState The saved instance state
     * @return The view for this fragment
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentHelpBinding fragmentHelpBinding = FragmentHelpBinding.inflate(inflater,
                container, false);

        initViews(fragmentHelpBinding);
        setClickListeners();
        return fragmentHelpBinding.getRoot();
    }

    /**
     * Initialize the views
     */
    public void initViews(FragmentHelpBinding fragmentHelpBinding) {
        Timber.d("Initializing views...");
        shoppinglist = fragmentHelpBinding.buttonWygenerowanieKodu;
        baseOfProducts = fragmentHelpBinding.buttonZmianaDanych;
        storageList = fragmentHelpBinding.buttonZarzadyanieUyztkownikamu;
        recipeOne = fragmentHelpBinding.buttonAboutUs;
        recipeAll = fragmentHelpBinding.buttonHelp;
        calendarRecipe = fragmentHelpBinding.buttonWyloguj;
    }

    /**
     * Set the click listeners
     */
    public void setClickListeners() {
        shoppinglist.setOnClickListener(v -> showShoppingList());
        baseOfProducts.setOnClickListener(v -> showbaseOfProducts());
        storageList.setOnClickListener(v -> showStorageList());
        recipeOne.setOnClickListener(v -> showRecipeOne());
        recipeAll.setOnClickListener(v -> showRecipeAll());
        calendarRecipe.setOnClickListener(v -> showCalendarRecipe());
    }

    public void showShoppingList() {
        Timber.d("Showing pop up...");
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.quick_start_1);

        ImageView next = dialog.findViewById(R.id.next);
        ImageView previouse = dialog.findViewById(R.id.previouse);
        ImageView closeButton = dialog.findViewById(R.id.close);

        next.setVisibility(View.INVISIBLE);
        previouse.setVisibility(View.INVISIBLE);

        closeButton.setOnClickListener(v -> {
            Timber.d("Closing pop up...");
            dialog.dismiss();
        });

        dialog.show();
    }

    public void showbaseOfProducts() {
        Timber.d("Showing pop up...");
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.quick_start_2);

        ImageView next = dialog.findViewById(R.id.next);
        ImageView previouse = dialog.findViewById(R.id.previouse);
        ImageView closeButton = dialog.findViewById(R.id.close);

        next.setVisibility(View.INVISIBLE);
        previouse.setVisibility(View.INVISIBLE);

        closeButton.setOnClickListener(v -> {
            Timber.d("Closing pop up...");
            dialog.dismiss();
        });

        dialog.show();
    }

    /**
     * Show the storage list
     */
    public void showStorageList() {
        Timber.d("Showing pop up...");
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.quick_start_3);

        ImageView next = dialog.findViewById(R.id.next);
        ImageView previouse = dialog.findViewById(R.id.previouse);
        ImageView closeButton = dialog.findViewById(R.id.close);

        next.setVisibility(View.INVISIBLE);
        previouse.setVisibility(View.INVISIBLE);

        closeButton.setOnClickListener(v -> {
            Timber.d("Closing pop up...");
            dialog.dismiss();
        });

        dialog.show();
    }

    /**
     * Show the recipe one
     */
    public void showRecipeOne() {
        Timber.d("Showing pop up...");
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.quick_start_4);

        ImageView next = dialog.findViewById(R.id.next);
        ImageView previouse = dialog.findViewById(R.id.previouse);
        ImageView closeButton = dialog.findViewById(R.id.close);

        next.setVisibility(View.INVISIBLE);
        previouse.setVisibility(View.INVISIBLE);

        closeButton.setOnClickListener(v -> {
            Timber.d("Closing pop up...");
            dialog.dismiss();
        });

        dialog.show();
    }

    /**
     * Show the recipe all
     */
    public void showRecipeAll() {
        Timber.d("Showing pop up...");
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.quick_start_5);

        ImageView next = dialog.findViewById(R.id.next);
        ImageView previouse = dialog.findViewById(R.id.previouse);
        ImageView closeButton = dialog.findViewById(R.id.close);

        next.setVisibility(View.INVISIBLE);
        previouse.setVisibility(View.INVISIBLE);

        closeButton.setOnClickListener(v -> {
            Timber.d("Closing pop up...");
            dialog.dismiss();
        });

        dialog.show();
    }

    /**
     * Show the calendar recipe
     */
    public void showCalendarRecipe() {
        Timber.d("Showing pop up...");
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.quick_start_6);

        ImageView next = dialog.findViewById(R.id.next);
        ImageView previouse = dialog.findViewById(R.id.previouse);
        ImageView closeButton = dialog.findViewById(R.id.close);

        next.setVisibility(View.INVISIBLE);
        previouse.setVisibility(View.INVISIBLE);

        closeButton.setOnClickListener(v -> {
            Timber.d("Closing pop up...");
            dialog.dismiss();
        });

        dialog.show();
    }
}
