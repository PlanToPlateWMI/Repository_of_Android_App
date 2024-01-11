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
package pl.plantoplate.ui.main.calendar.meal_info;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.meal.MealPlan;
import pl.plantoplate.data.remote.models.recipe.Ingredient;
import pl.plantoplate.data.remote.models.recipe.RecipeInfo;
import pl.plantoplate.databinding.FragmentItemRecipeInsideForCalendarBinding;
import pl.plantoplate.ui.custom_views.RadioGridGroup;
import pl.plantoplate.ui.main.calendar.meal_info.popups.QuestionCookRecipe;
import pl.plantoplate.ui.main.calendar.meal_info.popups.QuestionDeleteRecipe;
import pl.plantoplate.ui.main.calendar.meal_info.view_models.MealInfoViewModel;
import timber.log.Timber;

/**
 * Fragment class for displaying meal information.
 */
public class MealInfoFragment extends Fragment{

    private MealInfoViewModel mealInfoViewModel;
    private ViewPager2 viewPager2;
    private RadioGridGroup radioGridGroup;
    private ImageView recipeImage, recipeInfo, recipeInfoFake;
    private TextView recipeTitle, recipeTime, recipePortions, recipeLevel;
    private PopupMenu popupMenu;
    private PopupMenu popupMenuInfo;
    private String sourceLink = "http://google.com";
    private SharedPreferences prefs;

    private FloatingActionButton buttonIngredience;
    private FloatingActionButton buttonCook;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentItemRecipeInsideForCalendarBinding fragmentItemRecipeInsideForCalendarBinding =
                FragmentItemRecipeInsideForCalendarBinding.inflate(inflater, container, false);

        prefs = requireActivity().getSharedPreferences("prefs", 0);

