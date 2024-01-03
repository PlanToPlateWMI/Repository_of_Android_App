package pl.plantoplate.ui.main.recipes.recycler_views.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.recipe.Recipe;
import pl.plantoplate.ui.main.recipes.recycler_views.listeners.SetupRecipeButtons;
import pl.plantoplate.ui.main.recipes.recycler_views.view_holders.RecipeViewHolder;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder>{

    private List<Recipe> recipes;
    private SetupRecipeButtons listener;

    public RecipeAdapter(List<Recipe> recipes) {
        this.recipes = recipes;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRecipesList(List<Recipe> filterlist) {
        recipes = filterlist;
        notifyDataSetChanged();
    }

    public void setUpRecipeButtons(SetupRecipeButtons listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Recipe recipe = recipes.get(position);
        holder.bind(recipe, listener);
    }

    @Override
    public int getItemCount() {
        return recipes == null ? 0 : recipes.size();
    }
}
