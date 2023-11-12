package pl.plantoplate.ui.main.recipes.recipeInfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import java.util.ArrayList;
import java.util.Optional;
import pl.plantoplate.data.remote.models.recipe.RecipeInfo;
import pl.plantoplate.databinding.FragmentItemRecipeInsidePrzepisBinding;
import pl.plantoplate.ui.main.recipes.recipeInfo.viewModels.RecipeInfoViewModel;

public class RecipeStepsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentItemRecipeInsidePrzepisBinding fragmentItemRecipeInsidePrzepisBinding =
                FragmentItemRecipeInsidePrzepisBinding.inflate(inflater, container, false);

        initViews(fragmentItemRecipeInsidePrzepisBinding);
        return fragmentItemRecipeInsidePrzepisBinding.getRoot();
    }

    private void initViews(FragmentItemRecipeInsidePrzepisBinding fragmentItemRecipeInsidePrzepisBinding) {
        TextView recipeStepsTextView = fragmentItemRecipeInsidePrzepisBinding.przepis;

        RecipeInfoViewModel recipeInfoViewModel = new ViewModelProvider(requireParentFragment()).get(RecipeInfoViewModel.class);
        RecipeInfo recipeInfo = Optional.ofNullable(recipeInfoViewModel.getRecipeInfo().getValue()).orElse(new RecipeInfo());
        Optional.ofNullable(recipeInfo.getSteps()).orElse(new ArrayList<>())
                .stream().reduce((s1, s2) -> s1 + "\n" + s2).ifPresent(recipeStepsTextView::setText);
    }
}