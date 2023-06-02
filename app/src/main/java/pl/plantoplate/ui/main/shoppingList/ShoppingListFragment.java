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

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentShoppingListBinding;

public class ShoppingListFragment extends Fragment {

    private FragmentShoppingListBinding shopping_list_view;
    private SharedPreferences prefs;
    private ViewPager2 viewPager;

    @Override
    public void onStart() {
        super.onStart();

        // Get the SharedPreferences object
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        // Set selected all products fragment by default on restart fragment.
        shopping_list_view.bottomNavigationView2.setSelectedItemId(R.id.trzeba_kupic);
        viewPager.setCurrentItem(0);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        shopping_list_view = FragmentShoppingListBinding.inflate(inflater, container, false);

        viewPager = shopping_list_view.viewPager;
        setupViewPager(viewPager);

        shopping_list_view.bottomNavigationView2.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.trzeba_kupic:
                    viewPager.setCurrentItem(0);
                    System.out.println("trzeba kupic");
                    return true;
                case R.id.kupione:
                    viewPager.setCurrentItem(1);
                    System.out.println("kupione");
                    return true;
            }
            return false;
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        shopping_list_view.bottomNavigationView2.setSelectedItemId(R.id.trzeba_kupic);
                        break;
                    case 1:
                        shopping_list_view.bottomNavigationView2.setSelectedItemId(R.id.kupione);
                        break;
                }
            }
        });

        return shopping_list_view.getRoot();
    }

    private void setupViewPager(ViewPager2 viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        adapter.addFragment(new BuyProductsFragment());
        adapter.addFragment(new BoughtProductsFragment());
        viewPager.setAdapter(adapter);
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
