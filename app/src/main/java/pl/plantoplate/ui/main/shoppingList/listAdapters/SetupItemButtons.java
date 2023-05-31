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

package pl.plantoplate.ui.main.shoppingList.listAdapters;

import android.view.View;

import pl.plantoplate.repository.models.Product;

public interface SetupItemButtons {
    default void setupAddToShoppingListButtonClick(View v, Product product) {

    }

    default void setupDeleteProductButtonClick(View v, Product product) {

    }

    default void setupEditProductButtonClick(View v, Product product) {

    }

    default void setupCheckShoppingListButtonClick(View v, Product product) {

    }

    default void setupProductItemClick(View v, Product product) {

    }
}
