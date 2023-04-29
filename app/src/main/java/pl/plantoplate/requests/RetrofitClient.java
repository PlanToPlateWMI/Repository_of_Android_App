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
package pl.plantoplate.requests;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * A singleton class responsible for creating and returning a Retrofit client instance.
 */
public class RetrofitClient {

    // Base URL of the API
    private final String BASE_URL = "http://192.168.0.121:8080";
    // Singleton instance of the class
    private static RetrofitClient instance = null;
    // Interface representing the API endpoints
    private final RestApi api;

    /**
     * Private constructor to create a Retrofit client instance.
     */
    private RetrofitClient() {
        // Create a Retrofit instance with the specified base URL and converter factories
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // Create an implementation of the RestApi interface
        api = retrofit.create(RestApi.class);
    }

    /**
     * Returns the singleton instance of the RetrofitClient class.
     *
     * @return the instance of the RetrofitClient class
     */
    public static synchronized RetrofitClient getInstance() {
        // If no instance exists, create a new one
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    /**
     * Method that returns the interface representing the API endpoints.
     *
     * @return the interface representing the API endpoints
     */
    public RestApi getApi() {
        return api;
    }
}
