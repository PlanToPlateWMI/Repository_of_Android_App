package pl.plantoplate.ui.main.recipes.recipeInfo.events;

import java.util.ArrayList;

public class IngredientsChangeEvent {

    private ArrayList<Integer> ingredientsIds;

    public IngredientsChangeEvent(ArrayList<Integer> ingredientsIds) {
        this.ingredientsIds = ingredientsIds;
    }

    public ArrayList<Integer> getData() {
        return ingredientsIds;
    }
}
