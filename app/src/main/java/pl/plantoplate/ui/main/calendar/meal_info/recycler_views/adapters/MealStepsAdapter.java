package pl.plantoplate.ui.main.calendar.meal_info.recycler_views.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import pl.plantoplate.R;
import pl.plantoplate.ui.main.calendar.meal_info.recycler_views.view_holders.MealStepsViewHolder;

public class MealStepsAdapter extends RecyclerView.Adapter<MealStepsViewHolder> {

    private List<String> steps;

    public MealStepsAdapter(List<String> steps) {
        this.steps = steps;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setStepsList(List<String> steps) {
        this.steps = steps;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MealStepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_krok, parent, false);
        return new MealStepsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MealStepsViewHolder holder, int position) {
        String step = steps.get(position);
        holder.bind(step);
    }

    @Override
    public int getItemCount() {
        return steps == null ? 0 : steps.size();
    }
}