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
package pl.plantoplate.ui.main.recipes.recipe_info;

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
import pl.plantoplate.ui.main.recipes.recipe_info.recycler_views.adapters.RecipeStepsAdapter;
import pl.plantoplate.ui.main.recipes.recipe_info.view_models.RecipeInfoViewModel;

public class RecipeStepsFragment extends Fragment {

    private RecipeStepsAdapter recipeStepsAdapter;
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
        recipeStepsAdapter = new RecipeStepsAdapter(new ArrayList<>());
        recipeStepsRecyclerView.setAdapter(recipeStepsAdapter);
    }

    public void setupViewModel(){
        RecipeInfoViewModel recipeInfoViewModel;
        recipeInfoViewModel = new ViewModelProvider(requireParentFragment()).get(RecipeInfoViewModel.class);

        recipeInfoViewModel.getRecipeInfo().observe(getViewLifecycleOwner(),
                recipeInfo -> recipeStepsAdapter.setStepsList(recipeInfo.getSteps()));
    }
}