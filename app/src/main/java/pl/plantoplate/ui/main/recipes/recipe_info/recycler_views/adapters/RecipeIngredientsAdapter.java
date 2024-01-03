package pl.plantoplate.ui.main.recipes.recipe_info.recycler_views.adapters;

import android.annotation.SuppressLint;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import pl.plantoplate.R;
import pl.plantoplate.data.remote.models.recipe.Ingredient;
import pl.plantoplate.data.remote.models.user.Role;
import pl.plantoplate.ui.main.recipes.recipe_info.events.IngredientsChangeEvent;
import pl.plantoplate.ui.main.recipes.recipe_info.recycler_views.view_holders.RecipeIngredientsViewHolder;
import timber.log.Timber;

public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsViewHolder>{

    private Role role;
    private List<Ingredient> ingredients;
    private SparseBooleanArray selectedItems;

    public RecipeIngredientsAdapter(Role role) {
        this.role = role;
        this.ingredients = new ArrayList<>();
        this.selectedItems = new SparseBooleanArray();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setIngredientsList(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
        for (int i = 0; i < ingredients.size(); i++) {
            selectedItems.put(i, true);
        }
        EventBus.getDefault().post(new IngredientsChangeEvent(getSelectedIngredients()));
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeIngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_skladnik, parent, false);
        return new RecipeIngredientsViewHolder(itemView);
    }

    public void toggleSelection(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
    }

    public boolean isSelected(int position) {
        return selectedItems.get(position, false);
    }

    public List<Integer> getSelectedIngredients() {
        ArrayList<Integer> selectedIngredients = new ArrayList<>();
        for (int i = 0; i < selectedItems.size(); i++) {
            int position = selectedItems.keyAt(i);
            if (selectedItems.valueAt(i)) {
                selectedIngredients.add(ingredients.get(position).getId());
            }
        }
        return selectedIngredients;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientsViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        Timber.e("Ingredient: %s %s", ingredient.getIngredientName(), position);
        holder.bind(ingredient, isSelected(position), role);
        holder.setOnCheckedChangeListener((compoundButton, b) -> {
            toggleSelection(position);
            EventBus.getDefault().post(new IngredientsChangeEvent(getSelectedIngredients()));
        });
    }

    @Override
    public int getItemCount() {
        return ingredients == null ? 0 : ingredients.size();
    }
}