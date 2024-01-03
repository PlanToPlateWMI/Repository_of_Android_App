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
package pl.plantoplate.ui.main.products_database;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
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
import pl.plantoplate.ui.custom_views.RadioGridGroup;
import timber.log.Timber;

/**
 * This fragment is responsible for displaying the products database.
 */
public class ProductsDbaseFragment extends Fragment {

    private static final int ALL_PRODUCTS_FRAGMENT = R.id.wszystkie_button;
    private static final int OWN_PRODUCTS_FRAGMENT = R.id.wlasne_button;
    private ViewPager2 productsDbaseViewPager;
    private RadioGridGroup menuRadioGridGroup;

    @Override
    public void onResume() {
        super.onResume();
        int fragment = productsDbaseViewPager.getCurrentItem();
        productsDbaseViewPager.setCurrentItem(fragment);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentBazaProduktowBinding fragmentBazaProduktowBinding = FragmentBazaProduktowBinding.inflate(inflater, container, false);

        initViews(fragmentBazaProduktowBinding);
        setupViewPager(productsDbaseViewPager);
        setupNavigation();
        return fragmentBazaProduktowBinding.getRoot();
    }

    public void initViews(FragmentBazaProduktowBinding fragmentBazaProduktowBinding){
        productsDbaseViewPager = fragmentBazaProduktowBinding.viewPagerBase;
        menuRadioGridGroup = fragmentBazaProduktowBinding.radioGroupBaza;

        menuRadioGridGroup.setCheckedRadioButtonById(R.id.wszystkie_button);
    }

    /**
     * Method called on fragment view creation that setup bottom navigation
     * listener. If user click on bottom navigation item then we change
     * current fragment in swipe pager.
     */
    @SuppressLint("NonConstantResourceId")
    private void setupNavigation() {
        menuRadioGridGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == ALL_PRODUCTS_FRAGMENT) {
                productsDbaseViewPager.setCurrentItem(0);
            } else if (checkedId == OWN_PRODUCTS_FRAGMENT) {
                productsDbaseViewPager.setCurrentItem(1);
            }else{
                Timber.tag("RadioGridGroup").d("Unhandled ID: %s", checkedId); // Debugging
            }
        });
    }

    private void setupViewPager(ViewPager2 viewPagerBase) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        AllProductsFragment allProductsFragment = new AllProductsFragment();
        OwnProductsFragment ownProductsFragment = new OwnProductsFragment();

        allProductsFragment.setArguments(requireArguments());
        ownProductsFragment.setArguments(requireArguments());

        adapter.addFragment(allProductsFragment);
        adapter.addFragment(ownProductsFragment);
        viewPagerBase.setAdapter(adapter);

        viewPagerBase.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    menuRadioGridGroup.setCheckedRadioButtonById(ALL_PRODUCTS_FRAGMENT);
                else if (position == 1)
                    menuRadioGridGroup.setCheckedRadioButtonById(OWN_PRODUCTS_FRAGMENT);
                else
                    Timber.tag("ViewPager2").d("Unhandled position: %s", position); // Debugging
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