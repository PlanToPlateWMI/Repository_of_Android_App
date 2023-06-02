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
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentBazaProduktowBinding;
import pl.plantoplate.ui.main.shoppingList.BoughtProductsFragment;
import pl.plantoplate.ui.main.shoppingList.BuyProductsFragment;
import pl.plantoplate.ui.main.shoppingList.ShoppingListFragment;

public class ProductsDbaseFragment extends Fragment {

    private FragmentBazaProduktowBinding bazaProduktowBinding;
    private SearchView searchView;
    private ImageView back;
    private ViewPager2 viewPagerBase;

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
        viewPagerBase.setCurrentItem(0);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        bazaProduktowBinding = FragmentBazaProduktowBinding.inflate(inflater, container, false);

        viewPagerBase = bazaProduktowBinding.viewPagerBase;
        setupViewPager(viewPagerBase);

        // Get the SearchView
        searchView = bazaProduktowBinding.search;

        back = bazaProduktowBinding.back;

        back.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        bazaProduktowBinding.bottomNavigationView2.setOnItemSelectedListener(item ->{
            switch (item.getItemId()) {
                case R.id.wszystkie:
                    viewPagerBase.setCurrentItem(0);
                    return true;
                case R.id.wlasne:
                    viewPagerBase.setCurrentItem(1);
                    return true;
            }

            return false;
        });

        viewPagerBase.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bazaProduktowBinding.bottomNavigationView2.setSelectedItemId(R.id.wszystkie);
                        break;
                    case 1:
                        bazaProduktowBinding.bottomNavigationView2.setSelectedItemId(R.id.wlasne);
                        break;
                }
            }
        });

        return bazaProduktowBinding.getRoot();
    }

    private void setupViewPager(ViewPager2 viewPagerBase) {
        ProductsDbaseFragment.ViewPagerAdapter adapter = new ProductsDbaseFragment.ViewPagerAdapter(this);
        adapter.addFragment(new AllProductsFragment(requireArguments().getString("comesFrom")));
        adapter.addFragment(new OwnProductsFragment(requireArguments().getString("comesFrom")));
        viewPagerBase.setAdapter(adapter);
    }

    static class ViewPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }


    private void replaceFragment(Fragment fragment) {
        // Start a new fragment transaction and replace the current fragment with the specified fragment
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.baza_def, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }
}