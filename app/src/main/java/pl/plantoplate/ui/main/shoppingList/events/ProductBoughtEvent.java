package pl.plantoplate.ui.main.shoppingList.events;

public class ProductBoughtEvent {

    public final Integer productsCount;

    public ProductBoughtEvent(Integer productsCount) {
        this.productsCount = productsCount;
    }
}
