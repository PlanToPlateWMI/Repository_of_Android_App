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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MealPlanNew {
    private int recipeId;
    private int portions;
    @JsonProperty("ingredients")
    private List<Integer> ingredientsIds;
    private MealType mealType;
    private String date;
    @JsonProperty("isProductsAdd")
    private boolean isProductsAdd;
    @JsonProperty("isSynchronize")
    private boolean isSynchronize;

    public MealPlanNew(){
    }

    public MealPlanNew(int recipeId, int portions, List<Integer> ingredientsIds, MealType mealType, String date, boolean isProductsAdd, boolean isSynchronize) {
        this.recipeId = recipeId;
        this.portions = portions;
        this.ingredientsIds = ingredientsIds;
        this.mealType = mealType;
        this.date = date;
        this.isProductsAdd = isProductsAdd;
        this.isSynchronize = isSynchronize;
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

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean getIsProductsAdd() {
        return isProductsAdd;
    }

    public void setIsProductsAdd(boolean isProductsAdd) {
        this.isProductsAdd = isProductsAdd;
    }

    public boolean getIsSynchronize() {
        return isSynchronize;
    }

    public void setIsSynchronize(boolean isSynchronize) {
        this.isSynchronize = isSynchronize;
    }
}