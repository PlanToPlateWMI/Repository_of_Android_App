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
package pl.plantoplate.data.remote.models.meal;

import androidx.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MealPlan {

    private MealType mealType;
    private int portions;
    private String date;
    private int recipeId;
    @JsonProperty("ingredientsId")
    private List<Integer> ingredientsIds;

    public MealPlan(){
    }

    public MealPlan(MealType mealType, int portions, String date, int recipeId, List<Integer> ingredientsIds) {
        this.mealType = mealType;
        this.portions = portions;
        this.date = date;
        this.recipeId = recipeId;
        this.ingredientsIds = ingredientsIds;
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public int getPortions() {
        return portions;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public List<Integer> getIngredientsIds() {
        return ingredientsIds;
    }

    public void setIngredientsIds(List<Integer> ingredientsIds) {
        this.ingredientsIds = ingredientsIds;
    }

    @NonNull
    @Override
    public String toString() {
        return "MealPlan{" +
                "mealType=" + mealType +
                ", portions=" + portions +
                ", date='" + date + '\'' +
                ", recipeId=" + recipeId +
                ", ingredientsIds=" + ingredientsIds +
                '}';
    }
}
