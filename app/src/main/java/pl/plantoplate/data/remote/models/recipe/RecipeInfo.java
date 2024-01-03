package pl.plantoplate.data.remote.models.recipe;

import java.util.List;

public class RecipeInfo {

    private int id;
    private String title;
    private String image;
    private String source;
    private String time;
    private Level level;
    private int portions;
    private List<String> steps;
    private List<Ingredient> ingredients;
    private boolean vege;
    private int recipeId;

    public RecipeInfo() {}

    public RecipeInfo(int id, String title, String image, String source, String time, Level level, int portions, List<String> steps, List<Ingredient> ingredients, boolean vege, int recipeId) {
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

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
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