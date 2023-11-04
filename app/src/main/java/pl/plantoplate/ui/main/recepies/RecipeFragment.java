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
package pl.plantoplate.ui.main.recepies;

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
import pl.plantoplate.databinding.FragmentRecipeBinding;
import pl.plantoplate.ui.customViews.RadioGridGroup;

/**
 * This fragment is responsible for displaying the recipe view.
 */
public class RecipeFragment extends Fragment {
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
        FragmentRecipeBinding fragmentRecipeBinding = FragmentRecipeBinding.inflate(inflater, container, false);

        initViews(fragmentRecipeBinding);

        setupViewPager(viewPager);

        setupNavigation();

        return fragmentRecipeBinding.getRoot();
    }

    public void initViews(FragmentRecipeBinding fragmentRecipeBinding){
        viewPager = fragmentRecipeBinding.viewPagerBase;
        radioGridGroup = fragmentRecipeBinding.radioGroupBaza;
        RadioGridGroup radioGridGroupCategories = fragmentRecipeBinding.radioGroupRecipe;

        radioGridGroup.setCheckedRadioButtonById(R.id.wszystkie_button);
        radioGridGroupCategories.setCheckedRadioButtonById(R.id.wszystkie);
        viewPager.setCurrentItem(0);
        viewPager.setUserInputEnabled(false);
    }

    /**
     * Method called on fragment view creation that setup bottom navigation
     * listener. If user click on bottom navigation item then we change
     * current fragment in swipe pager.
     */
    private void setupNavigation() {
        radioGridGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.ulubione_button) {
                viewPager.setCurrentItem(1);
            } else {
                viewPager.setCurrentItem(0);
            }
        });
    }

    /**
     * Setup swipe pager for shopping list.
     * @param viewPager swipe pager
     */
    private void setupViewPager(ViewPager2 viewPager) {
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGridGroup.setCheckedRadioButtonById(R.id.wszystkie_button);
                        break;
                    case 1:
                        radioGridGroup.setCheckedRadioButtonById(R.id.ulubione_button);
                        break;
                }
            }
        });
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        adapter.addFragment(new AllRecipeFragment());
        adapter.addFragment(new LikeRecipeFragment());
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