package pl.plantoplate.ui.main.calendar.recyclerViews.models;

import java.util.ArrayList;
import java.util.Optional;

import pl.plantoplate.data.remote.models.meal.Meal;
import pl.plantoplate.data.remote.models.meal.MealType;

public class MealTypes {

    private MealType mealType;
    private ArrayList<Meal> meals;

    public MealTypes(MealType mealType, ArrayList<Meal> meals) {
        this.mealType = mealType;
        this.meals = meals;
    }

    public Optional<Meal> getMealByType(MealType type) {
        return meals.stream()
                .filter(meal -> meal.getMealType() == type)
                .findFirst();
    }

    public MealType getMealType() {
        return mealType;
    }

    public void setMealType(MealType mealType) {
        this.mealType = mealType;
    }

    public ArrayList<Meal> getMeals() {
        return meals;
    }

    public void setMeals(ArrayList<Meal> meals) {
        this.meals = meals;
    }
}
