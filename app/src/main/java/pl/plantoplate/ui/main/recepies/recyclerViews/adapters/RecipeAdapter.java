package pl.plantoplate.ui.main.recepies.recyclerViews.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.category.Recipe;
import pl.plantoplate.ui.main.recepies.recyclerViews.viewHolders.RecipeViewHolder;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeViewHolder>{

    private ArrayList<Recipe> recipes;
    private SetupItemButtons listener;

    public RecipeAdapter(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setRecipesList(ArrayList<Recipe> filterlist) {
        recipes = filterlist;
        notifyDataSetChanged();
    }

    public void setUpItemButtons(SetupItemButtons listener) {
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
