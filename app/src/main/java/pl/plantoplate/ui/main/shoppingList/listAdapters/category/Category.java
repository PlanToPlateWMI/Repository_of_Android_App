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

package pl.plantoplate.ui.main.shoppingList.listAdapters.category;

import java.util.ArrayList;

import pl.plantoplate.repository.remote.models.Product;

public class Category {
    private int id;
    private String name;
    private ArrayList<Product> products;

    public Category() {
    }

    public Category(int id, String name, ArrayList<Product> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    public int getId(){
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}