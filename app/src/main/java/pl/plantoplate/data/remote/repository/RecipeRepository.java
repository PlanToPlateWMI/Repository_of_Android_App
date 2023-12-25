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
package pl.plantoplate.data.remote.repository;

import java.util.ArrayList;
import java.util.HashMap;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.plantoplate.data.remote.ErrorHandler;
import pl.plantoplate.data.remote.RetrofitClient;
import pl.plantoplate.data.remote.models.recipe.Recipe;
import pl.plantoplate.data.remote.models.recipe.RecipeCategory;
import pl.plantoplate.data.remote.models.recipe.RecipeInfo;
import pl.plantoplate.data.remote.service.RecipeService;

public class RecipeRepository {

    private final RecipeService recipeService;

    public RecipeRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        recipeService = retrofitClient.getClient().create(RecipeService.class);
    }

    public Single<ArrayList<Recipe>> getAllRecipes(String category) {
        return recipeService.getAllRecipes(category)
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<Recipe>>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(400, "Katagoria nie istnieje.");
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<Recipe>> getOwnRecipes(String category, String token) {
        return recipeService.getOwnRecipes(category, token)
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<Recipe>>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(400, "Katagoria nie istnieje.");
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<RecipeInfo> getRecipe(int recipeId) {
        return recipeService.getRecipe(recipeId)
                .onErrorResumeNext(throwable -> new ErrorHandler<RecipeInfo>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(400, "Przepis nie istnieje.");
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Single<ArrayList<RecipeCategory>> getRecipeCategories() {
        return recipeService.getRecipeCategories()
                .onErrorResumeNext(throwable -> new ErrorHandler<ArrayList<RecipeCategory>>().
                        handleHttpError(throwable, new HashMap<>() {{
                            put(500, "Wystąpił nieznany błąd serwera.");
                        }}))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}