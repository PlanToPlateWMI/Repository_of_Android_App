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

package pl.plantoplate.ui.main.productsDatabase;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
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
import pl.plantoplate.databinding.FragmentBazaProduktowBinding;
import pl.plantoplate.ui.customViewes.RadioGridGroup;
import timber.log.Timber;

/**
 * This fragment is responsible for displaying the products database.
 */
public class ProductsDbaseFragment extends Fragment {

    private FragmentBazaProduktowBinding bazaProduktowBinding;
    private ViewPager2 viewPagerBase;
    private RadioGridGroup radioGridGroup;

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

        setupViewPager(viewPagerBase);

        // Set selected all products fragment by default on restart fragment.
        radioGridGroup = bazaProduktowBinding.radioGroupBaza;

        //make radio button checked
        radioGridGroup.setCheckedRadioButtonById(R.id.wszystkie_button);

        // Set up the search view
        setupNavigation();
        return bazaProduktowBinding.getRoot();
    }

    /**
     * Method called on fragment view creation that setup bottom navigation
     * listener. If user click on bottom navigation item then we change
     * current fragment in swipe pager.
     */
    @SuppressLint("NonConstantResourceId")
    private void setupNavigation() {
        radioGridGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Timber.tag("RadioGridGroup").d("Checked ID: %s", checkedId); // Debugging
            switch (checkedId) {
                case R.id.wszystkie_button:
                    viewPagerBase.setCurrentItem(0);
                    break;
                case R.id.wlasne_button:
                    viewPagerBase.setCurrentItem(1);
                    break;
                default:
                    Timber.tag("RadioGridGroup").d("Unhandled ID: %s", checkedId); // Debugging
                    break;
            }
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
                        bazaProduktowBinding.radioGroupBaza.setCheckedRadioButtonById(R.id.wszystkie_button);
                        break;
                    case 1:
                        bazaProduktowBinding.radioGroupBaza.setCheckedRadioButtonById(R.id.wlasne_button);
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