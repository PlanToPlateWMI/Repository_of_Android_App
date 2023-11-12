package pl.plantoplate.ui.main.recipes.recipeInfo.recyclerViews.viewHolders;

import android.graphics.Paint;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import pl.plantoplate.databinding.ItemKrokBinding;

public class RecipeStepsViewHolder extends RecyclerView.ViewHolder {

    private TextView stepTextView;
    //private boolean crossedOut = false;

    public RecipeStepsViewHolder(@NonNull View itemView) {
        super(itemView);

        ItemKrokBinding itemKrokBinding = ItemKrokBinding.bind(itemView);
        stepTextView = itemKrokBinding.textViewKrok;
    }

    public void bind(String step) {
        stepTextView.setText(step);
    }

//    public void toggleCrossedOut() {
//        crossedOut = !crossedOut;
//        stepTextView.setPaintFlags(crossedOut ?
//                stepTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG :
//                stepTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
//    }
}
