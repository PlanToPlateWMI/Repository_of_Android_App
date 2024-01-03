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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;
import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentShoppingListBinding;
import pl.plantoplate.ui.customViews.RadioGridGroup;
import pl.plantoplate.ui.main.shoppingList.events.ProductBoughtEvent;
import timber.log.Timber;

/**
 * Fragment for shopping list.
 */
public class ShoppingListFragment extends Fragment {

    private ViewPager2 viewPager;
    private RadioGridGroup radioGridGroup;
    public TextView quantityTextView;

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onProductBought(ProductBoughtEvent productBoughtEvent) {
        Timber.e("Product bought event received");
        Integer quantity = productBoughtEvent.productsCount;
        if (quantity > 0) {
            quantityTextView.setVisibility(View.VISIBLE);
            quantityTextView.setText(String.valueOf(quantity));
        } else {
            quantityTextView.setVisibility(View.INVISIBLE);
        }
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
        FragmentShoppingListBinding fragmentShoppingListBinding = FragmentShoppingListBinding.inflate(inflater, container, false);

        initViews(fragmentShoppingListBinding);
        setupViewPager(viewPager);
        setupNavigation();
        return fragmentShoppingListBinding.getRoot();
    }

    /**
     * Method called on fragment view creation that initialize fragment views.
     * @param fragmentShoppingListBinding binding of fragment view.
     */
    private void initViews(FragmentShoppingListBinding fragmentShoppingListBinding) {
        viewPager = fragmentShoppingListBinding.viewPager;
        radioGridGroup = fragmentShoppingListBinding.radioGroup;
        quantityTextView = fragmentShoppingListBinding.quantity;

        radioGridGroup.setCheckedRadioButtonById(R.id.trzeba_kupic_button);
        viewPager.setCurrentItem(0);
    }

    /**
     * Method called on fragment view creation that setup bottom navigation
     * listener. If user click on bottom navigation item then we change
     * current fragment in swipe pager.
     */
    private void setupNavigation() {
        radioGridGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.kupione_button) {
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
                        radioGridGroup.setCheckedRadioButtonById(R.id.trzeba_kupic_button);
                        break;
                    case 1:
                        radioGridGroup.setCheckedRadioButtonById(R.id.kupione_button);
                        break;
                }
            }
        });
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