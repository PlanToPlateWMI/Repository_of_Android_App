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
package pl.plantoplate.ui.main.recipes.recipe_info.events;

import java.util.List;

/**
 * This class is responsible for the event of changing the ingredients.
 */
public class IngredientsChangeEvent {

    private List<Integer> ingredientsIds;

    public IngredientsChangeEvent(List<Integer> ingredientsIds) {
        this.ingredientsIds = ingredientsIds;
    }

    public List<Integer> getData() {
        return ingredientsIds;
    }
}
