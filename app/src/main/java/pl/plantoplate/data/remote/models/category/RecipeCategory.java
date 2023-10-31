package pl.plantoplate.data.remote.models.category;

public class RecipeCategory {

    private int id;
    private String name;

    public RecipeCategory() {
    }

    public RecipeCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }
}
