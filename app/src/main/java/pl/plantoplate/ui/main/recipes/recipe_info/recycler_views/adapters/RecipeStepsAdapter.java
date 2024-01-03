package pl.plantoplate.ui.main.recipes.recipe_info.recycler_views.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import pl.plantoplate.R;
import pl.plantoplate.ui.main.recipes.recipe_info.recycler_views.view_holders.RecipeStepsViewHolder;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsViewHolder> {

    private List<String> steps;

    public RecipeStepsAdapter(List<String> steps) {
        this.steps = steps;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setStepsList(List<String> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeStepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_krok, parent, false);
        return new RecipeStepsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepsViewHolder holder, int position) {
        String step = steps.get(position);
        holder.bind(step);
    }

    @Override
    public int getItemCount() {
        return steps == null ? 0 : steps.size();
    }
}

