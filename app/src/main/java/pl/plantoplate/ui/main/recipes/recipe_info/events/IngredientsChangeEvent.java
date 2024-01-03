package pl.plantoplate.ui.main.recipes.recipe_info.events;

import java.util.List;

public class IngredientsChangeEvent {

    private List<Integer> ingredientsIds;

    public IngredientsChangeEvent(List<Integer> ingredientsIds) {
        this.ingredientsIds = ingredientsIds;
    }

    public List<Integer> getData() {
        return ingredientsIds;
    }
}
