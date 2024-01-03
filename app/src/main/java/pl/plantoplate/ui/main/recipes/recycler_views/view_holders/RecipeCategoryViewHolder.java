package pl.plantoplate.ui.main.recipes.recycler_views.view_holders;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.plantoplate.R;
import pl.plantoplate.ui.main.recipes.recycler_views.RecipeCategory;
import pl.plantoplate.ui.main.recipes.recycler_views.adapters.RecipeAdapter;
import pl.plantoplate.ui.main.recipes.recycler_views.listeners.SetupRecipeButtons;

public class RecipeCategoryViewHolder extends RecyclerView.ViewHolder {

    private final TextView recipeCategoryTextView;
    private final RecyclerView recipesRecyclerView;

    public RecipeCategoryViewHolder(@NonNull View itemView) {
        super(itemView);

        recipeCategoryTextView = itemView.findViewById(R.id.сategory_of_product);
        recipesRecyclerView = itemView.findViewById(R.id.product_recycler_view);
    }

    public void bind(RecipeCategory recipeCategory, SetupRecipeButtons listener) {
        recipeCategoryTextView.setText(recipeCategory.getName());
        recipesRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        RecipeAdapter recipeAdapter = new RecipeAdapter(recipeCategory.getRecipes());
        recipeAdapter.setUpRecipeButtons(listener);
        recipesRecyclerView.setAdapter(recipeAdapter);
    }
}
