package pl.plantoplate.ui.main.products_database.events;

/**
 * This class is responsible for changing the category.
 */
public class ChangeCategoryEvent {

    private final String category;

    public ChangeCategoryEvent(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
