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