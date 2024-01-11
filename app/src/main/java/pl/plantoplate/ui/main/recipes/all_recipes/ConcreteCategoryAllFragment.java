/*
 * Copyright 2023 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.plantoplate.ui.main.recipes.all_recipes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.recipe.Recipe;
import pl.plantoplate.databinding.FragmentRecipeInsideNotAllBinding;
import pl.plantoplate.ui.main.recipes.all_recipes.view_models.AllRecipesViewModel;
import pl.plantoplate.ui.main.recipes.recycler_views.RecipeCategory;
import pl.plantoplate.utils.CategorySorter;
import pl.plantoplate.ui.main.recipes.recipe_info.RecipeInfoFragment;
import pl.plantoplate.ui.main.recipes.recycler_views.adapters.RecipeAdapter;
import pl.plantoplate.ui.main.recipes.recycler_views.listeners.SetupRecipeButtons;

public class ConcreteCategoryAllFragment extends Fragment implements SearchView.OnQueryTextListener {

    private CompositeDisposable compositeDisposable;
    private AllRecipesViewModel allRecipesViewModel;
    private RecipeAdapter recipeAdapter;
    private FloatingActionButton floatingActionButton;
    private SearchView searchView;
    private String category;

    public ConcreteCategoryAllFragment() {
    }

    public ConcreteCategoryAllFragment(String category) {
        this.category = category;
    }

    @Override
    public void onResume() {
        super.onResume();
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        searchView.clearFocus();
        searchView.setQuery("", false);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        List<Recipe> recipesCategory = Optional.ofNullable(allRecipesViewModel.getAllRecipes().getValue())
                .map(categories -> categories.stream()
                        .filter(currentCategory -> currentCategory.getName().equals(category))
                        .findFirst()
                        .map(RecipeCategory::getRecipes)
                        .orElse(new ArrayList<>())).orElse(new ArrayList<>());

        recipesCategory = CategorySorter.filterRecipesBySearch(recipesCategory, query);
        recipeAdapter.setRecipesList(CategorySorter.sortRecipesByName(recipesCategory));
        return true;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentRecipeInsideNotAllBinding fragmentRecipeInsideNotAllBinding =
                FragmentRecipeInsideNotAllBinding.inflate(inflater, container, false);
        compositeDisposable = new CompositeDisposable();

        floatingActionButton = fragmentRecipeInsideNotAllBinding.plusInKalendarz;
        floatingActionButton.setVisibility(View.INVISIBLE);
        searchView = requireParentFragment().requireView().findViewById(R.id.search);

        setupRecyclerView(fragmentRecipeInsideNotAllBinding);
        setupViewModel();
        return fragmentRecipeInsideNotAllBinding.getRoot();
    }

    private void setupViewModel() {
        allRecipesViewModel = new ViewModelProvider(requireParentFragment()).get(AllRecipesViewModel.class);
        allRecipesViewModel.getAllRecipes().observe(getViewLifecycleOwner(),
                recipes -> {
                    List<Recipe> recipesCategory = Optional.ofNullable(allRecipesViewModel.getAllRecipes().getValue())
                            .map(categories -> categories.stream()
                                    .filter(currentCategory -> currentCategory.getName().equals(category))
                                    .findFirst()
                                    .map(RecipeCategory::getRecipes)
                                    .orElse(new ArrayList<>())).orElse(new ArrayList<>());
                    recipeAdapter.setRecipesList(CategorySorter.sortRecipesByName(recipesCategory));
                });
    }


    public void setupRecyclerView(FragmentRecipeInsideNotAllBinding fragmentRecipeInsideNotAllBinding){
        RecyclerView recyclerView = fragmentRecipeInsideNotAllBinding.productsOwnRecyclerView;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recipeAdapter = new RecipeAdapter(new ArrayList<>());
        recipeAdapter.setUpRecipeButtons(new SetupRecipeButtons() {
            @Override
            public void setupOnItemClick(int id) {
                RecipeInfoFragment recipeInfoFragment = new RecipeInfoFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("recipeId", id);
                recipeInfoFragment.setArguments(bundle);
                replaceFragment(recipeInfoFragment);
            }
        });
        recyclerView.setAdapter(recipeAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    public void replaceFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .addToBackStack(null)
                .commit();
    }
}