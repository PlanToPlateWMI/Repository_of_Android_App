package pl.plantoplate.ui.main.recipes.recipeInfo.newPopUpControl;

import androidx.fragment.app.FragmentManager;

import java.time.LocalDate;

import pl.plantoplate.data.remote.models.meal.MealPlan;
import pl.plantoplate.ui.main.recipes.recipeInfo.newPopUps.PopUpCalendarPlanningStart;

public class PopUpControlCalendarStart {

    private MealPlan mealPlan;
    private FragmentManager fragmentManager;

    public PopUpControlCalendarStart(FragmentManager fragmentManager, MealPlan mealPlan){
        this.mealPlan = mealPlan;
        this.fragmentManager = fragmentManager;
        this.mealPlan.setDate(LocalDate.now().toString());
    }

//    public void showPopUpCalendarPlanningStart() {
//
//        PopUpCalendarPlanningStart popUpCalendarPlanningStart =
//                new PopUpCalendarPlanningStart(mealPlan);
//
//        popUpCalendarPlanningStart.setOnAcceptButtonClickListener(v -> {
//            showPopUpChoosePortionsNumber();
//            mealPlan = popUpCalendarPlanningStart.getMealPlan();
//        });
//
//        choosePortionsNumberPopUp.show(fragmentManager, "ChoosePortionsNumberPopUp");
//    }
}
