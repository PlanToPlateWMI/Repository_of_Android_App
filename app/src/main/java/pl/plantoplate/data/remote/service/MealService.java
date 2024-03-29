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

import java.util.List;
import io.reactivex.rxjava3.core.Single;
import pl.plantoplate.data.remote.models.Message;
import pl.plantoplate.data.remote.models.meal.MealPlanNew;
import pl.plantoplate.data.remote.models.meal.Meal;
import pl.plantoplate.data.remote.models.meal.MealPlan;
import pl.plantoplate.data.remote.models.recipe.RecipeInfo;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MealService {

    @GET("api/meals")
    Single<List<Meal>> getMealsByDate(@Header("Authorization") String token, @Query("date") String date);

    @POST("api/meals")
    Single<Message> planMeal(@Header("Authorization") String token, @Body MealPlan mealPlan);

    @GET("api/meals/{mealId}")
    Single<RecipeInfo> getMealDetailsById(@Header("Authorization") String token, @Path("mealId") int mealId);

    @DELETE("api/meals/{mealId}")
    Single<Message> deleteMealById(@Header("Authorization") String token, @Path("mealId") int mealId);

    @POST("api/meals/v1")
    Single<Message> planMealV1(@Header("Authorization") String token, @Body MealPlanNew addMealProducts);

    @POST("api/meals/v2")
    Single<Message> planMealV2(@Header("Authorization") String token, @Body MealPlanNew addMealProducts);

    @PUT("api/meals/prepare/{id}")
    Single<Message> prepareMeal(@Header("Authorization") String token, @Path("id") int mealId);
}
