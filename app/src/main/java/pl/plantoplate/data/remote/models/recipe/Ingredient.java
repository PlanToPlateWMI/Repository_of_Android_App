package pl.plantoplate.data.remote.models.recipe;

public class Ingredient {

    private int id;
    private int quantity;
    private String ingredientName;
    private String unit;

    public Ingredient() {
    }

    public Ingredient(int id, int quantity, String ingredientName, String unit) {
        this.id = id;
        this.quantity = quantity;
        this.ingredientName = ingredientName;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
