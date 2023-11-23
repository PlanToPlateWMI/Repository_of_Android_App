package pl.plantoplate.data.remote.models.recipe;

import java.util.ArrayList;

public class RecipeInfo {

    private int id;
    private String title;
    private String image;
    private String source;
    private String time;
    private Level level;
    private int portions;
    private ArrayList<String> steps;
    private ArrayList<Ingredient> ingredients;
    private boolean vege;
    private int recipeId;

    public RecipeInfo() {
    }

    public RecipeInfo(int id,
                      String title,
                      String image,
                      String source,
                      String time,
                      Level level,
                      int portions,
                      ArrayList<String> steps,
                      ArrayList<Ingredient> ingredients,
                      boolean vege,
                      int recipeId) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.source = source;
        this.time = time;
        this.level = level;
        this.portions = portions;
        this.steps = steps;
        this.ingredients = ingredients;
        this.vege = vege;
        this.recipeId = recipeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getPortions() {
        return portions;
    }

    public void setPortions(int portions) {
        this.portions = portions;
    }

    public ArrayList<String> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<String> steps) {
        this.steps = steps;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public boolean isVege() {
        return vege;
    }

    public void setVege(boolean vege) {
        this.vege = vege;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }
}