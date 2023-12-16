package pl.plantoplate.ui.main.calendar.mealInfo;

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
import java.util.stream.Collectors;

import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.meal.MealPlan;
import pl.plantoplate.data.remote.models.recipe.Ingredient;
import pl.plantoplate.data.remote.models.recipe.RecipeInfo;
import pl.plantoplate.databinding.FragmentItemRecipeInsideForCalendarBinding;
import pl.plantoplate.ui.customViews.RadioGridGroup;
import pl.plantoplate.ui.main.calendar.mealInfo.popUpControl.PopUpCalendarRecipeControl;
import pl.plantoplate.ui.main.calendar.mealInfo.popUps.QuestionCookRecipe;
import pl.plantoplate.ui.main.calendar.mealInfo.popUps.QuestionDeleteRecipe;
import pl.plantoplate.ui.main.calendar.mealInfo.viewModels.MealInfoViewModel;
import pl.plantoplate.ui.main.recipes.recipeInfo.events.IngredientsChangeEvent;
import pl.plantoplate.ui.main.recipes.recipeInfo.popUpControl.PopUpControlCalendarStart;
import pl.plantoplate.ui.main.recipes.recipeInfo.popUpControl.PopUpControlShoppingStart;
import timber.log.Timber;

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

    private FloatingActionButton button_ingredience;
    private FloatingActionButton button_cook;

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
        button_ingredience = fragmentItemRecipeInsideForCalendarBinding.plusIng;
        button_cook = fragmentItemRecipeInsideForCalendarBinding.plusPrzepis;

        setupPopUpMenuInfo(recipeInfoFake);

        String role = prefs.getString("role", "");

        recipeInfo.setOnClickListener(v -> popupMenuInfo.show());
        button_cook.setOnClickListener(v -> {
            MealPlan mealPlan = new MealPlan();
            RecipeInfo recipeInfo = mealInfoViewModel.getMealInfo().getValue();
            mealPlan.setPortions(recipeInfo.getPortions());
            mealPlan.setIngredientsIds((ArrayList<Integer>) recipeInfo.getIngredients()
                    .stream().map(Ingredient::getId).collect(Collectors.toList()));

            Timber.e(mealPlan.toString());
            QuestionCookRecipe questionCookRecipe = new QuestionCookRecipe();
            Bundle bundle = new Bundle();
            bundle.putInt("mealId", recipeInfo.getId());
            questionCookRecipe.setArguments(bundle);
            questionCookRecipe.setOnAcceptClickListener(v1 -> {
                getParentFragmentManager().popBackStack();
            });
            questionCookRecipe.show(getChildFragmentManager(), "QuestionCookRecipe");
        });
        button_ingredience.setOnClickListener(v -> {
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
                questionCookRecipe.setOnAcceptClickListener(v -> {
                    getParentFragmentManager().popBackStack();
                });
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

        questionDeleteRecipe.setOnAcceptButtonClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });

        questionDeleteRecipe.show(getChildFragmentManager(), "QuestionDeleteRecipe");
    }

    public void setupViewModel(){
        HashMap<String, String> recipeLevelMapping = new HashMap<>() {{
            put("HARD", "Trudny");
            put("MEDIUM", "Średni");
            put("EASY", "Łatwy");
        }};

        mealInfoViewModel = new ViewModelProvider(this).get(MealInfoViewModel.class);
        mealInfoViewModel.getMealInfo().observe(getViewLifecycleOwner(), mealInfo -> {
            Picasso.get().load(mealInfo.getImage()).into(recipeImage);
            recipeTitle.setText(mealInfo.getTitle());
            recipeTime.setText(mealInfo.getTime() + " min.");
            recipePortions.setText(mealInfo.getPortions() + " os.");
            recipeLevel.setText(recipeLevelMapping.get(mealInfo.getLevel().name()));
            sourceLink = mealInfo.getSource();
        });
        mealInfoViewModel.getResponseMessage().observe(getViewLifecycleOwner(),
                responseMessage -> Toast.makeText(getContext(), responseMessage, Toast.LENGTH_SHORT).show());
        mealInfoViewModel.fetchMealInfo(requireArguments().getInt("mealId"));

        String role = prefs.getString("role", "");

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
                    recipeInfo.setVisibility(View.INVISIBLE);
                    recipeInfoFake.setVisibility(View.INVISIBLE);
                    button_ingredience.setVisibility(View.VISIBLE);
                    button_cook.setVisibility(View.INVISIBLE);
                    radioGridGroup.setCheckedRadioButtonById(R.id.skladniki_button);
                } else if (position == 1 && role.equals("ROLE_ADMIN")) {
                    recipeInfo.setVisibility(View.VISIBLE);
                    recipeInfoFake.setVisibility(View.VISIBLE);
                    button_ingredience.setVisibility(View.INVISIBLE);
                    button_cook.setVisibility(View.VISIBLE);
                    radioGridGroup.setCheckedRadioButtonById(R.id.przepis_button);
                } else {
                    button_ingredience.setVisibility(View.INVISIBLE);
                    button_cook.setVisibility(View.INVISIBLE);
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
