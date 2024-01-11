package pl.plantoplate.ui.main.calendar.recycler_views.view_holders;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.plantoplate.databinding.ItemCategoryKalendarzBinding;
import pl.plantoplate.ui.main.calendar.recycler_views.adapters.ConcreteMealAdapter;
import pl.plantoplate.ui.main.calendar.recycler_views.listeners.SetupMealItem;
import pl.plantoplate.ui.main.calendar.recycler_views.models.MealTypes;

/**
 * ViewHolder class for displaying meal types in a RecyclerView.
 */
public class MealTypeViewHolder extends RecyclerView.ViewHolder{

    private final TextView mealTypeTextView;
    private final RecyclerView mealsRecyclerView;

    public MealTypeViewHolder(@NonNull View itemView) {
        super(itemView);

        ItemCategoryKalendarzBinding binding = ItemCategoryKalendarzBinding.bind(itemView);
        mealTypeTextView = binding.categoryOfProduct;
        mealsRecyclerView = binding.productRecyclerView;
    }

    public void bind(MealTypes mealType, SetupMealItem listener) {
        mealTypeTextView.setText(mealType.getMealType().getPolishName());

        ConcreteMealAdapter concreteMealAdapter = new ConcreteMealAdapter();
        concreteMealAdapter.setMeals(mealType.getMeals());
        concreteMealAdapter.setUpMealItem(listener);
        mealsRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));
        mealsRecyclerView.setAdapter(concreteMealAdapter);
    }
}