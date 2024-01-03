package pl.plantoplate.ui.main.calendar.meal_info.view_models;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.models.recipe.RecipeInfo;
import pl.plantoplate.data.remote.repository.MealRepository;

public class MealInfoViewModel extends AndroidViewModel {

    private final CompositeDisposable compositeDisposable;
    private final MutableLiveData<RecipeInfo> mealInfo;
    private final MutableLiveData<String> responseMessage;
    private final SharedPreferences prefs;

    public MealInfoViewModel(@NonNull Application application) {
        super(application);
        prefs = getApplication().getSharedPreferences("prefs", 0);

        compositeDisposable = new CompositeDisposable();
        mealInfo = new MutableLiveData<>();
        responseMessage = new MutableLiveData<>();
    }

    public MutableLiveData<RecipeInfo> getMealInfo() {
        return mealInfo;
    }

    public MutableLiveData<String> getResponseMessage() {
        return responseMessage;
    }

    public void fetchMealInfo(int mealId){
        MealRepository mealRepository = new MealRepository();
        String token = "Bearer " + prefs.getString("token", "");

        Disposable disposable = mealRepository.getMealDetailsById(token, mealId)
                .subscribe(
                        mealInfo::setValue,
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
