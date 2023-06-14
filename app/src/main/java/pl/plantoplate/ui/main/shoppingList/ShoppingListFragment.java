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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentShoppingListBinding;
import pl.plantoplate.tools.CategorySorter;
import pl.plantoplate.ui.main.shoppingList.listAdapters.category.CategoryAdapter;
import pl.plantoplate.ui.main.shoppingList.viewModels.ShoppingListViewModel;

/**
 * Fragment for shopping list.
 */
public class ShoppingListFragment extends Fragment {

    private FragmentShoppingListBinding shopping_list_view;
    private SharedPreferences prefs;
    private ViewPager2 viewPager;

    /**
     * Method called on fragment start initialization.
     * Method initialize SharedPreferences object and set
     * selected all products fragment by default on restart fragment.
     */
    @Override
    public void onStart() {
        super.onStart();

        // Get the SharedPreferences object
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        // Set selected all products fragment by default on restart fragment.
        shopping_list_view.bottomNavigationView2.setSelectedItemId(R.id.trzeba_kupic);
        // Set first visible AllProductsFragment by default
        viewPager.setCurrentItem(0);
    }

    /**
     * Method called on fragment view creation.
     * Method initialize fragment view and setup swipe pager and bottom navigation.
     *
     * @param inflater layout inflater that can be used to inflate any views in the fragment.
     * @param container view group container that will contain the fragment.
     * @param savedInstanceState saved instance state of fragment.
     * @return root view of fragment.
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        shopping_list_view = FragmentShoppingListBinding.inflate(inflater, container, false);

        // Setup views
        viewPager = shopping_list_view.viewPager;

        // Setup swipe pager
        setupViewPager(viewPager);
        // Setup navigation
        setupNavigation();
        return shopping_list_view.getRoot();
    }

    /**
     * Method called on fragment view creation that setup bottom navigation
     * listener. If user click on bottom navigation item then we change
     * current fragment in swipe pager.
     */
    @SuppressLint("NonConstantResourceId")
    private void setupNavigation() {
        shopping_list_view.bottomNavigationView2.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.trzeba_kupic:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.kupione:
                    viewPager.setCurrentItem(1);
                    return true;
            }
            return false;
        });
    }

    /**
     * Setup swipe pager for shopping list.
     * @param viewPager swipe pager
     */
    private void setupViewPager(ViewPager2 viewPager) {

        // Set up change callback to change bottom navigation item when swipe pager
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
        // Set up adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        adapter.addFragment(new BuyProductsFragment());
        adapter.addFragment(new BoughtProductsFragment());
        viewPager.setAdapter(adapter);
    }

    /**
     * Adapter for swipe pager.
     */
    static class ViewPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();


        public ViewPagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        /**
         * Create fragment for swipe pager.
         *
         * @param position position of fragment
         * @return fragment
         */
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        /**
         * Get count of fragments.
         *
         * @return count of fragments
         */
        @Override
        public int getItemCount() {
            return fragmentList.size();
        }

        /**
         * Add fragment to fragment list.
         *
         * @param fragment fragment
         */
        public void addFragment(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("ShoppingListFragment.onDestroyView");
        shopping_list_view = null;
    }
}
