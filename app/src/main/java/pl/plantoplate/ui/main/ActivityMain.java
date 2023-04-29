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
package com.example.plantoplate.ui.main;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.plantoplate.R;
import com.example.plantoplate.databinding.ActivityMainForFragmentsBinding;
import com.example.plantoplate.ui.main.calendar.CalendarFragment;
import com.example.plantoplate.ui.main.recepies.RecipeFragment;
import com.example.plantoplate.ui.main.settings.SettingsFragment;
import com.example.plantoplate.ui.main.shoplist.ShoppingListFragment;
import com.example.plantoplate.ui.main.storage.StorageFragment;

public class ActivityMain extends AppCompatActivity{

    ActivityMainForFragmentsBinding binding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        binding = ActivityMainForFragmentsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new ShoppingListFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.calendar:
                    replaceFragment(new CalendarFragment());
                    break;
                case R.id.cottage:
                    replaceFragment(new StorageFragment());
                    break;
                case R.id.shopping_cart:
                    replaceFragment(new ShoppingListFragment());
                    break;
                case R.id.receipt_long:
                    replaceFragment(new RecipeFragment());
                    break;
                case R.id.settings:
                    replaceFragment(new SettingsFragment());
                    break;
            }
            return true;

        });
    }

    private void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();

    }

}
