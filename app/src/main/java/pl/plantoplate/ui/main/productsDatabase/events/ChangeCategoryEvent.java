package pl.plantoplate.ui.main.productsDatabase.events;

public class ChangeCategoryEvent {

    private final String category;

    public ChangeCategoryEvent(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
