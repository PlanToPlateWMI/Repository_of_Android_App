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
