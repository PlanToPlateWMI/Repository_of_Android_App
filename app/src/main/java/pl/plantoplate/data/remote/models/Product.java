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
package pl.plantoplate.data.remote.models;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

/**
 * Class representing a product returned from the API server.
 */
@Entity(tableName = "products")
public class Product implements Parcelable, Serializable {

    @PrimaryKey
    private int id;
    private String name;
    private String category;
    private float amount;
    private String unit;

    public Product() {
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

    public Product(Parcel in) {
        id = in.readInt();
        name = in.readString();
        category = in.readString();
        amount = in.readFloat();
        unit = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(category);
        dest.writeFloat(amount);
        dest.writeString(unit);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

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
