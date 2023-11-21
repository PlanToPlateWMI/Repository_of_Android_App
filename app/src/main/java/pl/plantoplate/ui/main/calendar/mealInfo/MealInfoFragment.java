package pl.plantoplate.ui.main.calendar.mealInfo;

import android.content.Intent;
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

import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.meal.MealPlan;
import pl.plantoplate.databinding.FragmentItemRecipeInsideForCalendarBinding;
import pl.plantoplate.ui.customViews.RadioGridGroup;
import pl.plantoplate.ui.main.calendar.mealInfo.viewModels.MealInfoViewModel;
import pl.plantoplate.ui.main.recipes.recipeInfo.events.IngredientsChangeEvent;
import pl.plantoplate.ui.main.recipes.recipeInfo.popUpControl.PopUpControlCalendarStart;
import pl.plantoplate.ui.main.recipes.recipeInfo.popUpControl.PopUpControlShoppingStart;
import timber.log.Timber;

public class MealInfoFragment extends Fragment{

    private ViewPager2 viewPager2;
    private RadioGridGroup radioGridGroup;
    private ImageView recipeImage, recipeMenu, fakeRecipeMenu;
    private TextView recipeTitle, recipeTime, recipePortions, recipeLevel;
    private PopupMenu popupMenu;
    private ArrayList<Integer> ingredientsIds;

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
        FragmentItemRecipeInsideForCalendarBinding fragmentItemRecipeInsideForCalendarBinding =
                FragmentItemRecipeInsideForCalendarBinding.inflate(inflater, container, false);

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
        recipeMenu = fragmentItemRecipeInsideForCalendarBinding.menuButton;
        fakeRecipeMenu = fragmentItemRecipeInsideForCalendarBinding.menuButtonTest;

        setupPopUpMenu(fakeRecipeMenu);

        recipeMenu.setOnClickListener(v -> popupMenu.show());
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
                PopUpControlShoppingStart popUpControl = new PopUpControlShoppingStart(getChildFragmentManager(), mealPlan);
                popUpControl.showPopUpNumerOfServingPerRecipe();
                return true;
            } else if(item.getItemId() == R.id.plan_kalendarz) {
                Timber.e(mealPlan.toString());
                PopUpControlCalendarStart popUpControl = new PopUpControlCalendarStart(getChildFragmentManager() , mealPlan);
                popUpControl.showPopUpNumerOfServingPerRecipe();
                return true;
            }
            return false;
        });
    }

    public void setupViewModel(){
        HashMap<String, String> recipeLevelMapping = new HashMap<>() {{
            put("HARD", "Trudny");
            put("MEDIUM", "Średni");
            put("EASY", "Łatwy");
        }};

        MealInfoViewModel mealInfoViewModel = new ViewModelProvider(this).get(MealInfoViewModel.class);
        mealInfoViewModel.getMealInfo().observe(getViewLifecycleOwner(), mealInfo -> {
            Picasso.get().load(mealInfo.getImage()).into(recipeImage);
            recipeTitle.setText(mealInfo.getTitle());
            recipeTime.setText(mealInfo.getTime() + " min.");
            recipePortions.setText(mealInfo.getPortions() + " os.");
            recipeLevel.setText(recipeLevelMapping.get(mealInfo.getLevel().name()));
        });
        mealInfoViewModel.getResponseMessage().observe(getViewLifecycleOwner(),
                responseMessage -> Toast.makeText(getContext(), responseMessage, Toast.LENGTH_SHORT).show());
        mealInfoViewModel.fetchMealInfo(requireArguments().getInt("mealId"));
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
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    radioGridGroup.setCheckedRadioButtonById(R.id.skladniki_button);
                } else {
                    radioGridGroup.setCheckedRadioButtonById(R.id.przepis_button);
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
