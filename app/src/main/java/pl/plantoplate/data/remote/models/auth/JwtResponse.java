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
 * A class that represents a JSON response from a server that includes a JSON Web Token (JWT)
 * and the associated user role.
 */
public class JwtResponse {

    /**
     * The JWT included in the response.
     */
    @SerializedName("token")
    private String token;

    /**
     * The role associated with the user.
     */
    @SerializedName("role")
    private String role;

    /**
     * Creates a new `JwtResponse` object with the given token and role.
     *
     * @param token The JWT included in the response.
     * @param role  The role associated with the user.
     */
    public JwtResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }

    public JwtResponse() {
    }

    /**
     * Returns the JWT included in the response.
     *
     * @return The JWT included in the response.
     */
    public String getToken() {
        return token;
    }

    /**
     * Returns the role associated with the user.
     *
     * @return The role associated with the user.
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the JWT included in the response.
     *
     * @param token The new JWT to set.
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Sets the role associated with the user.
     *
     * @param role The new role to set.
     */
    public void setRole(String role) {
        this.role = role;
    }
}
