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
package pl.plantoplate.ui.main.recepies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import io.reactivex.rxjava3.disposables.Disposable;
import pl.plantoplate.data.remote.repository.RecipeRepository;
import pl.plantoplate.databinding.FragmentRecipeInsideAllBinding;
import timber.log.Timber;

public class AllRecipeFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentRecipeInsideAllBinding fragmentRecipeInsideAllBinding = FragmentRecipeInsideAllBinding.inflate(inflater, container, false);

        getRecepies();

        return fragmentRecipeInsideAllBinding.getRoot();
    }

    public void getRecepies(){


        RecipeRepository recipeRepository = new RecipeRepository();

        Disposable disposable = recipeRepository.getAllRecipes("")
                .subscribe(
                        recipes -> {
                        },
                        throwable -> Timber.e(throwable, "Error while getting recipes")
                );
    }

//    public void setupRecyclerView(){
//        RecyclerView recyclerView = fragmentRecipeInsideAllBinding.recipeRecyclerView;
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//    }
}
