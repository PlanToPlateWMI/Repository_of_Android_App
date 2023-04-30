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
package pl.plantoplate.requests.joinGroup;

import com.google.gson.annotations.SerializedName;

/**
 * A model class representing data for joining a user to a group.
 */
public class UserJoinGroupData {

    @SerializedName("code")
    private String code;

    @SerializedName("email")
    private String email;

    @SerializedName("password")
    private String password;

    /**
     * Constructs a new UserJoinGroupData object with the given code, email, and password.
     *
     * @param code The code to join the group.
     * @param email The email of the user.
     * @param password The password of the user.
     */
    public UserJoinGroupData(String code, String email, String password) {
        this.code = code;
        this.email = email;
        this.password = password;
    }

    /**
     * Returns the code to join the group.
     *
     * @return The code to join the group.
     */
    public String getCode() {
        return code;
    }

    /**
     * Returns the email of the user.
     *
     * @return The email of the user.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the password of the user.
     *
     * @return The password of the user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the code to join the group.
     *
     * @param code The code to join the group.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Sets the email of the user.
     *
     * @param email The email of the user.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the password of the user.
     *
     * @param password The password of the user.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}

