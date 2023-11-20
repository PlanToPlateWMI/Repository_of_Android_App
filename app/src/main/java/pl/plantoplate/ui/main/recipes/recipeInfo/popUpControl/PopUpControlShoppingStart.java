package pl.plantoplate.ui.main.recipes.recipeInfo.popUpControl;

import android.content.Context;
import androidx.fragment.app.FragmentManager;
import pl.plantoplate.data.remote.models.meal.MealPlan;
import pl.plantoplate.ui.main.recipes.recipeInfo.popUps.CalendarPlanningPopUp;
import pl.plantoplate.ui.main.recipes.recipeInfo.popUps.ChoosePortionsNumberPopUp;
import pl.plantoplate.ui.main.recipes.recipeInfo.popUps.ProductsSynchronizationPopUp;
import pl.plantoplate.ui.main.recipes.recipeInfo.popUps.QuestionAddToCalendarPopUp;

/**
    This class is responsible for showing pop-ups after clicking on the "Add to shopping list" button
 */
public class PopUpControlShoppingStart {
    private MealPlan mealPlan;
    private FragmentManager fragmentManager;

    public PopUpControlShoppingStart(FragmentManager fragmentManager, MealPlan mealPlan){
        this.mealPlan = mealPlan;
        this.fragmentManager = fragmentManager;
    }

    public void showPopUpNumerOfServingPerRecipe() {

        ChoosePortionsNumberPopUp choosePortionsNumberPopUp =
                new ChoosePortionsNumberPopUp(mealPlan);

        choosePortionsNumberPopUp.setOnAcceptButtonClickListener(v -> {
            showPopUpSynchronization();
            mealPlan = choosePortionsNumberPopUp.getMealPlan();
        });

        choosePortionsNumberPopUp.show(fragmentManager, "ChoosePortionsNumberPopUp");
    }

    /**
        This method shows pop-up with question about synchronization
     */
    public void showPopUpSynchronization() {

        ProductsSynchronizationPopUp productsSynchronizationPopUp =
                new ProductsSynchronizationPopUp(mealPlan);

        productsSynchronizationPopUp.setOnAcceptButtonClickListener(v -> {
            showPopUpQuestionAlsoAddToCalendar();
            mealPlan = productsSynchronizationPopUp.getMealPlan();
        });

        productsSynchronizationPopUp.show(fragmentManager, "ProductsSynchronizationPopUp");
    }

    /**
        This method shows pop-up with question about adding to calendar
     */
    public void showPopUpQuestionAlsoAddToCalendar(){
        QuestionAddToCalendarPopUp questionAddToCalendarPopUp =
                new QuestionAddToCalendarPopUp();

        questionAddToCalendarPopUp.setOnAcceptButtonClickListener(v -> {
            showPopUpPlanning();
        });

        questionAddToCalendarPopUp.show(fragmentManager, "QuestionAddToCalendarPopUp");
    }

    /**
        This method shows pop-up with information about planning
     */
    public void showPopUpPlanning(){

        CalendarPlanningPopUp calendarPlanningPopUp =
                new CalendarPlanningPopUp(mealPlan);

        calendarPlanningPopUp.show(fragmentManager, "CalendarPlanningPopUp");
    }
}
