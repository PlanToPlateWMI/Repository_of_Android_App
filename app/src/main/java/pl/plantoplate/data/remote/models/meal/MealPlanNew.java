package pl.plantoplate.data.remote.models.meal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class  MealPlanNew {

    private int recipeId;
    private int portions;
    @JsonProperty("ingredients")
    private List<Integer> ingredientsIds;
    private MealType mealType;
    private String date;
    private boolean productsAdd;
    private boolean synchronize;

    public MealPlanNew(){
    }

    public MealPlanNew(int recipeId, int portions, List<Integer> ingredientsIds, MealType mealType, String date, boolean productsAdd, boolean synchronize) {
        this.recipeId = recipeId;
        this.portions = portions;
        this.ingredientsIds = ingredientsIds;
        this.mealType = mealType;
        this.date = date;
        this.productsAdd = productsAdd;
        this.synchronize = synchronize;
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

    public boolean isProductsAdd() {
        return productsAdd;
    }

    public void setProductsAdd(boolean productsAdd) {
        this.productsAdd = productsAdd;
    }

    public boolean isSynchronize() {
        return synchronize;
    }

    public void setSynchronize(boolean synchronize) {
        this.synchronize = synchronize;
    }
}