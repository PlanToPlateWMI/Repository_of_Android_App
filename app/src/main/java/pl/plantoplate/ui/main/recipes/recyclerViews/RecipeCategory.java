package pl.plantoplate.ui.main.recipes.recyclerViews;

import java.util.ArrayList;
import pl.plantoplate.data.remote.models.recipe.Recipe;

public class RecipeCategory {
    private String name;
    private ArrayList<Recipe> recipes;

    public RecipeCategory() {
    }

    public RecipeCategory(String name, ArrayList<Recipe> recipes) {
        this.name = name;
        this.recipes = recipes;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }
}
