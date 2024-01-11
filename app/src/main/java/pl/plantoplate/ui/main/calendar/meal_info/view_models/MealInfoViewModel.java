/*
 * Copyright 2023 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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

/**
 * This class is responsible for fetching the meal info from the server.
 */
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

    /**
     * This method fetches the meal info from the server.
     * @param mealId the id of the meal
     */
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
