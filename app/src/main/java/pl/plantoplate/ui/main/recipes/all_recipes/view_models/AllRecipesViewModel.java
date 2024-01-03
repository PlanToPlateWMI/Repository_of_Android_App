package pl.plantoplate.ui.main.recipes.all_recipes.view_models;

import android.app.Application;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import java.util.List;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.repository.RecipeRepository;
import pl.plantoplate.ui.main.recipes.recycler_views.RecipeCategory;
import pl.plantoplate.utils.CategorySorter;

public class AllRecipesViewModel extends AndroidViewModel {

    private final MutableLiveData<List<RecipeCategory>> allRecipes;
    private final CompositeDisposable compositeDisposable;

    public AllRecipesViewModel(@NonNull Application application) {
        super(application);
        allRecipes = new MutableLiveData<>();
        compositeDisposable = new CompositeDisposable();
    }

    public MutableLiveData<List<RecipeCategory>> getAllRecipes() {
        return allRecipes;
    }

    public void fetchAllRecipes(){
        RecipeRepository recipeRepository = new RecipeRepository();

        Disposable disposable = recipeRepository.getAllRecipes("")
                .subscribe(
                        recipes -> {
                            List<RecipeCategory> allCategories = CategorySorter.sortCategoriesByRecipe(recipes);
                            allRecipes.setValue(allCategories);
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
