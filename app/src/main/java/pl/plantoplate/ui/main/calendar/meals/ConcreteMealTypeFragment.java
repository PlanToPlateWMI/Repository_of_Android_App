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
package pl.plantoplate.ui.main.calendar.meals;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.time.LocalDate;
import java.util.ArrayList;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.meal.MealType;
import pl.plantoplate.data.remote.repository.MealRepository;
import pl.plantoplate.databinding.FragmentCalendarInsideBldBinding;
import pl.plantoplate.ui.main.calendar.mealInfo.MealInfoFragment;
import pl.plantoplate.ui.main.calendar.recyclerViews.adapters.ConcreteMealAdapter;
import pl.plantoplate.utils.CategorySorter;
import pl.plantoplate.ui.main.calendar.events.DateSelectedEvent;
import timber.log.Timber;

public class ConcreteMealTypeFragment extends Fragment {

    private MealType mealType;
    private CompositeDisposable compositeDisposable;
    private SharedPreferences prefs;
    private ConcreteMealAdapter concreteMealAdapter;
    private LocalDate date;

    public ConcreteMealTypeFragment(){}

    public ConcreteMealTypeFragment(MealType mealType) {
        this.mealType = mealType;
    }

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

    @Override
    public void onResume() {
        super.onResume();
        getMealsList(date);
    }

    @Subscribe
    public void onDateSelected(DateSelectedEvent event){
        concreteMealAdapter.setMeals(new ArrayList<>());
        date = event.getDate();
        concreteMealAdapter.setMeals(new ArrayList<>());
        getMealsList(event.getDate());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentCalendarInsideBldBinding fragmentCalendarInsideBldBinding =
                FragmentCalendarInsideBldBinding.inflate(inflater, container, false);
        compositeDisposable = new CompositeDisposable();
        prefs = requireActivity().getSharedPreferences("prefs", 0);
        date = LocalDate.now();

        setupRecyclerView(fragmentCalendarInsideBldBinding);
        return fragmentCalendarInsideBldBinding.getRoot();
    }

    public void setupRecyclerView(FragmentCalendarInsideBldBinding fragmentCalendarInsideBldBinding) {
        RecyclerView mealsRecyclerView = fragmentCalendarInsideBldBinding.productsOwnRecyclerView;
        mealsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        concreteMealAdapter = new ConcreteMealAdapter();
        concreteMealAdapter.setUpMealItem((mealId) -> {
            MealInfoFragment mealInfoFragment = new MealInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("mealId", mealId);
            mealInfoFragment.setArguments(bundle);
            replaceFragment(mealInfoFragment);
        });
        mealsRecyclerView.setAdapter(concreteMealAdapter);
    }

    private void getMealsList(LocalDate date) {
        MealRepository mealRepository = new MealRepository();
        compositeDisposable.add(
                mealRepository.getMealsByDate("Bearer " + prefs.getString("token", ""), date)
                        .subscribe(meals -> concreteMealAdapter.setMeals(CategorySorter.getSortedMealTypeList(meals, mealType)),
                                Timber::e)
        );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}