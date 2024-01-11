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

import java.util.HashMap;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;
import pl.plantoplate.data.remote.ErrorHandler;
import pl.plantoplate.data.remote.RetrofitClient;
import pl.plantoplate.data.remote.models.recipe.Recipe;
import pl.plantoplate.data.remote.models.recipe.RecipeInfo;
import pl.plantoplate.data.remote.service.RecipeService;

/**
 * A repository class responsible for retrieving recipe-related data from the remote API.
 */
public class RecipeRepository {

    private final RecipeService recipeService;

    public RecipeRepository() {
        RetrofitClient retrofitClient = RetrofitClient.getInstance();

        recipeService = retrofitClient.getClient().create(RecipeService.class);
    }

    /**
     * Retrieves a list of all recipes based on the provided category.
     *
     * @param category The category of recipes to retrieve.
     * @return A {@link Single} emitting a {@link List} of {@link Recipe} objects representing all recipes
     *         within the specified category.
     * @throws NullPointerException if {@code category} is {@code null}.
     *
     * @see Recipe
     */

    public Single<List<Recipe>> getAllRecipes(String category) {
        String recipeCategoryDoesNotExist = "Katagoria przepisu nie istnieje.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, recipeCategoryDoesNotExist);

        return recipeService.getAllRecipes(category)
                .onErrorResumeNext(throwable -> new ErrorHandler<List<Recipe>>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Retrieves a list of own recipes based on the provided category and token.
     *
     * @param category The category of recipes to retrieve.
     * @param token    The token used to authenticate the request.
     * @return A {@link Single} emitting a {@link List} of {@link Recipe} objects representing the user's own recipes
     *         within the specified category.
     * @throws NullPointerException if {@code category} or {@code token} is {@code null}.
     *
     * @see Recipe
     */

    public Single<List<Recipe>> getOwnRecipes(String category, String token) {
        String recipeCategoryDoesNotExist = "Katagoria przepisu nie istnieje.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, recipeCategoryDoesNotExist);

        return recipeService.getOwnRecipes(category, token)
                .onErrorResumeNext(throwable -> new ErrorHandler<List<Recipe>>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * Retrieves details of a recipe by its ID.
     *
     * @param recipeId The ID of the recipe to retrieve.
     * @return A {@link Single} emitting a {@link RecipeInfo} object representing the details of the specified recipe.
     * @throws NullPointerException if {@code recipeId} is {@code null}.
     *
     * @see RecipeInfo
     */

    public Single<RecipeInfo> getRecipe(int recipeId) {
        String recipeDoesNotExist = "Przepis nie istnieje.";
        HashMap<Integer, String> errorMap = new HashMap<>();
        errorMap.put(400, recipeDoesNotExist);

        return recipeService.getRecipe(recipeId)
                .onErrorResumeNext(throwable -> new ErrorHandler<RecipeInfo>().
                        handleHttpError(throwable, errorMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}