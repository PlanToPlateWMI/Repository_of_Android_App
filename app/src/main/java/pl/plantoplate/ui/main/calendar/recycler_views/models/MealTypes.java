package pl.plantoplate.ui.main.calendar.recycler_views.models;

import java.util.List;
import java.util.Optional;
import pl.plantoplate.data.remote.models.meal.Meal;
import pl.plantoplate.data.remote.models.meal.MealType;

public class MealTypes {

    private MealType mealType;
    private List<Meal> meals;

    public MealTypes(MealType mealType, List<Meal> meals) {
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

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }
}
