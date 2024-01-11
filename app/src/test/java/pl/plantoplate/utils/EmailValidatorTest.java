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
package pl.plantoplate.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class EmailValidatorTest {

    /**
     * This method tests if the isEmail() method in the Tools class returns true
     * for a valid email address.
     */
    @Test
    public void testValidEmail() {
        assertTrue(EmailValidator.isEmail("test@example.com"));
    }

    /**
     * This method tests if the isEmail() method in the Tools class returns false
     * for an invalid email address.
     */
    @Test
    public void testInvalidEmail() {
        assertFalse(EmailValidator.isEmail("invalid email"));
        assertFalse(EmailValidator.isEmail("invalid+@.email.com"));
        assertFalse(EmailValidator.isEmail("invalid@@gmail.com"));
        assertFalse(EmailValidator.isEmail("invalid@gmail..com"));
        assertFalse(EmailValidator.isEmail("invalid@gma-il.c"));
        assertFalse(EmailValidator.isEmail("_invalid@gmail.c"));
        assertFalse(EmailValidator.isEmail("invalid@gmail.c_"));
        assertFalse(EmailValidator.isEmail("invalid@gmail.c-"));
        assertFalse(EmailValidator.isEmail("-@invalid@gmail.c"));
        assertFalse(EmailValidator.isEmail("invalid@-gmail.c"));
        assertFalse(EmailValidator.isEmail("invalid@@email.com"));
        assertFalse(EmailValidator.isEmail("invalid@.email.com"));
        assertFalse(EmailValidator.isEmail("invalid@domain"));
        assertFalse(EmailValidator.isEmail("invalid@-email.com"));
        assertFalse(EmailValidator.isEmail("invalid@em_ail.com"));
        assertFalse(EmailValidator.isEmail("invalid@.com"));
        assertFalse(EmailValidator.isEmail("invalid@com."));
        assertFalse(EmailValidator.isEmail("invalid@.com."));
        assertFalse(EmailValidator.isEmail("invalid@.com.com"));
        assertFalse(EmailValidator.isEmail("invalid@111.222.333.44444"));
    }

    /**
     * This method tests if the isEmail() method in the Tools class returns false
     * for a null value as an email address.
     */
    @Test
    public void testNullEmail() {
        assertFalse(EmailValidator.isEmail(null));
    }
}
