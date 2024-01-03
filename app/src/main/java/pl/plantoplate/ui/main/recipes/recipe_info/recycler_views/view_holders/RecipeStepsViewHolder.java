package pl.plantoplate.ui.main.recipes.recipe_info.recycler_views.view_holders;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pl.plantoplate.databinding.ItemKrokBinding;

public class RecipeStepsViewHolder extends RecyclerView.ViewHolder {

    private final TextView stepTextView;

    public RecipeStepsViewHolder(@NonNull View itemView) {
        super(itemView);

        ItemKrokBinding itemKrokBinding = ItemKrokBinding.bind(itemView);
        stepTextView = itemKrokBinding.textViewKrok;
    }

    public void bind(String step) {
        stepTextView.setText(step.trim());
    }
}