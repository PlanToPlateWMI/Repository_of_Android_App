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
import pl.plantoplate.data.remote.models.recipe.Ingredient;
import pl.plantoplate.data.remote.models.recipe.RecipeInfo;
import pl.plantoplate.databinding.FragmentItemRecipeInsideSkladnikiBinding;
import pl.plantoplate.ui.main.recipes.recipeInfo.recyclerViews.adapters.RecipeIngredientsAdapter;
import pl.plantoplate.ui.main.recipes.recipeInfo.viewModels.RecipeInfoViewModel;

public class RecipeIngredientsFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentItemRecipeInsideSkladnikiBinding fragmentItemRecipeInsideSkladnikiBinding =
                FragmentItemRecipeInsideSkladnikiBinding.inflate(inflater, container, false);

        setupRecyclerView(fragmentItemRecipeInsideSkladnikiBinding);
        return fragmentItemRecipeInsideSkladnikiBinding.getRoot();
    }

    public void setupRecyclerView(FragmentItemRecipeInsideSkladnikiBinding fragmentItemRecipeInsideSkladnikiBinding){
        RecipeInfoViewModel recipeInfoViewModel = new ViewModelProvider(requireParentFragment()).get(RecipeInfoViewModel.class);
        ArrayList<Ingredient> ingredients = Optional.ofNullable(recipeInfoViewModel.getRecipeInfo().getValue()).
                orElse(new RecipeInfo()).getIngredients();
        ingredients = Optional.ofNullable(ingredients).orElse(new ArrayList<>());

        RecyclerView recyclerView = fragmentItemRecipeInsideSkladnikiBinding.recipeRecyclerViewSkladniki;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        RecipeIngredientsAdapter recipeIngredientsAdapter = new RecipeIngredientsAdapter(ingredients);
        recyclerView.setAdapter(recipeIngredientsAdapter);
    }
}