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

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.meal.MealType;
import pl.plantoplate.databinding.FragmentCalendarBinding;
import pl.plantoplate.tools.DateUtils;
import pl.plantoplate.ui.customViews.RadioGridGroup;
import pl.plantoplate.ui.main.calendar.events.DateSelectedEvent;
import pl.plantoplate.ui.main.calendar.recyclerViews.calendar.adapters.CalendarAdapter;
import pl.plantoplate.ui.main.recipes.RecipesFragment;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;
import timber.log.Timber;

/**
 * This fragment is responsible for displaying calendar view.
 */
public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding fragmentCalendarBinding;
    private ViewPager2 viewPager;
    private RadioGridGroup radioGridGroup;
    private FloatingActionButton addToCalendarButton;
    private TextView dateTextView;
    private CalendarAdapter calendarAdapter;

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

        fragmentCalendarBinding = FragmentCalendarBinding.inflate(inflater, container, false);

        initViews(fragmentCalendarBinding);
        setClickListeners();

        setupViewPager(viewPager);
        setupNavigation();
        setupRecyclerView();
        return fragmentCalendarBinding.getRoot();

    }

    public void initViews(FragmentCalendarBinding fragmentCalendarBinding) {
        addToCalendarButton = fragmentCalendarBinding.plusInKalendarz;
        viewPager = fragmentCalendarBinding.kalPrzep;
        dateTextView = fragmentCalendarBinding.miesiacdzienrok;
        radioGridGroup = fragmentCalendarBinding.radioGroupBaza;

        radioGridGroup.setCheckedRadioButtonById(R.id.wszystkie);
        viewPager.setCurrentItem(0);
        setDate();
    }

    public void setDate() {
        String formattedDate = DateUtils.formatPolishDate(LocalDate.now());
        dateTextView.setText(formattedDate);
    }

    public void setClickListeners() {
        addToCalendarButton.setOnClickListener(v -> replaceFragment(new RecipesFragment()));
    }

    /**
     * Method called on fragment view creation that setup bottom navigation
     * listener. If user click on bottom navigation item then we change
     * current fragment in swipe pager.
     */
    private void setupNavigation() {
        radioGridGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Timber.tag("RadioGridGroup").d("Checked ID: %s", checkedId);
            if (checkedId == R.id.wszystkie) {
                viewPager.setCurrentItem(0);
            } else if (checkedId == R.id.sniadanie) {
                viewPager.setCurrentItem(1);
            } else if (checkedId == R.id.obiad) {
                viewPager.setCurrentItem(2);
            } else if (checkedId == R.id.kolacja) {
                viewPager.setCurrentItem(3);
            } else {
                Timber.tag("RadioGridGroup").d("Unhandled ID: %s", checkedId);
            }
        });
    }

    public void setupRecyclerView(){
        RecyclerView recyclerView = fragmentCalendarBinding.kalendarzTutaj;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        calendarAdapter = new CalendarAdapter();
        calendarAdapter.setUpItemButtons(new SetupItemButtons() {
            @Override
            public void setupDateItemClick(View v, LocalDate date) {
                v.setSelected(!v.isSelected());
                EventBus.getDefault().post(new DateSelectedEvent(date));
            }
        });
        recyclerView.setAdapter(calendarAdapter);
        calendarAdapter.setDateList(DateUtils.generateDates());
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
                        radioGridGroup.setCheckedRadioButtonById(R.id.wszystkie);
                        break;
                    case 1:
                        radioGridGroup.setCheckedRadioButtonById(R.id.sniadanie);
                        break;
                    case 2:
                        radioGridGroup.setCheckedRadioButtonById(R.id.obiad);
                        break;
                    case 3:
                        radioGridGroup.setCheckedRadioButtonById(R.id.kolacja);
                        break;
                }
            }
        });
        // Set up adapter
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        adapter.addFragment(new AllMealTypesFragment());
        adapter.addFragment(new ConcreteMealTypeFragment(MealType.BREAKFAST));
        adapter.addFragment(new ConcreteMealTypeFragment(MealType.LUNCH));
        adapter.addFragment(new ConcreteMealTypeFragment(MealType.DINNER));
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

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, fragment);
        transaction.addToBackStack("calendarFragment");
        transaction.commit();
    }
}