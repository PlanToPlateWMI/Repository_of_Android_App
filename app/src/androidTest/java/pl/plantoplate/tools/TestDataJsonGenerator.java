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
package pl.plantoplate.tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import pl.plantoplate.data.remote.models.Message;
import pl.plantoplate.data.remote.models.auth.CodeResponse;
import pl.plantoplate.data.remote.models.product.Product;
import pl.plantoplate.data.remote.models.recipe.Level;
import pl.plantoplate.data.remote.models.recipe.Recipe;
import pl.plantoplate.data.remote.models.user.Role;
import pl.plantoplate.data.remote.models.user.UserInfo;

public class TestDataJsonGenerator {

    private TestDataJsonGenerator() {}

    public static String generateResponseCode(int code) {
        CodeResponse codeResponse = new CodeResponse(String.valueOf(code));
        return toJson(codeResponse);
    }

    public static String generateMessage(String message) {
        Message messageResponse = new Message(message);
        return toJson(messageResponse);
    }

    public static String generateRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe(11, "recipe11", "10", Level.EASY, null, "Dania główne", false));
        recipes.add(new Recipe(12, "recipe12", "10", Level.EASY, null, "Dania główne", false));
        recipes.add(new Recipe(2, "recipe2", "20", Level.MEDIUM, null, "Zupy", true));
        recipes.add(new Recipe(3, "recipe3", "30", Level.HARD, null, "Desery", false));
        recipes.add(new Recipe(5, "recipe4", "50", Level.MEDIUM, null, "Przekąski", false));
        recipes.add(new Recipe(4, "recipe5", "40", Level.EASY, null, "Napoje", true));
        return toJson(recipes);
    }

    public static String generateUserInfo(Role userRole) {
        UserInfo userInfo = new UserInfo("user", "user@gmail.com", userRole.toString());
        return toJson(userInfo);
    }

    public static String generateUserInfos() {
        List<UserInfo> userInfos = new ArrayList<>();
        userInfos.add(new UserInfo("user1", "user1@gmail.com", Role.ROLE_ADMIN.toString()));
        userInfos.add(new UserInfo("user1", "user1@gmail.com", Role.ROLE_ADMIN.toString()));
        userInfos.add(new UserInfo("user1", "user1@gmail.com", Role.ROLE_ADMIN.toString()));
        userInfos.add(new UserInfo("user1", "user1@gmail.com", Role.ROLE_ADMIN.toString()));
        userInfos.add(new UserInfo("user1", "user1@gmail.com", Role.ROLE_ADMIN.toString()));
        userInfos.add(new UserInfo("user1", "user1@gmail.com", Role.ROLE_ADMIN.toString()));
        return toJson(userInfos);
    }

    public static String generateProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "product1", "Mięso", 1, "kg"));
        products.add(new Product(2, "product2", "Mięso", 1, "kg"));
        products.add(new Product(3, "product3", "Mięso", 1, "kg"));
        products.add(new Product(4, "product4", "Mięso", 1, "kg"));
        return toJson(products);
    }

    public static String toJson(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try {
            json = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
