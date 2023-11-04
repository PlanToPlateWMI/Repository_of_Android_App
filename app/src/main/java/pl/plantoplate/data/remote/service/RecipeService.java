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
package pl.plantoplate.data.remote.service;

import java.util.ArrayList;
import io.reactivex.rxjava3.core.Single;
import pl.plantoplate.data.remote.models.category.Recipe;
import pl.plantoplate.data.remote.models.category.RecipeCategory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeService {

    @GET("api/recipes")
    Single<ArrayList<Recipe>> getAllRecipes(@Query("category") String type);

    @GET("api/recipes/selected")
    Single<ArrayList<Recipe>> getSelectedRecipes(@Query("category") String type);

    @GET("api/recipe-categories")
    Single<ArrayList<RecipeCategory>> getRecipeCategories();
}