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
package pl.plantoplate.requests.sendRegisterData;

import com.google.gson.annotations.SerializedName;

/**

 Represents user information required for API signing up.
 */
public class UserRegisterData {

    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    /**

     Constructs a new UserInfo object.
     @param name user's name
     @param email user's email address
     @param password user's password
     */
    public UserRegisterData(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
    /**

     Returns the user's name.
     @return user's name
     */
    public String getName() {
        return name;
    }
    /**

     Sets the user's name.
     @param name user's name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**

     Returns the user's email address.
     @return user's email address
     */
    public String getEmail() {
        return email;
    }
    /**

     Sets the user's email address.
     @param email user's email address
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**

     Returns the user's password.
     @return user's password
     */
    public String getPassword() {
        return password;
    }
    /**

     Sets the user's password.
     @param password user's password
     */
    public void setPassword(String password) {
        this.password = password;
    }
}