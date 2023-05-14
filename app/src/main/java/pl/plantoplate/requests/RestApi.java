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

import pl.plantoplate.requests.createGroup.CreateGroupData;
import pl.plantoplate.requests.joinGroup.UserJoinGroupData;
import pl.plantoplate.requests.joinGroup.sendRegisterData.UserRegisterData;

import okhttp3.ResponseBody;
import pl.plantoplate.requests.products.Product;
import pl.plantoplate.requests.signin.SignInData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * An interface representing the API endpoints.
 */
public interface RestApi {

    /**
     * Sends a POST request to the API endpoint to get a email confirmation code for user registration (for the first time).
     *
     * @param info the user information object containing the email and password
     * @return a Call object representing the API request
     */
    @POST("api/auth/signup")
    Call<ResponseBody> sendUserRegisterData(@Body UserRegisterData info);

    /**
     * Sends a GET request to the API endpoint to get a new email confirmation code for user registration.
     *
     * @param email the user email address
     * @return a Call object representing the API request
     */
    @GET("api/mail/code")
    Call<ResponseBody> getConfirmCode(@Query("email") String email);

    /**
     * Sends a request to the server to create a new group with the provided data.
     *
     * @param createGroupRequest The data needed to create the group.
     * @return A {@link Call} object that can be used to execute the network request asynchronously and get the response.
     */
    @POST("api/auth/group")
    Call<ResponseBody> createGroup(@Body CreateGroupData createGroupRequest);

    /**
     * Sends a request to the server to add the current user to a group with the provided code.
     *
     * @param userJoinGroupRequest The data needed to join the group.
     * @return A {@link Call} object that can be used to execute the network request asynchronously and get the response.
     */
    @POST("api/invite-codes")
    Call<ResponseBody> joinGroupByCode(@Body UserJoinGroupData userJoinGroupRequest);


    /**
     * Makes a GET request to the "api/invite-codes/" endpoint to generate a group code.
     *
     * @param token The authorization token to include in the request header (current user token).
     * @param role The role to assign to the user who redeems the group code.
     * @return A `Call<ResponseBody>` object representing the asynchronous request.
     */
    @GET("api/invite-codes")
    Call<ResponseBody> generateGroupCode(@Header("Authorization") String token, @Query("role") String role);


    /**
     * Sends a POST request to the API endpoint to sign in the user.
     *
     * @param info the user information object containing the email and password
     * @return a Call object representing the API request
     */
    @POST("api/auth/signin")
    Call<ResponseBody> signinUser(@Body SignInData info);


    /**
     * Sends a reset password request to the server with the given {@link SignInData} information.
     * @param info The {@link SignInData} object containing the user's email address and password reset token.
     * @return A {@link Call} object that wraps a {@link ResponseBody} object, representing the server's response to the reset password request.
     */
    @POST("api/auth/password/reset")
    Call<ResponseBody> resetPassword(@Body SignInData info);

    /**
     * Sends a GET request to the API endpoint to get the user's shopping list.
     *
     * @param token the user's authorization token.
     * @return a Call object representing the API request
     */
    @GET("api/shopping")
    Call<ResponseBody> getShoppingList(@Header("Authorization") String token);

    /**
     * Sends a GET request to the API endpoint to get all general and group products.
     * @param token the user's authorization token.
     * @return a Call object representing the API request
     */
    @GET("api/products")
    Call<ResponseBody> getProducts(@Header("Authorization") String token);

    @POST("api/products")
    Call<ResponseBody> addOwnProduct(@Header("Authorization") String token, @Body Product product);
}

