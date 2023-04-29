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
package com.example.plantoplate.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {

    // Define the email regex pattern
    private static final String emailRegex = "^[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$";

    /**
     * This method checks if a given string is a valid email address.
     *
     * @param str the string to check
     * @return true if the string is a valid email address, false otherwise
     */
    public static boolean isEmail(String str) {

        // If the email is null, return false
        if (str == null) {
            return false;
        }

        // Compile the regex pattern into a Pattern object
        Pattern pattern = Pattern.compile(emailRegex);

        // Match the email string against the regex pattern using a Matcher object
        Matcher matcher = pattern.matcher(str);

        return matcher.matches();
    }
}
