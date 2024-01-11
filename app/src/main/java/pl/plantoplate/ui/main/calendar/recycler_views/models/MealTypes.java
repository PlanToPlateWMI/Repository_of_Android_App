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
package pl.plantoplate.ui.main.calendar.recycler_views.models;

import java.util.List;
import java.util.Optional;
import pl.plantoplate.data.remote.models.meal.Meal;
import pl.plantoplate.data.remote.models.meal.MealType;

/**
 * This class is responsible for holding a list of meals of a specific type.
 */
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
