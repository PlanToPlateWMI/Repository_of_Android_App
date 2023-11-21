package pl.plantoplate.ui.main.calendar.mealInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import pl.plantoplate.databinding.FragmentItemRecipeInsideSkladnikiBinding;
import pl.plantoplate.ui.main.calendar.mealInfo.recyclerViews.adapters.MealIngredientsAdapter;
import pl.plantoplate.ui.main.calendar.mealInfo.viewModels.MealInfoViewModel;

public class MealIngredientsFragment extends Fragment {

    private MealInfoViewModel mealInfoViewModel;
    private MealIngredientsAdapter mealIngredientsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentItemRecipeInsideSkladnikiBinding fragmentItemRecipeInsideSkladnikiBinding =
                FragmentItemRecipeInsideSkladnikiBinding.inflate(inflater, container, false);

        setupRecyclerView(fragmentItemRecipeInsideSkladnikiBinding);
        setMealInfoViewModel();
        return fragmentItemRecipeInsideSkladnikiBinding.getRoot();
    }

    public void setMealInfoViewModel(){
        mealInfoViewModel = new ViewModelProvider(requireParentFragment()).get(MealInfoViewModel.class);

        mealInfoViewModel.getMealInfo().observe(getViewLifecycleOwner(),
                recipeInfo -> mealIngredientsAdapter.setIngredientsList(recipeInfo.getIngredients()));
    }

    public void setupRecyclerView(FragmentItemRecipeInsideSkladnikiBinding fragmentItemRecipeInsideSkladnikiBinding){
        RecyclerView recyclerView = fragmentItemRecipeInsideSkladnikiBinding.recipeRecyclerViewSkladniki;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mealIngredientsAdapter = new MealIngredientsAdapter(new ArrayList<>());
        recyclerView.setAdapter(mealIngredientsAdapter);
    }
}