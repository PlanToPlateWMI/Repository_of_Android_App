package com.example.planetoplate_app;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.planetoplate_app.databinding.ActivityMainForFragmentsBinding;

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
