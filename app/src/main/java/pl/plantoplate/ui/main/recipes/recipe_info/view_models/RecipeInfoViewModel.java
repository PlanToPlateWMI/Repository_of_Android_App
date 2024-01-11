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
package pl.plantoplate.ui.main.recipes.recipe_info.view_models;

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