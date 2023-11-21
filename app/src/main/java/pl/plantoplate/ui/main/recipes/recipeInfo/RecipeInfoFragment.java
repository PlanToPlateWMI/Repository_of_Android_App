package pl.plantoplate.ui.main.recipes.recipeInfo;

import android.content.Context;
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
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.meal.Meal;
import pl.plantoplate.data.remote.models.meal.MealPlan;
import pl.plantoplate.databinding.FragmentItemRecipeInsideBinding;
import pl.plantoplate.ui.customViews.RadioGridGroup;
import pl.plantoplate.ui.main.productsDatabase.AddYourOwnProductFragment;
import pl.plantoplate.ui.main.recipes.recipeInfo.events.IngredientsChangeEvent;
import pl.plantoplate.ui.main.recipes.recipeInfo.popUpControl.PopUpControlCalendarStart;
import pl.plantoplate.ui.main.recipes.recipeInfo.popUpControl.PopUpControlShoppingStart;
import pl.plantoplate.ui.main.recipes.recipeInfo.viewModels.RecipeInfoViewModel;
import timber.log.Timber;

public class RecipeInfoFragment extends Fragment {

    private ViewPager2 viewPager2;
    private RadioGridGroup radioGridGroup;
    private ImageView recipeImage, recipeMenu, fakeRecipeMenu, questionImageView, questionImageViewFake,
            infoImageView, infoImageViewFake;
    private TextView recipeTitle, recipeTime, recipePortions, recipeLevel;
    private PopupMenu popupMenu;
    private PopupMenu menu;
    private PopupMenu menuInfo;
    private ArrayList<Integer> ingredientsIds;
    private String sourceLink = "http://google.com";
    SharedPreferences prefs;

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
        recipeMenu = fragmentItemRecipeInsideBinding.menuButton;
        fakeRecipeMenu = fragmentItemRecipeInsideBinding.menuButtonTest;
        questionImageView = fragmentItemRecipeInsideBinding.question;
        questionImageViewFake = fragmentItemRecipeInsideBinding.questionFake;
        infoImageView = fragmentItemRecipeInsideBinding.info;
        infoImageViewFake = fragmentItemRecipeInsideBinding.infoFake;

        setupPopUpMenu(fakeRecipeMenu);
        setupPopUpMenuForImage(questionImageViewFake);
        setupPopUpMenuForImageInfo(infoImageViewFake);

        String role = prefs.getString("role", "");

        if(role.equals("ROLE_ADMIN")) {
            recipeMenu.setVisibility(View.VISIBLE);
            recipeMenu.setOnClickListener(v -> popupMenu.show());
        }else {
            recipeMenu.setVisibility(View.INVISIBLE);
        }

        questionImageView.setOnClickListener(v -> menu.show());
        infoImageView.setOnClickListener(v -> menuInfo.show());
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
        HashMap<String, String> recipeLevelMapping = new HashMap<>() {{
            put("HARD", "Trudny");
            put("MEDIUM", "Średni");
            put("EASY", "Łatwy");
        }};

        RecipeInfoViewModel recipeInfoViewModel = new ViewModelProvider(this).get(RecipeInfoViewModel.class);
        recipeInfoViewModel.getRecipeInfo().observe(getViewLifecycleOwner(), recipeInfo -> {
            Picasso.get().load(recipeInfo.getImage()).into(recipeImage);
            recipeTitle.setText(recipeInfo.getTitle());
            recipeTime.setText(recipeInfo.getTime() + " min.");
            recipePortions.setText(recipeInfo.getPortions() + " os.");
            recipeLevel.setText(recipeLevelMapping.get(recipeInfo.getLevel().name()));
            sourceLink = recipeInfo.getSource();
        });
        recipeInfoViewModel.getResponseMessage().observe(getViewLifecycleOwner(),
                responseMessage -> Toast.makeText(getContext(), responseMessage, Toast.LENGTH_SHORT).show());
        recipeInfoViewModel.fetchRecipeInfo(requireArguments().getInt("recipeId"));

        String role = prefs.getString("role", "");

        if(role.equals("ROLE_ADMIN")) {
            recipeMenu.setVisibility(View.VISIBLE);
            recipeMenu.setOnClickListener(v -> popupMenu.show());
        }else {
            recipeMenu.setVisibility(View.INVISIBLE);
        }
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
                    radioGridGroup.setCheckedRadioButtonById(R.id.skladniki_button);
                } else if (position == 1 && role.equals("ROLE_ADMIN")) {
                    questionImageView.setVisibility(View.INVISIBLE);
                    questionImageViewFake.setVisibility(View.INVISIBLE);
                    infoImageView.setVisibility(View.VISIBLE);
                    infoImageViewFake.setVisibility(View.VISIBLE);
                    radioGridGroup.setCheckedRadioButtonById(R.id.przepis_button);
                } else {
                    questionImageView.setVisibility(View.INVISIBLE);
                    questionImageViewFake.setVisibility(View.INVISIBLE);
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