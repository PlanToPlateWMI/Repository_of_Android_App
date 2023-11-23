package pl.plantoplate.ui.main.recipes.allRecipes.viewModels;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.models.recipe.Recipe;
import pl.plantoplate.data.remote.repository.RecipeRepository;
import pl.plantoplate.ui.main.recipes.recyclerViews.RecipeCategory;
import pl.plantoplate.utils.CategorySorter;

public class AllRecipesViewModel extends AndroidViewModel {

    private HashMap<String, MutableLiveData<RecipeCategory>> recipeCategoryList;
    private final CompositeDisposable compositeDisposable;

    public AllRecipesViewModel(@NonNull Application application) {
        super(application);
        recipeCategoryList = new HashMap<>();
        compositeDisposable = new CompositeDisposable();

    }
    
    public MutableLiveData<RecipeCategory> getRecipeCategory(String category) {
        if (recipeCategoryList.containsKey(category)) {
            return recipeCategoryList.get(category);
        }else {
            throw new IllegalArgumentException("Category not found");
        }
    }

    public void getAllRecipes(){
        RecipeRepository recipeRepository = new RecipeRepository();

        Disposable disposable = recipeRepository.getAllRecipes("")
                .subscribe(
                        recipes -> {
                            ArrayList<RecipeCategory> allCategories = CategorySorter.sortCategoriesByRecipe(recipes);
                            for (RecipeCategory recipeCategory : allCategories) {
                                recipeCategoryList.put(recipeCategory.getName(), new MutableLiveData<>(recipeCategory));
                             }
                            },
                        throwable -> Toast.makeText(getApplication().getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show()
                );

        compositeDisposable.add(disposable);
    }

    public void getCategoryRecepies(String category) {
        RecipeRepository recipeRepository = new RecipeRepository();

        Disposable disposable = recipeRepository.getAllRecipes(category)
                .subscribe(
                        recipes -> {
                            ArrayList<Recipe> concreteCategoryRecipes = CategorySorter.sortRecipesByName(recipes);
                            RecipeCategory recipeCategory = new RecipeCategory(category, concreteCategoryRecipes);
                            recipeCategoryList.put(category, new MutableLiveData<>(recipeCategory));
                            },
                        throwable -> Toast.makeText(getApplication().getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show()
                );

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
