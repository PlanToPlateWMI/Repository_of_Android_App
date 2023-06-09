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

package pl.plantoplate.repository.remote.models;

import java.util.ArrayList;

public class ShoppingList {

    private ArrayList<Product> toBuy;
    private ArrayList<Product> bought;

    public ShoppingList() {
    }

    public ShoppingList(ArrayList<Product> toBuy, ArrayList<Product> bought) {
        this.toBuy = toBuy;
        this.bought = bought;
    }

    public ArrayList<Product> getToBuy() {
        return toBuy;
    }

    public void setToBuy(ArrayList<Product> toBuy) {
        this.toBuy = toBuy;
    }

    public ArrayList<Product> getBought() {
        return bought;
    }

    public void setBought(ArrayList<Product> bought) {
        this.bought = bought;
    }
}
