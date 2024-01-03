package pl.plantoplate.ui.main.calendar.recycler_views.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import pl.plantoplate.R;
import pl.plantoplate.ui.main.calendar.recycler_views.listeners.SetupMealItem;
import pl.plantoplate.ui.main.calendar.recycler_views.models.MealTypes;
import pl.plantoplate.ui.main.calendar.recycler_views.view_holders.MealTypeViewHolder;

public class MealTypesAdapter extends RecyclerView.Adapter<MealTypeViewHolder>{

    private List<MealTypes> mealTypes;
    private SetupMealItem listener;

    public MealTypesAdapter() {
        this.mealTypes = new ArrayList<>();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setMealTypes(List<MealTypes> mealTypes) {
        this.mealTypes = mealTypes;
        notifyDataSetChanged();
    }

    public void setUpMealItem(SetupMealItem listener) {
        this.listener = listener;
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
        holder.bind(mealType, listener);
    }

    @Override
    public int getItemCount() {
        return mealTypes == null ? 0 : mealTypes.size();
    }
}