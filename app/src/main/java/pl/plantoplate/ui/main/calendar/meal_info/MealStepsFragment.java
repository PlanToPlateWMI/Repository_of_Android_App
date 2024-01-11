package pl.plantoplate.ui.main.calendar.meal_info;

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
import pl.plantoplate.databinding.FragmentItemRecipeInsidePrzepisItemsBinding;
import pl.plantoplate.ui.main.calendar.meal_info.recycler_views.adapters.MealStepsAdapter;
import pl.plantoplate.ui.main.calendar.meal_info.view_models.MealInfoViewModel;

/**
 * This class is responsible for setting up the steps in the recycler view.
 */
public class MealStepsFragment extends Fragment {

    private MealStepsAdapter mealStepsAdapter;
    private RecyclerView recipeStepsRecyclerView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentItemRecipeInsidePrzepisItemsBinding fragmentItemRecipeInsidePrzepisItemsBinding =
                FragmentItemRecipeInsidePrzepisItemsBinding.inflate(inflater, container, false);

        initViews(fragmentItemRecipeInsidePrzepisItemsBinding);
        setupViewModel();
        setupRecyclerView();
        return fragmentItemRecipeInsidePrzepisItemsBinding.getRoot();
    }

    private void initViews(FragmentItemRecipeInsidePrzepisItemsBinding fragmentItemRecipeInsidePrzepisItemsBinding) {
        recipeStepsRecyclerView = fragmentItemRecipeInsidePrzepisItemsBinding.recipeRecyclerViewKroki;
    }

    public void setupRecyclerView(){
        recipeStepsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mealStepsAdapter = new MealStepsAdapter(new ArrayList<>());
        recipeStepsRecyclerView.setAdapter(mealStepsAdapter);
    }

    public void setupViewModel(){
        MealInfoViewModel mealInfoViewModel;
        mealInfoViewModel = new ViewModelProvider(requireParentFragment()).get(MealInfoViewModel.class);

        mealInfoViewModel.getMealInfo().observe(getViewLifecycleOwner(),
                recipeInfo -> mealStepsAdapter.setStepsList(recipeInfo.getSteps()));
    }
}
