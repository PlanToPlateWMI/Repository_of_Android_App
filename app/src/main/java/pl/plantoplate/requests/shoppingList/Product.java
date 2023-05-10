package pl.plantoplate.requests.shoppingList;

public class Product {
    private int id;
    private String product;
    private int amount;
    private String unit;

    public int getId() {
        return id;
    }

    public String getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }


}
