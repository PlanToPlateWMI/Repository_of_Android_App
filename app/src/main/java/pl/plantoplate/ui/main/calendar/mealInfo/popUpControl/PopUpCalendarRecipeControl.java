package pl.plantoplate.ui.main.calendar.mealInfo.popUpControl;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

import pl.plantoplate.data.remote.models.meal.MealPlan;
import pl.plantoplate.ui.main.calendar.mealInfo.popUps.ProductsSynchronizationPopUpCalendar;
import pl.plantoplate.ui.main.calendar.mealInfo.popUps.QuestionCookRecipe;
import pl.plantoplate.ui.main.calendar.mealInfo.popUps.QuestionDeleteRecipe;
import pl.plantoplate.ui.main.recipes.recipeInfo.popUps.ProductsSynchronizationPopUp;

public class PopUpCalendarRecipeControl {

    private MealPlan mealPlan;
    private FragmentManager fragmentManager;

    public PopUpCalendarRecipeControl(FragmentManager fragmentManager, MealPlan mealPlan){
        this.mealPlan = mealPlan;
        this.fragmentManager = fragmentManager;
    }

    /**
     This method shows pop-up with question about synchronization
     */
    public void showPopUpSynchronization() {

        ProductsSynchronizationPopUpCalendar productsSynchronizationPopUpCalendar =
                new ProductsSynchronizationPopUpCalendar(mealPlan);

        productsSynchronizationPopUpCalendar.setOnAcceptButtonClickListener(v -> {
            mealPlan = productsSynchronizationPopUpCalendar.getMealPlan();
        });

        productsSynchronizationPopUpCalendar.show(fragmentManager, "ProductsSynchronizationPopUpCalendar");
    }

    /**
     This method shows pop-up with question about adding to calendar
     */
    public void showPopUpCookRecipe(){
        QuestionCookRecipe questionCookRecipe =
                new QuestionCookRecipe();

        questionCookRecipe.setOnAcceptButtonClickListener(v -> {
            //synchronization
        });

        questionCookRecipe.show(fragmentManager, "QuestionCookRecipe");
    }
}