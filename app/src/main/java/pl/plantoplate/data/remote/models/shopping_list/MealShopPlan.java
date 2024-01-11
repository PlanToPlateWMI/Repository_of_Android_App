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
package pl.plantoplate.data.remote.models.shopping_list;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import pl.plantoplate.data.remote.models.meal.MealPlan;

public class MealShopPlan {

    private int recipeId;
    private int portions;
    @JsonProperty("ingredientsId")
    private List<Integer> ingredientsIds;

    public MealShopPlan(){
    }

    public MealShopPlan(MealPlan mealPlan){
        recipeId = mealPlan.getRecipeId();
        portions = mealPlan.getPortions();
        ingredientsIds = mealPlan.getIngredientsIds();
    }

    public MealShopPlan(int recipeId, int portions, List<Integer> ingredientsIds) {
        this.recipeId = recipeId;
        this.portions = portions;
        this.ingredientsIds = ingredientsIds;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getPortions() {
        return portions;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }

    public List<Integer> getIngredientsIds() {
        return ingredientsIds;
    }

    public void setIngredientsIds(List<Integer> ingredientsIds) {
        this.ingredientsIds = ingredientsIds;
    }
}