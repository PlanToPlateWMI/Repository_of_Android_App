package pl.plantoplate.data.remote.models.meal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

public class Meal {

    @JsonProperty("mealId")
    private int id;
    private String recipeTitle;
    private int time;
    private MealType mealType;

    public Meal(){
    }

    public Meal(int id, String recipeTitle, int time, MealType mealType) {
        this.id = id;
        this.recipeTitle = recipeTitle;
        this.time = time;
        this.mealType = mealType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }
}
