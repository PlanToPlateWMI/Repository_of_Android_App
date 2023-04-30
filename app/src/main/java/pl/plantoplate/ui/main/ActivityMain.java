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

import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener;
import com.google.android.material.snackbar.Snackbar;

import pl.plantoplate.R;
import pl.plantoplate.databinding.ActivityMainForFragmentsBinding;
import pl.plantoplate.ui.main.calendar.CalendarFragment;
import pl.plantoplate.ui.main.recepies.RecipeFragment;
import pl.plantoplate.ui.main.settings.SettingsFragment;
import pl.plantoplate.ui.main.shoplist.ShoppingListFragment;
import pl.plantoplate.ui.main.storage.StorageFragment;

public class ActivityMain extends AppCompatActivity implements OnItemSelectedListener {

    private ActivityMainForFragmentsBinding binding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityMainForFragmentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setOnItemSelectedListener(this);
        replaceFragment(new CalendarFragment());

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Snackbar.make(binding.getRoot(), "Item selected", Snackbar.LENGTH_SHORT).show();
        System.out.println("Item selected");

        switch (item.getItemId()){
            case R.id.calendar:
                Snackbar.make(binding.getRoot(), "Calendar", Snackbar.LENGTH_SHORT).show();
                replaceFragment(new CalendarFragment());
                return true;
            case R.id.cottage:
                Snackbar.make(binding.getRoot(), "Cottage", Snackbar.LENGTH_SHORT).show();
                replaceFragment(new StorageFragment());
                return true;
            case R.id.shopping_cart:
                Snackbar.make(binding.getRoot(), "Shopping List", Snackbar.LENGTH_SHORT).show();
                replaceFragment(new ShoppingListFragment());
                return true;
            case R.id.receipt_long:
                Snackbar.make(binding.getRoot(), "Recipes", Snackbar.LENGTH_SHORT).show();
                replaceFragment(new RecipeFragment());
                return true;
            case R.id.settings:
                Snackbar.make(binding.getRoot(), "Settings", Snackbar.LENGTH_SHORT).show();
                replaceFragment(new SettingsFragment());
                return true;
        }
        return false;
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
