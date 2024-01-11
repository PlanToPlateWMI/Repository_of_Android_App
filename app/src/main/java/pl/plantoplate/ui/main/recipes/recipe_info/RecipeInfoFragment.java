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
package pl.plantoplate.ui.main.recipes.recipe_info;

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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.meal.MealPlanNew;
import pl.plantoplate.data.remote.models.meal.MealPlan;
import pl.plantoplate.databinding.FragmentItemRecipeInsideBinding;
import pl.plantoplate.ui.custom_views.RadioGridGroup;
import pl.plantoplate.ui.main.recipes.recipe_info.events.IngredientsChangeEvent;
import pl.plantoplate.ui.main.recipes.recipe_info.new_popups.PopUpCalendarPlanningStart;
import pl.plantoplate.ui.main.recipes.recipe_info.new_popups.PopUpShoppingStart;
import pl.plantoplate.ui.main.recipes.recipe_info.view_models.RecipeInfoViewModel;
import timber.log.Timber;

public class RecipeInfoFragment extends Fragment {

    private ViewPager2 viewPager2;
    private RadioGridGroup radioGridGroup;

    private ImageView recipeImage;
    private ImageView questionImageView;
    private ImageView questionImageViewFake;
    private ImageView infoImageView;
    private ImageView infoImageViewFake;
    private TextView recipeTitle;
    private TextView recipeTime;
    private TextView recipePortions;
    private TextView recipeLevel;
    private PopupMenu popupMenu;
    private PopupMenu menu;
    private PopupMenu menuInfo;
    private List<Integer> ingredientsIds;
    private String sourceLink = "http://google.com";
    private SharedPreferences prefs;
    private FloatingActionButton buttonIngredience;
    private FloatingActionButton buttonCook;

