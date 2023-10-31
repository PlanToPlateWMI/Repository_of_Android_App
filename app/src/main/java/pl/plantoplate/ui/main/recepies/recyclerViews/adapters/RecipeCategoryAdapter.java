package pl.plantoplate.ui.main.recepies.recyclerViews.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import pl.plantoplate.R;
import pl.plantoplate.ui.main.recepies.recyclerViews.RecipeCategory;
import pl.plantoplate.ui.main.recepies.recyclerViews.viewHolders.RecipeCategoryViewHolder;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;
import timber.log.Timber;

public class RecipeCategoryAdapter extends RecyclerView.Adapter<RecipeCategoryViewHolder>  {

    private SetupItemButtons listener;
    private ArrayList<RecipeCategory> categories;

    public RecipeCategoryAdapter(ArrayList<RecipeCategory> categories) {
        this.categories = categories;
    }

    public void setUpItemButtons(SetupItemButtons listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setCategoriesList(ArrayList<RecipeCategory> filterlist) {

        for (RecipeCategory category : filterlist) {
            Timber.e(category.getName());
        }

        categories = filterlist;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_recipe, parent, false);
        return new RecipeCategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeCategoryViewHolder holder, int position) {
        holder.bind(categories.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return categories == null ? 0 : categories.size();
    }
}