        initViews(fragmentItemRecipeInsideForCalendarBinding);
        setupViewModel();
        setupNavigation();
        setupViewPager(viewPager2);
        return fragmentItemRecipeInsideForCalendarBinding.getRoot();
    }

    /**
     * Sets up the views and their listeners.
     */
    public void initViews(FragmentItemRecipeInsideForCalendarBinding fragmentItemRecipeInsideForCalendarBinding){
        recipeImage = fragmentItemRecipeInsideForCalendarBinding.recipeImage;
        recipeTitle = fragmentItemRecipeInsideForCalendarBinding.textNazwaPrzepisu;
        recipeTime = fragmentItemRecipeInsideForCalendarBinding.timeText;
        recipePortions = fragmentItemRecipeInsideForCalendarBinding.personText;
        recipeLevel = fragmentItemRecipeInsideForCalendarBinding.levelText;
        viewPager2 = fragmentItemRecipeInsideForCalendarBinding.viewPagerRecipeInside;
        radioGridGroup = fragmentItemRecipeInsideForCalendarBinding.radioGroupRecipeInside;
        recipeInfo = fragmentItemRecipeInsideForCalendarBinding.info;
        recipeInfoFake = fragmentItemRecipeInsideForCalendarBinding.infoFake;
        buttonIngredience = fragmentItemRecipeInsideForCalendarBinding.plusIng;
        buttonCook = fragmentItemRecipeInsideForCalendarBinding.plusPrzepis;

        setupPopUpMenuInfo(recipeInfoFake);

        recipeInfo.setOnClickListener(v -> popupMenuInfo.show());
        buttonCook.setOnClickListener(v -> {
            MealPlan mealPlan = new MealPlan();
            RecipeInfo recipeInfo = mealInfoViewModel.getMealInfo().getValue();
            mealPlan.setPortions(recipeInfo.getPortions());
            mealPlan.setPortions(Optional.of(recipeInfo.getPortions()).orElse(1));
            mealPlan.setIngredientsIds((ArrayList<Integer>) recipeInfo.getIngredients()
                    .stream().map(Ingredient::getId).collect(Collectors.toList()));

            Timber.e(mealPlan.toString());
            QuestionCookRecipe questionCookRecipe = new QuestionCookRecipe();
            Bundle bundle = new Bundle();
            bundle.putInt("mealId", recipeInfo.getId());
            questionCookRecipe.setArguments(bundle);
            questionCookRecipe.setOnAcceptClickListener(v1 ->
                    getParentFragmentManager().popBackStack());
            questionCookRecipe.show(getChildFragmentManager(), "QuestionCookRecipe");
        });
        buttonIngredience.setOnClickListener(v -> {
            MealPlan mealPlan = new MealPlan();
            RecipeInfo recipeInfo = mealInfoViewModel.getMealInfo().getValue();
            mealPlan.setPortions(recipeInfo.getPortions());
            mealPlan.setIngredientsIds((ArrayList<Integer>) recipeInfo.getIngredients()
                    .stream().map(Ingredient::getId).collect(Collectors.toList()));

            Timber.e(mealPlan.toString());
            mealPlan.setRecipeId(recipeInfo.getId());
            showPopUpDeleteRecipe(mealPlan);
        });
    }

    /**
     * Sets up Pop Up Menu for deleting recipe from calendar.
     */
    public void setupPopUpMenuInfo(View view) {
        popupMenuInfo = new PopupMenu(requireContext(), view, Gravity.END);
        popupMenuInfo.getMenuInflater().inflate(R.menu.info_menu, popupMenuInfo.getMenu());
        popupMenuInfo.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.info_menu){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sourceLink));
                startActivity(browserIntent);
                return true;
            }
            return false;
        });
    }

    public void setupPopUpMenu(View view) {
        popupMenu = new PopupMenu(requireContext(), view, Gravity.END);
        popupMenu.getMenuInflater().inflate(R.menu.recipe_menu_calendar, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            MealPlan mealPlan = new MealPlan();
            RecipeInfo recipeInfo = mealInfoViewModel.getMealInfo().getValue();
            mealPlan.setPortions(recipeInfo.getPortions());
            mealPlan.setIngredientsIds((ArrayList<Integer>) recipeInfo.getIngredients()
                    .stream().map(Ingredient::getId).collect(Collectors.toList()));
            if(item.getItemId() == R.id.przygotowany_przepis) {
                Timber.e(mealPlan.toString());
                QuestionCookRecipe questionCookRecipe = new QuestionCookRecipe();
                Bundle bundle = new Bundle();
                bundle.putInt("mealId", recipeInfo.getId());
                questionCookRecipe.setArguments(bundle);
                questionCookRecipe.setOnAcceptClickListener(v ->
                        getParentFragmentManager().popBackStack());
                questionCookRecipe.show(getChildFragmentManager(), "QuestionCookRecipe");
                return true;
            } else if(item.getItemId() == R.id.usun_przepis) {
                Timber.e(mealPlan.toString());
                mealPlan.setRecipeId(recipeInfo.getId());
                showPopUpDeleteRecipe(mealPlan);
                return true;
            }
            return false;
        });
    }


    public void showPopUpDeleteRecipe(MealPlan mealPlan){
        QuestionDeleteRecipe questionDeleteRecipe =
                new QuestionDeleteRecipe();
        Bundle bundle = new Bundle();
        bundle.putInt("mealId", mealPlan.getRecipeId());
        questionDeleteRecipe.setArguments(bundle);

        questionDeleteRecipe.setOnAcceptButtonClickListener(v ->
                getParentFragmentManager().popBackStack());

        questionDeleteRecipe.show(getChildFragmentManager(), "QuestionDeleteRecipe");
    }

    /**
     * Sets up the view model and its observers.
     */
    public void setupViewModel(){
        HashMap<String, String> recipeLevelMapping = new HashMap<>();
        recipeLevelMapping.put("HARD", "Trudny");
        recipeLevelMapping.put("MEDIUM", "Średni");
        recipeLevelMapping.put("EASY", "Łatwy");

        mealInfoViewModel = new ViewModelProvider(this).get(MealInfoViewModel.class);
        mealInfoViewModel.getMealInfo().observe(getViewLifecycleOwner(), mealInfo -> {
            String imageUrl = mealInfo.getImage();
            if (imageUrl != null) {
                Picasso.get().load(imageUrl).into(recipeImage);
            } else {
                Picasso.get().load(R.drawable.noimage).into(recipeImage);
            }
            recipeTitle.setText(mealInfo.getTitle());
            recipeTime.setText(mealInfo.getTime() + " min.");
            recipePortions.setText(mealInfo.getPortions() + " os.");
            recipeLevel.setText(recipeLevelMapping.get(mealInfo.getLevel().name()));
            sourceLink = mealInfo.getSource();
            if (sourceLink == null){
                recipeInfo.setEnabled(false);
                recipeInfo.setVisibility(View.INVISIBLE);
            }
        });
        mealInfoViewModel.getResponseMessage().observe(getViewLifecycleOwner(),
                responseMessage -> Toast.makeText(getContext(), responseMessage, Toast.LENGTH_SHORT).show());
        mealInfoViewModel.fetchMealInfo(requireArguments().getInt("mealId"));
    }

    /**
     * Sets up navigation between IngredientsFragment and CookFragment.
     */
    private void setupNavigation() {
        radioGridGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.skladniki_button) {
                viewPager2.setCurrentItem(0);
            } else {
                viewPager2.setCurrentItem(1);
            }
        });
    }

    /**
     * Sets up ViewPager2 with two fragments: IngredientsFragment and CookFragment.
     */
    private void setupViewPager(ViewPager2 viewPager) {
        String role = prefs.getString("role", "");

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0 && role.equals("ROLE_ADMIN")) {
                    recipeInfo.setVisibility(View.INVISIBLE);
                    recipeInfoFake.setVisibility(View.INVISIBLE);
                    buttonIngredience.setVisibility(View.VISIBLE);
                    buttonCook.setVisibility(View.INVISIBLE);
                    radioGridGroup.setCheckedRadioButtonById(R.id.skladniki_button);
                } else if (position == 1 && role.equals("ROLE_ADMIN")) {
                    recipeInfo.setVisibility(sourceLink == null ? View.INVISIBLE : View.VISIBLE);
                    recipeInfoFake.setVisibility(sourceLink == null ? View.INVISIBLE : View.VISIBLE);
                    buttonIngredience.setVisibility(View.INVISIBLE);
                    buttonCook.setVisibility(View.VISIBLE);
                    radioGridGroup.setCheckedRadioButtonById(R.id.przepis_button);
                } else {
                    buttonIngredience.setVisibility(View.INVISIBLE);
                    buttonCook.setVisibility(View.INVISIBLE);
                    if(position == 0) {
                        radioGridGroup.setCheckedRadioButtonById(R.id.skladniki_button);
                        recipeInfo.setVisibility(View.VISIBLE);
                        recipeInfoFake.setVisibility(View.VISIBLE);
                    } else{
                        radioGridGroup.setCheckedRadioButtonById(R.id.przepis_button);
                        recipeInfo.setVisibility(View.VISIBLE);
                        recipeInfoFake.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        adapter.addFragment(new MealIngredientsFragment());
        adapter.addFragment(new MealStepsFragment());
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
