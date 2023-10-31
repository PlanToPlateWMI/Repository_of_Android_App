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
package pl.plantoplate.data.remote.models.category;

import pl.plantoplate.data.remote.models.category.Level;

public class Recipe {

    private int id;
    private String title;
    private String time;
    private Level level;
    private String image;
    private String categoryName;

    private boolean vege;

    public Recipe() {
    }

    public Recipe(int id, String title, String time, Level level, String image, String categoryName, boolean vege) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.level = level;
        this.image = image;
        this.categoryName = categoryName;
        this.vege = vege;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public Level getLevel() {
        return level;
    }

    public String getImage() {
        return image;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public boolean getVege() {
        return vege;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setVege(boolean vege) {
        this.vege = vege;
    }
}