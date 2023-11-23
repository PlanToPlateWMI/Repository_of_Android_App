package pl.plantoplate.ui.main.recipes.recipeInfo.recyclerViews.viewHolders;

import android.graphics.Paint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.math.BigDecimal;
import java.math.RoundingMode;

import pl.plantoplate.data.remote.models.recipe.Ingredient;
import pl.plantoplate.data.remote.models.user.Role;
import pl.plantoplate.databinding.ItemSkladnikBinding;

public class RecipeIngredientsViewHolder extends RecyclerView.ViewHolder {

    private CheckBox checkBox;
    private TextView ingredientName;
    private TextView ingredientAmount;

    public RecipeIngredientsViewHolder(@NonNull View itemView) {
        super(itemView);

        ItemSkladnikBinding itemSkladnikBinding = ItemSkladnikBinding.bind(itemView);
        checkBox = itemSkladnikBinding.checkBox;
        ingredientName = itemSkladnikBinding.textViewSkladnik;
        ingredientAmount = itemSkladnikBinding.textViewIloscSkladnika;

    }

    public void setOnCheckedChangeListener(CheckBox.OnCheckedChangeListener listener) {
        checkBox.setOnCheckedChangeListener(listener);
    }

    public void bind(Ingredient ingredient, boolean isSelected, Role role) {
        checkBox.setVisibility(role == Role.ROLE_ADMIN ? View.VISIBLE : View.GONE);
        checkBox.setChecked(isSelected);
        float amount = BigDecimal.valueOf(Float.parseFloat(String.valueOf(ingredient.getQuantity())))
                .setScale(3, RoundingMode.HALF_UP)
                .floatValue();
        ingredientName.setText(ingredient.getIngredientName());
        ingredientAmount.setText(amount + " " + ingredient.getUnit());
    }
}
