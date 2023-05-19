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

import pl.plantoplate.requests.products.Product;

public interface OnProductItemClickListener {
    default void onAddToShoppingListButtonClick(View v, Product product) {

    }

    default void onDeleteProductButtonClick(View v, Product product) {

    }

    default void onEditProductButtonClick(View v, Product product) {

    }

    default void onCheckShoppingListButtonClick(View v, Product product) {

    }

    default void onProductItemClick(View v, Product product) {

    }
}
