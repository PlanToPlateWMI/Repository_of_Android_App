package pl.plantoplate.ui.main.recipes.recipeInfo.recyclerViews.viewHolders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pl.plantoplate.data.remote.models.recipe.Ingredient;
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

    public void bind(Ingredient ingredient) {
        checkBox.setChecked(true);
        ingredientName.setText(ingredient.getIngredientName());
        ingredientAmount.setText(ingredient.getQuantity() + " " + ingredient.getUnit());
    }
}
