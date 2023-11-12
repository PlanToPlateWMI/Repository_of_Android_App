package pl.plantoplate.ui.main.recipes.recipeInfo.recyclerViews.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import pl.plantoplate.R;
import pl.plantoplate.ui.main.recipes.recipeInfo.recyclerViews.viewHolders.RecipeStepsViewHolder;

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsViewHolder> {

    private ArrayList<String> steps;

    public RecipeStepsAdapter(ArrayList<String> steps) {
        this.steps = steps;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setStepsList(ArrayList<String> steps) {
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
        //holder.itemView.setOnClickListener(v -> holder.toggleCrossedOut());
    }

    @Override
    public int getItemCount() {
        return steps == null ? 0 : steps.size();
    }
}

