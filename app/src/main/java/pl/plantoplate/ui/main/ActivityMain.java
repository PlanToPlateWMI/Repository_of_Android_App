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

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import pl.plantoplate.R;
import pl.plantoplate.databinding.ActivityMainForFragmentsBinding;
import pl.plantoplate.ui.main.calendar.CalendarFragment;
import pl.plantoplate.ui.main.recepies.RecipeFragment;
import pl.plantoplate.ui.main.settings.SettingsFragment;
import pl.plantoplate.ui.main.shoppingList.ShoppingListFragment;
import pl.plantoplate.ui.main.storage.StorageFragment;

/**
 * This is the main activity of the application. It is responsible for displaying the bottom navigation bar and
 * switching between the fragments.
 */
public class ActivityMain extends AppCompatActivity {

    private ActivityMainForFragmentsBinding binding;

    /**
     * Called when the activity is starting. Inflates the layout using view binding
     * and sets the initial fragment to be displayed.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it
     *                           most recently supplied in onSaveInstanceState(Bundle).
     *                           Otherwise, it is null.
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using view binding
        binding = ActivityMainForFragmentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set the navigation item selected listener to this activity
        binding.bottomNavigationView.setOnItemSelectedListener(this::onNavigationItemSelected);

        // Set the initial fragment to be displayed
        if (savedInstanceState == null) {
            binding.bottomNavigationView.setSelectedItemId(R.id.shopping_cart);
        }
    }

    /**
     * Called when a navigation item in the bottom navigation view is selected.
     *
     * @param item The menu item that was selected.
     * @return True if the item was handled, false otherwise.
     */
    @SuppressLint("NonConstantResourceId")
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // Replace the current fragment with the selected fragment based on its ID
        switch (item.getItemId()) {
            case R.id.calendar:
                replaceFragment(new CalendarFragment());
                return true;
            case R.id.cottage:
                replaceFragment(new StorageFragment());
                return true;
            case R.id.shopping_cart:
                replaceFragment(new ShoppingListFragment());
                return true;
            case R.id.receipt_long:
                replaceFragment(new RecipeFragment());
                return true;
            case R.id.settings:
                replaceFragment(new SettingsFragment());
                return true;
        }
        return false;
    }

    /**
     * Replaces the current fragment with the specified fragment.
     *
     * @param fragment The fragment to replace the current fragment with.
     */
    private void replaceFragment(Fragment fragment) {

        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

