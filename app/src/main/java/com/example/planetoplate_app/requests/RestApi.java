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
package com.example.planetoplate_app.requests;

import com.example.planetoplate_app.requests.sendRegisterData.UserInfo;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * An interface representing the API endpoints.
 */
public interface RestApi {

    /**
     * Sends a POST request to the API endpoint to retrieve a confirmation code for user registration.
     *
     * @param info the user information object containing the email and password
     * @return a Call object representing the API request
     */
    @POST("/auth/signup/")
    Call<ResponseBody> getConfirmCode(@Body UserInfo info);
}

