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
package pl.plantoplate.ui.main.recipes.own_recipes.view_models;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.repository.RecipeRepository;
import pl.plantoplate.ui.main.recipes.recycler_views.RecipeCategory;
import pl.plantoplate.utils.CategorySorter;

/**
 * ViewModel class for managing own recipes in an Android app.
 */
public class OwnRecipesViewModel extends AndroidViewModel {

    private MutableLiveData<List<RecipeCategory>> ownRecipes;
    private final CompositeDisposable compositeDisposable;
    private SharedPreferences prefs;

    public OwnRecipesViewModel(@NonNull Application application) {
        super(application);
        ownRecipes = new MutableLiveData<>();
        compositeDisposable = new CompositeDisposable();
        prefs = application.getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    public MutableLiveData<List<RecipeCategory>> getOwnRecipes() {
        return ownRecipes;
    }

    public void fetchOwnRecipes() {
        RecipeRepository recipeRepository = new RecipeRepository();

        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = recipeRepository.getOwnRecipes("", token)
                .subscribe(
                        recipes -> {
                            List<RecipeCategory> allCategories = CategorySorter.sortCategoriesByRecipe(recipes);
                            ownRecipes.setValue(allCategories);
                        },
                        throwable -> Toast.makeText(getApplication().getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show()
                );

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
