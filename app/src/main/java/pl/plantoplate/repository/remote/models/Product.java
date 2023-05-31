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

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Class representing a product returned from the API server.
 */
@Entity(tableName = "products")
public class Product {

    @PrimaryKey
    private int id;
    private String name;
    private String category;
    private float amount;
    private String unit;
    private boolean isOwn = false;

    public Product() {
    }

    public Product(int id, String name, String category, float amount, String unit, boolean isOwn) { 
        this.id = id;
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.unit = unit;
        this.isOwn = isOwn;
    }

    public Product(int id, String name, String category, int amount, String unit) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.unit = unit;
    }

    public Product(int id, String name, String category, String unit) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.amount = 0;
        this.unit = unit;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public float getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}
