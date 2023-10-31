package pl.plantoplate.ui.main.recepies.recyclerViews.viewHolders;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.plantoplate.R;
import pl.plantoplate.ui.main.recepies.recyclerViews.RecipeCategory;
import pl.plantoplate.ui.main.recepies.recyclerViews.adapters.RecipeAdapter;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;

public class RecipeCategoryViewHolder extends RecyclerView.ViewHolder {

    private final TextView recipeCategoryTextView;
    private final RecyclerView recipesRecyclerView;

    public RecipeCategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        recipeCategoryTextView = itemView.findViewById(R.id.сategory_of_product);
        recipesRecyclerView = itemView.findViewById(R.id.product_recycler_view);
    }

    public void bind(RecipeCategory recipeCategory, SetupItemButtons listener) {
        recipeCategoryTextView.setText(recipeCategory.getName());
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        RecipeAdapter recipeAdapter = new RecipeAdapter(recipeCategory.getRecipes());
        recipeAdapter.setUpItemButtons(listener);
        recipesRecyclerView.setAdapter(recipeAdapter);
    }
}
