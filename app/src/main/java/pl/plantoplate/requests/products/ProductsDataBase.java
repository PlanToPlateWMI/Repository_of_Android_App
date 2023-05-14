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
package pl.plantoplate.requests.products;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Class used to represent the products database.
 */
public class ProductsDataBase {

    @SerializedName("general")
    private ArrayList<Product> general;
    @SerializedName("group")
    private ArrayList<Product> group;

    public ProductsDataBase(ArrayList<Product> general_products, ArrayList<Product> group_products) {
        this.general = general_products;
        this.group = group_products;
    }

    public ArrayList<Product> getGeneralProducts() {
        return general;
    }

    public ArrayList<Product> getGroupProducts() {
        return group;
    }
}