    public RecipeInfoFragment() {
        // Required empty public constructor
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

    @Subscribe
    public void onMessageEvent(IngredientsChangeEvent ingredientsChangeEvent) {
        this.ingredientsIds = ingredientsChangeEvent.getData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentItemRecipeInsideBinding fragmentItemRecipeInsideBinding =
                FragmentItemRecipeInsideBinding.inflate(inflater, container, false);
        prefs = requireActivity().getSharedPreferences("prefs", 0);

        initViews(fragmentItemRecipeInsideBinding);
        setupViewModel();
        setupNavigation();
        setupViewPager(viewPager2);
        return fragmentItemRecipeInsideBinding.getRoot();
    }

    public void initViews(FragmentItemRecipeInsideBinding fragmentItemRecipeInsideBinding){
        recipeImage = fragmentItemRecipeInsideBinding.recipeImage;
        recipeTitle = fragmentItemRecipeInsideBinding.textNazwaPrzepisu;
        recipeTime = fragmentItemRecipeInsideBinding.timeText;
        recipePortions = fragmentItemRecipeInsideBinding.personText;
        recipeLevel = fragmentItemRecipeInsideBinding.levelText;
        viewPager2 = fragmentItemRecipeInsideBinding.viewPagerRecipeInside;
        radioGridGroup = fragmentItemRecipeInsideBinding.radioGroupRecipeInside;
        questionImageView = fragmentItemRecipeInsideBinding.question;
        questionImageViewFake = fragmentItemRecipeInsideBinding.questionFake;
        infoImageView = fragmentItemRecipeInsideBinding.info;
        infoImageViewFake = fragmentItemRecipeInsideBinding.infoFake;
        buttonIngredience = fragmentItemRecipeInsideBinding.plusIng;
        buttonCook = fragmentItemRecipeInsideBinding.plusPrzepis;

        setupPopUpMenuForImage(questionImageViewFake);
        setupPopUpMenuForImageInfo(infoImageViewFake);

        questionImageView.setOnClickListener(v -> menu.show());
        infoImageView.setOnClickListener(v -> menuInfo.show());
        buttonIngredience.setOnClickListener(v -> {
            MealPlan mealPlan = new MealPlan();
            mealPlan.setIngredientsIds(ingredientsIds);
            mealPlan.setRecipeId(requireArguments().getInt("recipeId"));

            Timber.e(mealPlan.toString());
            MealPlanNew addMealProducts = new MealPlanNew();
            addMealProducts.setRecipeId(requireArguments().getInt("recipeId"));
            addMealProducts.setIngredientsIds(ingredientsIds);
            PopUpShoppingStart popUpControl = new PopUpShoppingStart(addMealProducts);
            popUpControl.show(getChildFragmentManager(), "PopUpShoppingStart");

        });
        buttonCook.setOnClickListener(v -> {
            MealPlan mealPlan = new MealPlan();
            mealPlan.setIngredientsIds(ingredientsIds);
            mealPlan.setRecipeId(requireArguments().getInt("recipeId"));

            Timber.e(mealPlan.toString());
            MealPlanNew addMealProducts = new MealPlanNew();
            addMealProducts.setRecipeId(requireArguments().getInt("recipeId"));
            addMealProducts.setIngredientsIds(ingredientsIds);
            PopUpCalendarPlanningStart popUpControl = new PopUpCalendarPlanningStart(addMealProducts);
            popUpControl.show(getChildFragmentManager(), "PopUpCalendarStart");

        });
    }

    public void setupPopUpMenu(View view) {
        popupMenu = new PopupMenu(requireContext(), view, Gravity.END);
        popupMenu.getMenuInflater().inflate(R.menu.recipe_inside_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(item -> {
            MealPlan mealPlan = new MealPlan();
            mealPlan.setIngredientsIds(ingredientsIds);
            mealPlan.setRecipeId(requireArguments().getInt("recipeId"));
            if(item.getItemId() == R.id.lista_plan){
                Timber.e(mealPlan.toString());
                MealPlanNew addMealProducts = new MealPlanNew();
                addMealProducts.setRecipeId(requireArguments().getInt("recipeId"));
                addMealProducts.setIngredientsIds(ingredientsIds);
                PopUpShoppingStart popUpControl = new PopUpShoppingStart(addMealProducts);
                popUpControl.show(getChildFragmentManager(), "PopUpShoppingStart");
                return true;
            } else if(item.getItemId() == R.id.plan_kalendarz) {
                Timber.e(mealPlan.toString());
                MealPlanNew addMealProducts = new MealPlanNew();
                addMealProducts.setRecipeId(requireArguments().getInt("recipeId"));
                addMealProducts.setIngredientsIds(ingredientsIds);
                PopUpCalendarPlanningStart popUpControl = new PopUpCalendarPlanningStart(addMealProducts);
                popUpControl.show(getChildFragmentManager(), "PopUpCalendarStart");
                return true;
            }
            return false;
        });
    }

    public void setupPopUpMenuForImage(View view) {
        menu = new PopupMenu(requireContext(), view, Gravity.END);
        menu.getMenuInflater().inflate(R.menu.question_menu, menu.getMenu());
    }

    public void setupPopUpMenuForImageInfo(View view) {
        menuInfo = new PopupMenu(requireContext(), view, Gravity.END);
        menuInfo.getMenuInflater().inflate(R.menu.info_menu, menuInfo.getMenu());
        menuInfo.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.info_menu){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(sourceLink));
                startActivity(browserIntent);
                return true;
            }
            return false;
        });
    }

    public void setupViewModel(){
        HashMap<String, String> recipeLevelMapping = new HashMap<>();
        recipeLevelMapping.put("HARD", "Trudny");
        recipeLevelMapping.put("MEDIUM", "Średni");
        recipeLevelMapping.put("EASY", "Łatwy");

        RecipeInfoViewModel recipeInfoViewModel = new ViewModelProvider(this).get(RecipeInfoViewModel.class);
        recipeInfoViewModel.getRecipeInfo().observe(getViewLifecycleOwner(), recipeInfo -> {
            String imageUrl = recipeInfo.getImage();
            if (imageUrl != null) {
                Picasso.get().load(imageUrl).into(recipeImage);
            } else {
                Picasso.get().load(R.drawable.noimage).into(recipeImage);
            }
            recipeTitle.setText(recipeInfo.getTitle());
            recipeTime.setText(recipeInfo.getTime() + " min.");
            recipePortions.setText(recipeInfo.getPortions() + " os.");
            recipeLevel.setText(recipeLevelMapping.get(recipeInfo.getLevel().name()));
            sourceLink = recipeInfo.getSource();
            if (sourceLink == null){
                infoImageView.setEnabled(false);
                infoImageView.setVisibility(View.INVISIBLE);
            }
        });
        recipeInfoViewModel.getResponseMessage().observe(getViewLifecycleOwner(),
                responseMessage -> Toast.makeText(getContext(), responseMessage, Toast.LENGTH_SHORT).show());
        recipeInfoViewModel.fetchRecipeInfo(requireArguments().getInt("recipeId"));
    }

    private void setupNavigation() {
        radioGridGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.skladniki_button) {
                viewPager2.setCurrentItem(0);
            } else {
                viewPager2.setCurrentItem(1);
            }
        });
    }

    private void setupViewPager(ViewPager2 viewPager) {
        String role = prefs.getString("role", "");

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0 && role.equals("ROLE_ADMIN")) {
                    questionImageView.setVisibility(View.VISIBLE);
                    questionImageViewFake.setVisibility(View.VISIBLE);
                    infoImageView.setVisibility(View.INVISIBLE);
                    infoImageViewFake.setVisibility(View.INVISIBLE);
                    buttonIngredience.setVisibility(View.VISIBLE);
                    buttonCook.setVisibility(View.INVISIBLE);
                    radioGridGroup.setCheckedRadioButtonById(R.id.skladniki_button);
                } else if (position == 1 && role.equals("ROLE_ADMIN")) {
                    questionImageView.setVisibility(View.INVISIBLE);
                    questionImageViewFake.setVisibility(View.INVISIBLE);
                    infoImageView.setVisibility(sourceLink == null ? View.INVISIBLE : View.VISIBLE);
                    infoImageViewFake.setVisibility(sourceLink == null ? View.INVISIBLE : View.VISIBLE);
                    buttonIngredience.setVisibility(View.INVISIBLE);
                    buttonCook.setVisibility(View.VISIBLE);
                    radioGridGroup.setCheckedRadioButtonById(R.id.przepis_button);
                } else {
                    questionImageView.setVisibility(View.INVISIBLE);
                    questionImageViewFake.setVisibility(View.INVISIBLE);
                    buttonIngredience.setVisibility(View.INVISIBLE);
                    buttonCook.setVisibility(View.INVISIBLE);
                    if(position == 0) {
                        radioGridGroup.setCheckedRadioButtonById(R.id.skladniki_button);
                        infoImageView.setVisibility(View.INVISIBLE);
                        infoImageViewFake.setVisibility(View.INVISIBLE);
                    } else{
                        radioGridGroup.setCheckedRadioButtonById(R.id.przepis_button);
                        infoImageView.setVisibility(View.VISIBLE);
                        infoImageViewFake.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        adapter.addFragment(new RecipeIngredientsFragment());
        adapter.addFragment(new RecipeStepsFragment());
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