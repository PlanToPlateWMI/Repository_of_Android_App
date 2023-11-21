package pl.plantoplate.ui.main.calendar.recyclerViews.viewHolders;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.plantoplate.databinding.ItemCategoryKalendarzBinding;
import pl.plantoplate.ui.main.calendar.recyclerViews.adapters.ConcreteMealAdapter;
import pl.plantoplate.ui.main.calendar.recyclerViews.listeners.SetupMealItem;
import pl.plantoplate.ui.main.calendar.recyclerViews.models.MealTypes;

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