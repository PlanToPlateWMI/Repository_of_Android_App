package pl.plantoplate.ui.main.recipes.recipeInfo.popUpControl;

import android.app.Dialog;
import android.content.Context;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import pl.plantoplate.R;

/*
    This class is responsible for showing pop-ups after clicking on the "Add to calendar" button
 */
public class PopUpControlCalendarStart {

    /*
        This method shows pop-up with question about adding to calendar
     */
    public void showPopUpNumerOfServingPerRecipe(Context context) {

        Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.new_pop_up_number_of_servings_per_recipe);

        TextView acceptButton = dialog.findViewById(R.id.zatwierdzenie);
        TextView cancelButton = dialog.findViewById(R.id.close);

        acceptButton.setOnClickListener(v -> {

            //pobieranie ilosci porcji

            Toast.makeText(context, "Liczba porcji została ustalona", Toast.LENGTH_SHORT).show();

            showPopUpPlanning(context);
            dialog.dismiss();

        });

        cancelButton.setOnClickListener(v -> {
            Toast.makeText(context, "Liczba porcji nie została ustalona", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    /*
        This method shows pop-up with question about adding to calendar
     */
    public void showPopUpPlanning(Context context){
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.new_pop_up_planing_calendar);

        TextView acceptButton = dialog.findViewById(R.id.button_yes);
        TextView cancelButton = dialog.findViewById(R.id.button_no);

        //only if ONE OF calendar item is clicked
        //one radio batton is checked
        acceptButton.setOnClickListener(v -> {
            Toast.makeText(context, "Przepis został dodany do kalendarza", Toast.LENGTH_SHORT).show();
            showPopUpQuestionAlsoAddToShoppingList(context);
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            Toast.makeText(context, "Przepis nie został dodany do kalendarza", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    /*
        This method shows pop-up with question about adding to shopping list
     */
    public void showPopUpQuestionAlsoAddToShoppingList(Context context){
        Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.new_pop_up_question_add_to_shoping_list);

        TextView acceptButton = dialog.findViewById(R.id.button_yes);
        TextView cancelButton = dialog.findViewById(R.id.button_no);

        acceptButton.setOnClickListener(v -> {
            Toast.makeText(context, "Dodawanie do listy zakupów...", Toast.LENGTH_SHORT).show();
            showPopUpSynchronization(context);
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            Toast.makeText(context, "Produkty nie zostały dodane do listy zakupów", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

    /*
        This method shows pop-up with question about synchronization
     */
    public void showPopUpSynchronization(Context context) {

        Dialog dialog = new Dialog(context);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.new_pop_up_question_synhronization_on);

        CheckBox checkBox = dialog.findViewById(R.id.checkBox);

        TextView acceptButton = dialog.findViewById(R.id.button_yes);
        TextView cancelButton = dialog.findViewById(R.id.button_no);

        acceptButton.setOnClickListener(v -> {

            //wlaczanie synchronizacji
            if(checkBox.isChecked()){
                Toast.makeText(context, "Synchronizacja została włączona", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Synchronizacja nie została włączona", Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(context, "Produkty zostały dodany do listy zakupów", Toast.LENGTH_SHORT).show();

            dialog.dismiss();

        });

        cancelButton.setOnClickListener(v -> {
            Toast.makeText(context, "Produkty nie zostały dodany do listy zakupów", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }

}
