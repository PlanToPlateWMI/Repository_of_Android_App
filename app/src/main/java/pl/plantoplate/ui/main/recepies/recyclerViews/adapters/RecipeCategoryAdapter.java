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
        //log item count
        Timber.e("Item count: %s", categories.size());

        if (categories == null) {
            return 0;
        }
        return categories.size();
    }
}