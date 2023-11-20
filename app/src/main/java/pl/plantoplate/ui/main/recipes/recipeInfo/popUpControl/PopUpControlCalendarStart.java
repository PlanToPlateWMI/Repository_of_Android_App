package pl.plantoplate.ui.main.recipes.recipeInfo.popUpControl;

import android.content.Context;
import android.widget.Toast;
import androidx.fragment.app.FragmentManager;
import java.time.LocalDate;
import pl.plantoplate.data.remote.models.meal.MealPlan;
import pl.plantoplate.ui.main.recipes.recipeInfo.popUps.AddPlannedIngredientsPopUp;
import pl.plantoplate.ui.main.recipes.recipeInfo.popUps.CalendarPlanningPopUp;
import pl.plantoplate.ui.main.recipes.recipeInfo.popUps.ChoosePortionsNumberPopUp;
import pl.plantoplate.ui.main.recipes.recipeInfo.popUps.ProductsSynchronizationPopUp;

/**
    This class is responsible for showing pop-ups after clicking on the "Add to calendar" button
 */
public class PopUpControlCalendarStart {

    private MealPlan mealPlan;
    private FragmentManager fragmentManager;

    public PopUpControlCalendarStart(FragmentManager fragmentManager, MealPlan mealPlan){
        this.mealPlan = mealPlan;
        this.fragmentManager = fragmentManager;
        this.mealPlan.setDate(LocalDate.now().toString());
    }

    /**
        This method shows pop-up with question about adding to calendar
     */
    public void showPopUpNumerOfServingPerRecipe() {

        ChoosePortionsNumberPopUp choosePortionsNumberPopUp =
                new ChoosePortionsNumberPopUp(mealPlan);

        choosePortionsNumberPopUp.setOnAcceptButtonClickListener(v -> {
            showPopUpPlanning();
            mealPlan = choosePortionsNumberPopUp.getMealPlan();
        });

        choosePortionsNumberPopUp.show(fragmentManager, "ChoosePortionsNumberPopUp");
    }

    /**
        This method shows pop-up with question about adding to calendar
     */
    public void showPopUpPlanning(){
        CalendarPlanningPopUp calendarPlanningPopUp =
                new CalendarPlanningPopUp(mealPlan);

        calendarPlanningPopUp.setOnAcceptButtonClickListener(v -> {
            showPopUpQuestionAlsoAddToShoppingList();
            mealPlan = calendarPlanningPopUp.getMealPlan();
        });

        calendarPlanningPopUp.show(fragmentManager, "CalendarPlanningPopUp");
    }

    /**
        This method shows pop-up with question about adding to shopping list
     */
    public void showPopUpQuestionAlsoAddToShoppingList(){
        AddPlannedIngredientsPopUp addPlannedIngredientsPopUp =
                new AddPlannedIngredientsPopUp();

        addPlannedIngredientsPopUp.setOnAcceptButtonClickListener(v -> {
            showPopUpSynchronization();
        });

        addPlannedIngredientsPopUp.show(fragmentManager, "AddPlannedIngredientsPopUp");
    }

    /**
        This method shows pop-up with question about synchronization
     */
    public void showPopUpSynchronization() {

        ProductsSynchronizationPopUp productsSynchronizationPopUp =
                new ProductsSynchronizationPopUp(mealPlan);

        productsSynchronizationPopUp.show(fragmentManager, "ProductsSynchronizationPopUp");
    }
}