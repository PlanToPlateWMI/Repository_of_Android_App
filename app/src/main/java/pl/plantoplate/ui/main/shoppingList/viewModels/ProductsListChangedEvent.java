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
package pl.plantoplate.ui.main.shoppingList.viewModels;

import java.util.ArrayList;

import pl.plantoplate.data.remote.models.shoppingList.ListType;
import pl.plantoplate.data.remote.models.product.Product;

public class ProductsListChangedEvent {

    private final ArrayList<Product> itemList;
    private final ListType listType;

    public ProductsListChangedEvent (ArrayList<Product> itemList, ListType listType) {
        this.itemList = itemList;
        this.listType = listType;
    }

    public ArrayList<Product> getItemList() {
        return itemList;
    }

    public ListType getListType() {
        return listType;
    }
}
