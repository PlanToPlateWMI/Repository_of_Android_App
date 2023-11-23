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

package pl.plantoplate.data.remote.models.auth;

import com.google.gson.annotations.SerializedName;

/**
 * A class that represents the data required to create a new group.
 */
public class UserCredentials {

    /**
     * The email address of the group creator.
     */
    @SerializedName("email")
    private String email;

    /**
     * The password for the group creator.
     */
    @SerializedName("password")
    private String password;

    /**
     * Creates a new `CreateGroupData` object with the given email and password.
     *
     * @param email    The email address of the group creator.
     * @param password The password for the group creator.
     */
    public UserCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    /**
     * Returns the email address of the group creator.
     *
     * @return The email address of the group creator.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the password for the group creator.
     *
     * @return The password for the group creator.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the email address of the group creator.
     *
     * @param email The new email address to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the password for the group creator.
     *
     * @param password The new password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}

