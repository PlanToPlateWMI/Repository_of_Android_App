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
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentBazaProduktowBinding;

/**
 * This fragment is responsible for displaying the products database.
 */
public class ProductsDbaseFragment extends Fragment {

    private FragmentBazaProduktowBinding bazaProduktowBinding;
    private SearchView searchView;
    private ViewPager2 viewPagerBase;

    public ProductsDbaseFragment(String comesFrom) {
        Bundle args = new Bundle();
        args.putString("comesFrom", comesFrom);
        setArguments(args);
    }

    public ProductsDbaseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        int fragment = viewPagerBase.getCurrentItem();
        viewPagerBase.setCurrentItem(fragment);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        bazaProduktowBinding = FragmentBazaProduktowBinding.inflate(inflater, container, false);

        viewPagerBase = bazaProduktowBinding.viewPagerBase;
        // Get the SearchView
        searchView = bazaProduktowBinding.search;

        setupViewPager(viewPagerBase);
        setupBottomNavigationView();

        return bazaProduktowBinding.getRoot();
    }

    @SuppressLint("NonConstantResourceId")
    private void setupBottomNavigationView() {
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
    }

    private void setupViewPager(ViewPager2 viewPagerBase) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        adapter.addFragment(new AllProductsFragment(requireArguments().getString("comesFrom")));
        adapter.addFragment(new OwnProductsFragment(requireArguments().getString("comesFrom")));
        viewPagerBase.setAdapter(adapter);

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
}