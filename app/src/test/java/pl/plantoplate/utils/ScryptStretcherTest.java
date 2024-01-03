package pl.plantoplate.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class ScryptStretcherTest {

    @Test
    public void testStretchWithSameInputs() {
        String password = "password";
        String salt = "salt";

        String stretchedPassword1 = SCryptStretcher.stretch(password, salt);
        String stretchedPassword2 = SCryptStretcher.stretch(password, salt);

        assertNotNull(stretchedPassword1);
        assertNotNull(stretchedPassword2);
        assertEquals(stretchedPassword1, stretchedPassword2);
    }

    @Test
    public void testStretchWithDifferentSalts() {
        String password = "password";
        String salt1 = "salt1";
        String salt2 = "salt2";

        String stretchedPassword1 = SCryptStretcher.stretch(password, salt1);
        String stretchedPassword2 = SCryptStretcher.stretch(password, salt2);

        assertNotNull(stretchedPassword1);
        assertNotNull(stretchedPassword2);
        assertNotEquals(stretchedPassword1, stretchedPassword2);
    }

    @Test
    public void testStretchWithDifferentPasswords() {
        String password1 = "password1";
        String password2 = "password2";
        String salt = "salt";

        String stretchedPassword1 = SCryptStretcher.stretch(password1, salt);
        String stretchedPassword2 = SCryptStretcher.stretch(password2, salt);

        assertNotNull(stretchedPassword1);
        assertNotNull(stretchedPassword2);
        assertNotEquals(stretchedPassword1, stretchedPassword2);
    }

    @Test
    public void testStretchWithEmptyInputs() {
        String password = "";
        String salt = "";

        String stretchedPassword = SCryptStretcher.stretch(password, salt);

        assertNotNull(stretchedPassword);
        assertTrue(stretchedPassword.isEmpty());
    }

    @Test
    public void testStretchWithNullInputs() {
        String password = null;
        String salt = null;

        String stretchedPassword = SCryptStretcher.stretch(password, salt);

        assertNotNull(stretchedPassword);
        assertTrue(stretchedPassword.isEmpty());
    }

    @Test
    public void testStretchWithNullPassword() {
        String password = "";
        String salt = null;

        String stretchedPassword = SCryptStretcher.stretch(password, salt);

        assertNotNull(stretchedPassword);
        assertTrue(stretchedPassword.isEmpty());
    }

    @Test
    public void testStretchWithNullSalt() {
        String password = null;
        String salt = "";

        String stretchedPassword = SCryptStretcher.stretch(password, salt);

        assertNotNull(stretchedPassword);
        assertTrue(stretchedPassword.isEmpty());
    }
}
