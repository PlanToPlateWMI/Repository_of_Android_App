package pl.plantoplate.ui.main.recipes.recipeInfo.viewModels;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.models.recipe.RecipeInfo;
import pl.plantoplate.data.remote.repository.RecipeRepository;

public class RecipeInfoViewModel extends AndroidViewModel {

    private CompositeDisposable compositeDisposable;
    private MutableLiveData<RecipeInfo> recipeInfo;
    private MutableLiveData<String> responseMessage;

    public RecipeInfoViewModel(@NonNull Application application) {
        super(application);

        compositeDisposable = new CompositeDisposable();
        recipeInfo = new MutableLiveData<>();
        responseMessage = new MutableLiveData<>();
    }

    public MutableLiveData<RecipeInfo> getRecipeInfo() {
        return recipeInfo;
    }

    public MutableLiveData<String> getResponseMessage() {
        return responseMessage;
    }

    public void fetchRecipeInfo(int recipeId){
        RecipeRepository recipeRepository = new RecipeRepository();

        Disposable disposable = recipeRepository.getRecipe(recipeId)
                .subscribe(
                        recipe -> recipeInfo.setValue(recipe),
                        throwable -> responseMessage.setValue(throwable.getMessage())
                );

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}