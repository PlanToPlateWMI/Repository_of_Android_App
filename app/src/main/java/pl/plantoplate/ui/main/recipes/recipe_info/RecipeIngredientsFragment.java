package pl.plantoplate.ui.main.recipes.recipe_info;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import pl.plantoplate.data.remote.models.user.Role;
import pl.plantoplate.databinding.FragmentItemRecipeInsideSkladnikiBinding;
import pl.plantoplate.ui.main.recipes.recipe_info.recycler_views.adapters.RecipeIngredientsAdapter;
import pl.plantoplate.ui.main.recipes.recipe_info.view_models.RecipeInfoViewModel;

public class RecipeIngredientsFragment extends Fragment {

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
        RecipeInfoViewModel recipeInfoViewModel;
        recipeInfoViewModel = new ViewModelProvider(requireParentFragment()).get(RecipeInfoViewModel.class);

        recipeInfoViewModel.getRecipeInfo().observe(getViewLifecycleOwner(),
                recipeInfo -> recipeIngredientsAdapter.setIngredientsList(recipeInfo.getIngredients()));
    }

    public void setupRecyclerView(FragmentItemRecipeInsideSkladnikiBinding fragmentItemRecipeInsideSkladnikiBinding){
        SharedPreferences prefs = requireActivity().getSharedPreferences("prefs", 0);
        Role role = Role.valueOf(prefs.getString("role", ""));

        RecyclerView recyclerView = fragmentItemRecipeInsideSkladnikiBinding.recipeRecyclerViewSkladniki;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeIngredientsAdapter = new RecipeIngredientsAdapter(role);
        recyclerView.setAdapter(recipeIngredientsAdapter);
    }
}