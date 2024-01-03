package pl.plantoplate.ui.main.recipes.recycler_views.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.plantoplate.R;
import pl.plantoplate.ui.main.recipes.recycler_views.RecipeCategory;
import pl.plantoplate.ui.main.recipes.recycler_views.listeners.SetupRecipeButtons;
import pl.plantoplate.ui.main.recipes.recycler_views.view_holders.RecipeCategoryViewHolder;
import timber.log.Timber;

public class RecipeCategoryAdapter extends RecyclerView.Adapter<RecipeCategoryViewHolder>  {

    private SetupRecipeButtons listener;
    private List<RecipeCategory> categories;

    public RecipeCategoryAdapter(List<RecipeCategory> categories) {
        this.categories = categories;
    }

    public void setUpRecipeButtons(SetupRecipeButtons listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCategoriesList(List<RecipeCategory> filterlist) {
        categories = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Timber.e("onCreateViewHolder");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_recipe, parent, false);
        return new RecipeCategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeCategoryViewHolder holder, int position) {
        holder.bind(categories.get(position), listener);
        Timber.e(categories.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }
}