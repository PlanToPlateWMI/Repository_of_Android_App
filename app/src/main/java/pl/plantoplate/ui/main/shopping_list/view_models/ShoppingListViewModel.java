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
package pl.plantoplate.ui.main.shopping_list.view_models;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

/**
 * This class is used to notify the view that the list of products has changed.
 */
public class ShoppingListViewModel extends AndroidViewModel {

    /**
     * The list of products
     */
    private MutableLiveData<Integer> bougthProductsCount;
    public ShoppingListViewModel(@NonNull Application application) {
        super(application);
        bougthProductsCount = new MutableLiveData<>();
    }

    /**
     * Sets the bougth products count.
     * @param count the list type
     */
    public void setBougthProductsCount(int count) {
        bougthProductsCount.setValue(count);
    }

    /**
     * @return the list type
     */
    public MutableLiveData<Integer> getBougthProductsCount() {
        return bougthProductsCount;
    }

}
