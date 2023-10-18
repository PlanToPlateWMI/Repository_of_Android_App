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

package pl.plantoplate.ui.main.calendar;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentCalendarBinding;
import pl.plantoplate.ui.customViewes.RadioGridGroup;

/**
 * This fragment is responsible for displaying calendar view.
 */
public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding calendar_view;
    private SharedPreferences prefs;
    private ViewPager2 viewPager;
    private RadioGridGroup radioGridGroup;

    /**
     * Called to create the view hierarchy of the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState A Bundle object containing the saved state of the fragment.
     * @return The root View of the fragment's layout.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        calendar_view = FragmentCalendarBinding.inflate(inflater, container, false);

        // Get the SharedPreferences object
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        // Setup views
        viewPager = calendar_view.kalPrzep;

        // Set selected all products fragment by default on restart fragment.
        radioGridGroup = calendar_view.radioGroupBaza;

        //make radio button checked
        radioGridGroup.setCheckedRadioButtonById(R.id.wszystkie);

        // Set first visible AllProductsFragment by default
        viewPager.setCurrentItem(0);

        // Setup swipe pager
        setupViewPager(viewPager);

        // Setup navigation
        setupNavigation();

        return calendar_view.getRoot();

    }

    /**
     * Method called on fragment view creation that setup bottom navigation
     * listener. If user click on bottom navigation item then we change
     * current fragment in swipe pager.
     */
    @SuppressLint("NonConstantResourceId")
    private void setupNavigation() {
        radioGridGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Log.d("RadioGridGroup", "Checked ID: " + checkedId); // Debugging
            switch (checkedId) {
                case R.id.wszystkie:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.sniadanie:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.obiad:
                    viewPager.setCurrentItem(2);
                    break;
                case R.id.kolacja:
                    viewPager.setCurrentItem(3);
                    break;
                default:
                    Log.d("RadioGridGroup", "Unhandled ID: " + checkedId); // Debugging
                    break;
            }
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
                        calendar_view.radioGroupBaza.setCheckedRadioButtonById(R.id.wszystkie);
                        break;
                    case 1:
                        calendar_view.radioGroupBaza.setCheckedRadioButtonById(R.id.sniadanie);
                        break;
                    case 2:
                        calendar_view.radioGroupBaza.setCheckedRadioButtonById(R.id.obiad);
                        break;
                    case 3:
                        calendar_view.radioGroupBaza.setCheckedRadioButtonById(R.id.kolacja);
                        break;
                }
            }
        });
        // Set up adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        adapter.addFragment(new AllCategoryProductsFragment());
        adapter.addFragment(new BreakfastLunchDinnerCategoryProductsFragment());
        adapter.addFragment(new BreakfastLunchDinnerCategoryProductsFragment());
        adapter.addFragment(new BreakfastLunchDinnerCategoryProductsFragment());
        viewPager.setAdapter(adapter);
    }

    /**
     * Adapter for swipe pager.
     */
    static class ViewPagerAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();

        /**
         * Constructs a new ViewPagerAdapter.
         *
         * @param fragment The fragment associated with the adapter.
         */
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

    /**
     * Called when the view previously created by {@link #onCreateView} has been detached from the fragment.
     * This method is called after {@link #onStop} and before {@link #onDestroy}.
     * It is recommended to unbind any references or resources associated with the view in this method.
     * This method should also nullify any view references to prevent potential memory leaks.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        System.out.println("ShoppingListFragment.onDestroyView");
        calendar_view = null;
    }

}