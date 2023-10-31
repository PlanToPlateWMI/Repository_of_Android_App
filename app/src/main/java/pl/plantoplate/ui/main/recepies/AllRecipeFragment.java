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
package pl.plantoplate.ui.main.recepies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.models.category.Level;
import pl.plantoplate.data.remote.models.category.Recipe;
import pl.plantoplate.data.remote.repository.RecipeRepository;
import pl.plantoplate.databinding.FragmentRecipeInsideAllBinding;
import pl.plantoplate.tools.CategorySorter;
import pl.plantoplate.ui.main.recepies.recyclerViews.RecipeCategory;
import pl.plantoplate.ui.main.recepies.recyclerViews.adapters.RecipeCategoryAdapter;
import timber.log.Timber;

public class AllRecipeFragment extends Fragment {

    RecipeCategoryAdapter recipeCategoryAdapter;
    FloatingActionButton floatingActionButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        FragmentRecipeInsideAllBinding fragmentRecipeInsideAllBinding = FragmentRecipeInsideAllBinding.inflate(inflater, container, false);

        setupRecyclerView(fragmentRecipeInsideAllBinding);

        getRecepies();

        floatingActionButton = fragmentRecipeInsideAllBinding.plusInAllRecipes;

        floatingActionButton.setVisibility(View.INVISIBLE);

        return fragmentRecipeInsideAllBinding.getRoot();
    }


    public void getRecepies(){

        RecipeCategory recipeCategory = new RecipeCategory("Przystawki", new ArrayList<>());
        RecipeCategory recipeCategory1 = new RecipeCategory("Zupy", new ArrayList<>());

        //int id, String title, String time, Level level, String image, String categoryName, boolean vege
        Recipe recipe = new Recipe(1, "Zupa pomidorowa", "30 min", Level.EASY,
                "https://assets.tmecosys.com/image/upload/t_web767x639/img/recipe/ras/Assets/AF530EE2-91C9-4964-AB81-8EC3AD5C0788/Derivates/41DDA174-8F07-4BE6-B763-06438A86065B.jpg",
                "Zupy", true);
        Recipe recipe3 = new Recipe(1, "Zupa hujowa", "30 min", Level.EASY,
                "https://assets.tmecosys.com/image/upload/t_web767x639/img/recipe/ras/Assets/AF530EE2-91C9-4964-AB81-8EC3AD5C0788/Derivates/41DDA174-8F07-4BE6-B763-06438A86065B.jpg",
                "Zupy", true);
        Recipe recipe4 = new Recipe(1, "Zupa huj", "30 min", Level.EASY,
                "https://assets.tmecosys.com/image/upload/t_web767x639/img/recipe/ras/Assets/AF530EE2-91C9-4964-AB81-8EC3AD5C0788/Derivates/41DDA174-8F07-4BE6-B763-06438A86065B.jpg",
                "Zupy", true);
        Recipe recipe2 = new Recipe(1, "Gowno", "2 min", Level.EASY,
                "https://assets.tmecosys.com/image/upload/t_web767x639/img/recipe/ras/Assets/AF530EE2-91C9-4964-AB81-8EC3AD5C0788/Derivates/41DDA174-8F07-4BE6-B763-06438A86065B.jpg",
                "Przystawki", false);

        recipeCategory.getRecipes().add(recipe);
        recipeCategory1.getRecipes().add(recipe2);

        RecipeRepository recipeRepository = new RecipeRepository();

//        Disposable disposable = recipeRepository.getAllRecipes("")
//                .subscribe(
//                        recipes -> {
//                            recipeCategoryAdapter.setCategoriesList(CategorySorter.sortCategoriesByRecipe(recipes));
//                        },
//                        throwable -> Timber.e(throwable, "Error while getting recipes")
//                );

        recipeCategoryAdapter.setCategoriesList(CategorySorter.sortCategoriesByRecipe(new ArrayList<Recipe>() {{
            add(recipe);
            add(recipe2);
            add(recipe3);
            add(recipe4);
        }}));

    }

    public void setupRecyclerView(FragmentRecipeInsideAllBinding fragmentRecipeInsideAllBinding){
        RecyclerView recyclerView = fragmentRecipeInsideAllBinding.recipeRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recipeCategoryAdapter = new RecipeCategoryAdapter(new ArrayList<>());
        recyclerView.setAdapter(recipeCategoryAdapter);
    }
}