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
import java.util.Optional;
import pl.plantoplate.data.remote.models.recipe.RecipeInfo;
import pl.plantoplate.databinding.FragmentItemRecipeInsidePrzepisBinding;
import pl.plantoplate.ui.main.recipes.recipeInfo.recyclerViews.adapters.RecipeStepsAdapter;
import pl.plantoplate.ui.main.recipes.recipeInfo.viewModels.RecipeInfoViewModel;

public class RecipeStepsFragment extends Fragment {
    private RecipeStepsAdapter recipeStepsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentItemRecipeInsidePrzepisBinding fragmentItemRecipeInsidePrzepisBinding =
                FragmentItemRecipeInsidePrzepisBinding.inflate(inflater, container, false);

        setupRecyclerView(fragmentItemRecipeInsidePrzepisBinding);
        return fragmentItemRecipeInsidePrzepisBinding.getRoot();
    }

    private void setupRecyclerView(FragmentItemRecipeInsidePrzepisBinding fragmentItemRecipeInsidePrzepisBinding) {
        RecipeInfoViewModel recipeInfoViewModel = new ViewModelProvider(requireParentFragment()).get(RecipeInfoViewModel.class);
        RecipeInfo recipeInfo = Optional.ofNullable(recipeInfoViewModel.getRecipeInfo().getValue()).orElse(new RecipeInfo());

        RecyclerView recyclerView = fragmentItemRecipeInsidePrzepisBinding.recipeRecyclerViewSkladniki;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeStepsAdapter = new RecipeStepsAdapter(Optional.ofNullable(recipeInfo.getSteps()).orElse(new ArrayList<>()));
        recyclerView.setAdapter(recipeStepsAdapter);
    }
}