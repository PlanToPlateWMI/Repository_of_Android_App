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
import pl.plantoplate.databinding.FragmentItemRecipeInsideSkladnikiBinding;
import pl.plantoplate.ui.main.recipes.recipeInfo.recyclerViews.adapters.RecipeIngredientsAdapter;
import pl.plantoplate.ui.main.recipes.recipeInfo.viewModels.RecipeInfoViewModel;

public class RecipeIngredientsFragment extends Fragment {

    private RecipeInfoViewModel recipeInfoViewModel;
    private RecipeIngredientsAdapter recipeIngredientsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentItemRecipeInsideSkladnikiBinding fragmentItemRecipeInsideSkladnikiBinding =
                FragmentItemRecipeInsideSkladnikiBinding.inflate(inflater, container, false);

        setupRecyclerView(fragmentItemRecipeInsideSkladnikiBinding);
        setRecipeInfoViewModel();
        return fragmentItemRecipeInsideSkladnikiBinding.getRoot();
    }

    public void setRecipeInfoViewModel(){
        recipeInfoViewModel = new ViewModelProvider(requireParentFragment()).get(RecipeInfoViewModel.class);

        recipeInfoViewModel.getRecipeInfo().observe(getViewLifecycleOwner(),
                recipeInfo -> recipeIngredientsAdapter.setIngredientsList(recipeInfo.getIngredients()));
    }

    public void setupRecyclerView(FragmentItemRecipeInsideSkladnikiBinding fragmentItemRecipeInsideSkladnikiBinding){
        RecyclerView recyclerView = fragmentItemRecipeInsideSkladnikiBinding.recipeRecyclerViewSkladniki;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeIngredientsAdapter = new RecipeIngredientsAdapter(new ArrayList<>());
        recyclerView.setAdapter(recipeIngredientsAdapter);
    }
}