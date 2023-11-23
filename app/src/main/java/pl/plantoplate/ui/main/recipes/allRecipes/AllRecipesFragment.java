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
package pl.plantoplate.ui.main.recipes.allRecipes;

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
import pl.plantoplate.databinding.FragmentRecipeBaseBinding;
import pl.plantoplate.ui.customViews.RadioGridGroup;

public class AllRecipesFragment extends Fragment {

    private ViewPager2 viewPager2;
    private RadioGridGroup radioGridGroup;

    public AllRecipesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentRecipeBaseBinding fragmentRecipeBaseBinding =
                FragmentRecipeBaseBinding.inflate(inflater, container, false);

        initViews(fragmentRecipeBaseBinding);
        setupViewPager(viewPager2);
        setupNavigation();
        return fragmentRecipeBaseBinding.getRoot();
    }

    public void initViews(FragmentRecipeBaseBinding fragmentRecipeBaseBinding){
        viewPager2 = fragmentRecipeBaseBinding.viewPagerBase;
        radioGridGroup = fragmentRecipeBaseBinding.radioGroupRecipe;

        radioGridGroup.setCheckedRadioButtonById(R.id.wszystkie);
        viewPager2.setCurrentItem(0);
        viewPager2.setUserInputEnabled(false);
    }

    private void setupNavigation() {
        radioGridGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.dania_glowne){
                viewPager2.setCurrentItem(1);
            } else if (checkedId == R.id.zupy){
                viewPager2.setCurrentItem(2);
            } else if (checkedId == R.id.desery){
                viewPager2.setCurrentItem(3);
            } else if (checkedId == R.id.przekaski){
                viewPager2.setCurrentItem(4);
            } else if (checkedId == R.id.napoje){
                viewPager2.setCurrentItem(5);
            } else {
                viewPager2.setCurrentItem(0);
            }
        });
    }

    private void setupViewPager(ViewPager2 viewPager) {
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radioGridGroup.setCheckedRadioButtonById(R.id.wszystkie);
                        break;
                    case 1:
                        radioGridGroup.setCheckedRadioButtonById(R.id.dania_glowne);
                        break;
                    case 2:
                        radioGridGroup.setCheckedRadioButtonById(R.id.zupy);
                        break;
                    case 3:
                        radioGridGroup.setCheckedRadioButtonById(R.id.desery);
                        break;
                    case 4:
                        radioGridGroup.setCheckedRadioButtonById(R.id.przekaski);
                        break;
                    case 5:
                        radioGridGroup.setCheckedRadioButtonById(R.id.napoje);
                        break;
                }
            }
        });
        AllRecipesFragment.ViewPagerAdapter adapter = new AllRecipesFragment.ViewPagerAdapter(this);
        adapter.addFragment(new RecipeCategoriesAllFragment());
        adapter.addFragment(new ConcreteCategoryAllFragment("Dania główne"));
        adapter.addFragment(new ConcreteCategoryAllFragment("Zupy"));
        adapter.addFragment(new ConcreteCategoryAllFragment("Desery"));
        adapter.addFragment(new ConcreteCategoryAllFragment("Przekąski"));
        adapter.addFragment(new ConcreteCategoryAllFragment("Napoje"));
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false);
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