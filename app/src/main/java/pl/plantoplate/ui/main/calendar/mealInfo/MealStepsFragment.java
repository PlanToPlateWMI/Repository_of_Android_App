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
import pl.plantoplate.databinding.FragmentItemRecipeInsidePrzepisItemsBinding;
import pl.plantoplate.ui.main.calendar.mealInfo.recyclerViews.adapters.MealStepsAdapter;
import pl.plantoplate.ui.main.calendar.mealInfo.viewModels.MealInfoViewModel;

public class MealStepsFragment extends Fragment {

    private MealInfoViewModel mealInfoViewModel;
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
        mealInfoViewModel = new ViewModelProvider(requireParentFragment()).get(MealInfoViewModel.class);

        mealInfoViewModel.getMealInfo().observe(getViewLifecycleOwner(),
                recipeInfo -> mealStepsAdapter.setStepsList(recipeInfo.getSteps()));
    }
}
