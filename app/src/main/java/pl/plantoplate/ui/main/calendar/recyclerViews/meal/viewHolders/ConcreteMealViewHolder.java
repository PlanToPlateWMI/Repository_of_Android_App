package pl.plantoplate.ui.main.calendar.recyclerViews.meal.viewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pl.plantoplate.data.remote.models.meal.Meal;
import pl.plantoplate.databinding.ItemKalendarzBinding;

public class ConcreteMealViewHolder extends RecyclerView.ViewHolder {

    private final ImageView checkBox;
    private TextView mealName;
    private TextView mealPreparationTime;
    private ImageView deleteMeal;

    public ConcreteMealViewHolder(@NonNull View itemView) {
        super(itemView);

        ItemKalendarzBinding binding = ItemKalendarzBinding.bind(itemView);
        checkBox = binding.iconUncheckKupione;
        mealName = binding.nazwaPrzepisu;
        mealPreparationTime = binding.czasUgotowania;
        deleteMeal = binding.iconDeleteKupione;
    }

    public void bind(Meal meal) {
        mealName.setText(meal.getRecipeTitle());
        mealPreparationTime.setText(meal.getTime() + " min");
    }
}