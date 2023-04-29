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

import org.bouncycastle.crypto.generators.SCrypt;

import java.util.Arrays;
import java.util.Base64;

/**

 A utility class for performing password key stretching using the SCrypt algorithm.
 */
public class SCryptStretcher {

    /**

     Stretch the given password using the SCrypt algorithm with the given salt.
     @param password The password to stretch
     @param salt The salt (choosen String) to use
     @return The stretched password as a string
     */
    public static String stretch(String password, String salt) {
        byte[] saltBytes = salt.getBytes();
        byte[] passwordBytes = password.getBytes();
        byte[] hashedPassword = SCrypt.generate(passwordBytes, saltBytes, 16, 16, 16, 128);
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    /**

     Validate the given password against a previously-stretched hash using the SCrypt algorithm with the given salt.
     @param password The password to validate
     @param hash The previously-stretched hash to validate against
     @param salt The salt used to stretch the hash
     @return True if the password is valid, false otherwise
     */
    public static boolean validate(String password, String hash, String salt) {
        byte[] hashBytes = hash.getBytes();
        byte[] generatedHash = stretch(password, salt).getBytes();
        return Arrays.equals(generatedHash, hashBytes);
    }

}
