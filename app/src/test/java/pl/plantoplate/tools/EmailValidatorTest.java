package pl.plantoplate.tools;

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
