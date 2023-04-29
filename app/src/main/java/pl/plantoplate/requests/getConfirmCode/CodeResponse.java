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

 This class represents a code response body from the API.
 */
public class CodeResponse {

    @SerializedName("code")
    private String code;

    /**

     Constructor to create a new CodeResponse object.
     @param code The code received from the API response.
     */
    public CodeResponse(String code) {
        this.code = code;
    }

    /**

     Returns the code received from the API response.
     @return The code received from the API response.
     */
    public String getCode() {
        return code;
    }

    /**

     Sets the code received from the API response.
     @param code The code received from the API response.
     */
    public void setCode(String code) {
        this.code = code;
    }
}
