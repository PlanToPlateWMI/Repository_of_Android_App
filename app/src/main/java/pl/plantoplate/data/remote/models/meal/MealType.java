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
package pl.plantoplate.data.remote.models.meal;

public enum MealType {
    BREAKFAST("Śniadanie"),
    LUNCH("Obiad"),
    DINNER("Kolacja");

    private final String polishName;

    MealType(String polishName) {
        this.polishName = polishName;
    }

    public String getPolishName() {
        return polishName;
    }

    public static MealType fromString(String text) {
        for (MealType mealType : MealType.values()) {
            if (mealType.polishName.equalsIgnoreCase(text)) {
                return mealType;
            }
        }
        return BREAKFAST;
    }
}