package pl.plantoplate.ui.main.recipes.allRecipes.viewModels;

import android.app.Application;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.models.recipe.Recipe;
import pl.plantoplate.data.remote.repository.RecipeRepository;
import pl.plantoplate.ui.main.recipes.recyclerViews.RecipeCategory;
import pl.plantoplate.utils.CategorySorter;

public class AllRecipesViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<RecipeCategory>> allRecipes;
    private final CompositeDisposable compositeDisposable;

    public AllRecipesViewModel(@NonNull Application application) {
        super(application);
        allRecipes = new MutableLiveData<>();
        compositeDisposable = new CompositeDisposable();
    }

    public MutableLiveData<ArrayList<RecipeCategory>> getAllRecipes() {
        return allRecipes;
    }

    public void fetchAllRecipes(){
        RecipeRepository recipeRepository = new RecipeRepository();

        Disposable disposable = recipeRepository.getAllRecipes("")
                .subscribe(
                        recipes -> {
                            ArrayList<RecipeCategory> allCategories = CategorySorter.sortCategoriesByRecipe(recipes);
                            allRecipes.setValue(allCategories);
                            },
                        throwable -> Toast.makeText(getApplication().getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show()
                );

        compositeDisposable.add(disposable);
    }

//    public void fetchRecipeCategory(String category) {
//        RecipeRepository recipeRepository = new RecipeRepository();
//
//        Disposable disposable = recipeRepository.getAllRecipes(category)
//                .subscribe(
//                        recipes -> {
//                            ArrayList<Recipe> concreteCategoryRecipes = CategorySorter.sortRecipesByName(recipes);
//                            RecipeCategory recipeCategory = new RecipeCategory(category, concreteCategoryRecipes);
//                            recipeCategoryList.put(category, new MutableLiveData<>(recipeCategory));
//                            },
//                        throwable -> Toast.makeText(getApplication().getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show()
//                );
//
//        compositeDisposable.add(disposable);
//    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
