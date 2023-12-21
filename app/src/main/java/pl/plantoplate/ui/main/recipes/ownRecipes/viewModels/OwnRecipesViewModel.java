package pl.plantoplate.ui.main.recipes.ownRecipes.viewModels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import java.util.ArrayList;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.repository.RecipeRepository;
import pl.plantoplate.ui.main.recipes.recyclerViews.RecipeCategory;
import pl.plantoplate.utils.CategorySorter;

public class OwnRecipesViewModel extends AndroidViewModel {

    private MutableLiveData<ArrayList<RecipeCategory>> ownRecipes;
    private final CompositeDisposable compositeDisposable;
    private SharedPreferences prefs;

    public OwnRecipesViewModel(@NonNull Application application) {
        super(application);
        ownRecipes = new MutableLiveData<>();
        compositeDisposable = new CompositeDisposable();
        prefs = application.getApplicationContext().getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }

    public MutableLiveData<ArrayList<RecipeCategory>> getOwnRecipes() {
        return ownRecipes;
    }

    public void fetchOwnRecipes() {
        RecipeRepository recipeRepository = new RecipeRepository();

        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = recipeRepository.getOwnRecipes("", token)
                .subscribe(
                        recipes -> {
                            ArrayList<RecipeCategory> allCategories = CategorySorter.sortCategoriesByRecipe(recipes);
                            ownRecipes.setValue(allCategories);
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
