package pl.plantoplate.ui.main.recipes.recipeInfo;

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
import pl.plantoplate.ui.main.recipes.recipeInfo.recyclerViews.adapters.RecipeStepsAdapter;
import pl.plantoplate.ui.main.recipes.recipeInfo.viewModels.RecipeInfoViewModel;

public class RecipeStepsFragment extends Fragment {

    private RecipeInfoViewModel recipeInfoViewModel;
    private RecipeStepsAdapter recipeStepsAdapter;
    private RecyclerView recipeStepsRecyclerView;

    public RecipeStepsFragment() {
    }
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
        recipeStepsAdapter = new RecipeStepsAdapter(new ArrayList<>());
        recipeStepsRecyclerView.setAdapter(recipeStepsAdapter);
    }

    public void setupViewModel(){
        recipeInfoViewModel = new ViewModelProvider(requireParentFragment()).get(RecipeInfoViewModel.class);

        recipeInfoViewModel.getRecipeInfo().observe(getViewLifecycleOwner(),
                recipeInfo -> recipeStepsAdapter.setStepsList(recipeInfo.getSteps()));
    }
}