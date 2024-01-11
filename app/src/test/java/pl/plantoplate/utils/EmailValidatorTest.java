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
