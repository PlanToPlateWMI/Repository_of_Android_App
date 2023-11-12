package pl.plantoplate.ui.main.recipes.recipeInfo.recyclerViews.viewHolders;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pl.plantoplate.databinding.ItemRecipeStepBinding;

public class RecipeStepsViewHolder extends RecyclerView.ViewHolder {

    private TextView stepTextView;

    public RecipeStepsViewHolder(@NonNull View itemView) {
        super(itemView);

        ItemRecipeStepBinding itemRecipeStepBinding = ItemRecipeStepBinding.bind(itemView);
        stepTextView = itemRecipeStepBinding.przepis;
    }

    public void bind(String step) {
        stepTextView.setText(step);
    }
}