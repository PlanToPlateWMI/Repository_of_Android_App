package pl.plantoplate.ui.main.recipes.recipeInfo;

import android.content.Context;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import pl.plantoplate.R;
import pl.plantoplate.databinding.FragmentItemRecipeInsideBinding;
import pl.plantoplate.ui.customViews.RadioGridGroup;
import pl.plantoplate.ui.main.recipes.recipeInfo.popUpControl.PopUpControlCalendarStart;
import pl.plantoplate.ui.main.recipes.recipeInfo.popUpControl.PopUpControlShoppingStart;
import pl.plantoplate.ui.main.recipes.recipeInfo.viewModels.RecipeInfoViewModel;
import timber.log.Timber;

public class RecipeInfoFragment extends Fragment {

    private ViewPager2 viewPager2;
    private RadioGridGroup radioGridGroup;
    private ImageView recipeImage, recipeMenu, fakeRecipeMenu, questionImageView, questionImageViewFake;
    private TextView recipeTitle, recipeTime, recipePortions, recipeLevel;
    private PopupMenu popupMenu;
    private PopupMenu menu;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentItemRecipeInsideBinding fragmentItemRecipeInsideBinding =
                FragmentItemRecipeInsideBinding.inflate(inflater, container, false);

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

        setupPopUpMenu(fakeRecipeMenu);
        setupPopUpMenuForImage(questionImageViewFake);

        recipeMenu.setOnClickListener(v -> popupMenu.show());
        questionImageView.setOnClickListener(v -> menu.show());
    }

    public void setupPopUpMenu(View view) {
        popupMenu = new PopupMenu(requireContext(), view, Gravity.END);
        popupMenu.getMenuInflater().inflate(R.menu.recipe_inside_menu, popupMenu.getMenu());
        Context context = getContext();

        popupMenu.setOnMenuItemClickListener(item -> {
            if(item.getItemId() == R.id.lista_plan){
                Timber.d("Lista plan");
                PopUpControlShoppingStart popUpControl = new PopUpControlShoppingStart();
                popUpControl.showPopUpNumerOfServingPerRecipe(context);
                return true;
            } else if(item.getItemId() == R.id.plan_kalendarz) {
                Timber.d("Plan kalendarz");
                PopUpControlCalendarStart popUpControl = new PopUpControlCalendarStart();
                popUpControl.showPopUpNumerOfServingPerRecipe(context);
                return true;
            }
            return false;
        });
    }

    public void setupPopUpMenuForImage(View view) {
        menu = new PopupMenu(requireContext(), view, Gravity.END);
        menu.getMenuInflater().inflate(R.menu.question_menu, menu.getMenu());
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
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    questionImageView.setVisibility(View.VISIBLE);
                    questionImageViewFake.setVisibility(View.VISIBLE);
                    radioGridGroup.setCheckedRadioButtonById(R.id.skladniki_button);
                } else {
                    questionImageView.setVisibility(View.INVISIBLE);
                    questionImageViewFake.setVisibility(View.INVISIBLE);
                    radioGridGroup.setCheckedRadioButtonById(R.id.przepis_button);
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