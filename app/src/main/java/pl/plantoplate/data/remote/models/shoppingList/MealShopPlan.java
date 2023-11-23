package pl.plantoplate.data.remote.models.shoppingList;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

import pl.plantoplate.data.remote.models.meal.MealPlan;

public class MealShopPlan {

    private int recipeId;
    private int portions;
    @JsonProperty("ingredientsId")
    private ArrayList<Integer> ingredientsIds;

    public MealShopPlan(){
    }

    public MealShopPlan(MealPlan mealPlan){
        recipeId = mealPlan.getRecipeId();
        portions = mealPlan.getPortions();
        ingredientsIds = mealPlan.getIngredientsIds();
    }

    public MealShopPlan(int recipeId, int portions, ArrayList<Integer> ingredientsIds) {
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

    public ArrayList<Integer> getIngredientsIds() {
        return ingredientsIds;
    }

    public void setIngredientsIds(ArrayList<Integer> ingredientsIds) {
        this.ingredientsIds = ingredientsIds;
    }
}
