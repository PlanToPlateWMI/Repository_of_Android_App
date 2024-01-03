package pl.plantoplate.ui.main.recipes.recycler_views;

import java.util.List;
import pl.plantoplate.data.remote.models.recipe.Recipe;

public class RecipeCategory {
    private String name;
    private List<Recipe> recipes;

    public RecipeCategory() {
    }

    public RecipeCategory(String name, List<Recipe> recipes) {
        this.name = name;
        this.recipes = recipes;
    }

    public String getName() {
        return name;
    }

    public List<Recipe> getRecipes() {
        return recipes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }
}
