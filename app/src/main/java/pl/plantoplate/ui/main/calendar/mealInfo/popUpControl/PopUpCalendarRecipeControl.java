package pl.plantoplate.ui.main.calendar.mealInfo.popUpControl;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.models.meal.MealPlan;
import pl.plantoplate.data.remote.repository.MealRepository;
import pl.plantoplate.ui.main.calendar.mealInfo.popUps.ProductsSynchronizationPopUpCalendar;
import pl.plantoplate.ui.main.calendar.mealInfo.popUps.QuestionCookRecipe;
import pl.plantoplate.ui.main.calendar.mealInfo.popUps.QuestionDeleteRecipe;
import pl.plantoplate.ui.main.recipes.recipeInfo.popUps.ProductsSynchronizationPopUp;

public class PopUpCalendarRecipeControl {

//    private MealPlan mealPlan;
//    private FragmentManager fragmentManager;
//    private SharedPreferences prefs;
//
//    public PopUpCalendarRecipeControl(FragmentManager fragmentManager, MealPlan mealPlan){
//        this.mealPlan = mealPlan;
//        this.fragmentManager = fragmentManager;
//    }
//
//    /**
//     This method shows pop-up with question about synchronization
//     */
//    public void showPopUpSynchronization() {
//
//        ProductsSynchronizationPopUpCalendar productsSynchronizationPopUpCalendar =
//                new ProductsSynchronizationPopUpCalendar(mealPlan);
//
//        productsSynchronizationPopUpCalendar.setOnAcceptButtonClickListener(v -> {
//            mealPlan = productsSynchronizationPopUpCalendar.getMealPlan();
//        });
//
//        productsSynchronizationPopUpCalendar.show(fragmentManager, "ProductsSynchronizationPopUpCalendar");
//    }
//
//    /**
//     This method shows pop-up with question about adding to calendar
//     */
//    public void showPopUpCookRecipe(){
//        QuestionCookRecipe questionCookRecipe =
//                new QuestionCookRecipe();
//
//        questionCookRecipe.setOnAcceptButtonClickListener(v -> {
//            //synchronization
//        });
//
//        questionCookRecipe.show(fragmentManager, "QuestionCookRecipe");
//    }
//
//    public void prepareMeal(){
//        String token = "Bearer " + prefs.getString("token", "");
//        MealRepository mealRepository = new MealRepository();
//        Disposable disposable = mealRepository.planMealV1(token, addMealProducts)
//                .subscribe(mealPlan -> {
//                    Toast.makeText(requireContext(), "Produkty zostały dodane do listy zakupów", Toast.LENGTH_SHORT).show();
//                    dismiss();
//                }, throwable -> Toast.makeText(requireContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show());
//        compositeDisposable.add(disposable);
//    }
}