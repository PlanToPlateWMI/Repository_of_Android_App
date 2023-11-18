package pl.plantoplate.ui.main.calendar.recyclerViews.meal.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pl.plantoplate.R;
import pl.plantoplate.ui.main.calendar.recyclerViews.meal.models.MealTypes;
import pl.plantoplate.ui.main.calendar.recyclerViews.meal.viewHolders.MealTypeViewHolder;

public class MealTypesAdapter extends RecyclerView.Adapter<MealTypeViewHolder>{

    private ArrayList<MealTypes> mealTypes;

    public MealTypesAdapter() {
        this.mealTypes = new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMealTypes(ArrayList<MealTypes> mealTypes) {
        this.mealTypes = mealTypes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MealTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_kalendarz, parent, false);
        return new MealTypeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MealTypeViewHolder holder, int position) {
        MealTypes mealType = mealTypes.get(position);
        holder.bind(mealType);
    }

    @Override
    public int getItemCount() {
        return mealTypes == null ? 0 : mealTypes.size();
    }
}