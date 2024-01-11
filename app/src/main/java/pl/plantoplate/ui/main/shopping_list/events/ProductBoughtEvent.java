package pl.plantoplate.ui.main.shopping_list.events;

/**
 * The event class for the ProductBoughtEvent.
 * It holds the number of products bought.
 */
public class ProductBoughtEvent {

    public final Integer productsCount;

    public ProductBoughtEvent(Integer productsCount) {
        this.productsCount = productsCount;
    }
}
