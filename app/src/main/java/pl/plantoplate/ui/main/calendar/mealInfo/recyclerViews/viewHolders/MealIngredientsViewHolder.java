package pl.plantoplate.ui.main.calendar.mealInfo.recyclerViews.viewHolders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import pl.plantoplate.data.remote.models.recipe.Ingredient;
import pl.plantoplate.databinding.ItemSkladnikBinding;

public class MealIngredientsViewHolder extends RecyclerView.ViewHolder {

    private CheckBox checkBox;
    private TextView ingredientName;
    private TextView ingredientAmount;

    public MealIngredientsViewHolder(@NonNull View itemView) {
        super(itemView);

        ItemSkladnikBinding itemSkladnikBinding = ItemSkladnikBinding.bind(itemView);
        checkBox = itemSkladnikBinding.checkBox;
        ingredientName = itemSkladnikBinding.textViewSkladnik;
        ingredientAmount = itemSkladnikBinding.textViewIloscSkladnika;

    }

    public void setOnCheckedChangeListener(CheckBox.OnCheckedChangeListener listener) {
        checkBox.setOnCheckedChangeListener(listener);
    }

    public void bind(Ingredient ingredient, boolean isSelected) {
        checkBox.setChecked(isSelected);
        ingredientName.setText(ingredient.getIngredientName());
        ingredientAmount.setText(ingredient.getQuantity() + " " + ingredient.getUnit());

        //checkBox.setOnClickListener(v -> toggleCrossedOut());
    }

//        public void toggleCrossedOut() {
//            boolean crossedOut = checkBox.isChecked();
//            ingredientName.setPaintFlags(crossedOut ?
//                            ingredientName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG) :
//                            ingredientName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//
//            ingredientAmount.setPaintFlags(crossedOut ?
//                    ingredientAmount.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG) :
//                    ingredientAmount.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//    }
}
