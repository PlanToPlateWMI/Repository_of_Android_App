package pl.plantoplate.ui.main.shopping_list.events;

public class ProductBoughtEvent {

    public final Integer productsCount;

    public ProductBoughtEvent(Integer productsCount) {
        this.productsCount = productsCount;
    }
}
