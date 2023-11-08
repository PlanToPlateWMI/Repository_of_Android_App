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
package pl.plantoplate.ui.main.recepies.selectedRecipes;

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
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.repository.RecipeRepository;
import pl.plantoplate.databinding.FragmentRecipeBaseBinding;
import pl.plantoplate.databinding.FragmentRecipeInsideNotAllBinding;
import pl.plantoplate.databinding.FragmentRecipeNewBinding;
import pl.plantoplate.tools.CategorySorter;
import pl.plantoplate.ui.main.recepies.recyclerViews.adapters.RecipeCategoryAdapter;
import timber.log.Timber;

public class SelectedRecipesFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRecipeBaseBinding fragmentRecipeBaseBinding =
                FragmentRecipeBaseBinding.inflate(inflater, container, false);
        return fragmentRecipeBaseBinding.getRoot();
    }

//    private CompositeDisposable compositeDisposable;
//    private RecipeCategoryAdapter recipeCategoryAdapter;
//    private FloatingActionButton floatingActionButton;
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        FragmentRecipeInsideNotAllBinding fragmentRecipeInsideNotAllBinding =
//                FragmentRecipeInsideNotAllBinding.inflate(inflater, container, false);
//        compositeDisposable = new CompositeDisposable();
//
//        floatingActionButton = fragmentRecipeInsideNotAllBinding.plusInKalendarz;
//
//        setupRecyclerView(fragmentRecipeInsideNotAllBinding);
//        getSelectedRecepies();
//        return fragmentRecipeInsideNotAllBinding.getRoot();
//    }
//
//    public void getSelectedRecepies(){
//        RecipeRepository recipeRepository = new RecipeRepository();
//
//        Disposable disposable = recipeRepository.getSelectedRecipes("")
//                .subscribe(
//                        recipes -> recipeCategoryAdapter.setCategoriesList(CategorySorter.sortCategoriesByRecipe(recipes)),
//                        throwable -> Timber.e(throwable, "Error while getting recipes")
//                );
//
//        compositeDisposable.add(disposable);
//    }
//
//    public void setupRecyclerView(FragmentRecipeInsideNotAllBinding fragmentRecipeInsideAllBinding){
//        RecyclerView recyclerView = fragmentRecipeInsideAllBinding.productsOwnRecyclerView;
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recipeCategoryAdapter = new RecipeCategoryAdapter(new ArrayList<>());
//        recyclerView.setAdapter(recipeCategoryAdapter);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        compositeDisposable.dispose();
//    }
}