package pl.plantoplate.data.remote.models.meal;

public enum MealType {
    BREAKFAST("Śniadanie"),
    LUNCH("Obiad"),
    DINNER("Kolacja");

    private final String polishName;

    MealType(String polishName) {
        this.polishName = polishName;
    }

    public String getPolishName() {
        return polishName;
    }
}