package pl.plantoplate.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class AppStateHandlerTest {

    @Test
    public void testSaveAppState() {
        AppStateHandler stateHandler = new AppStateHandler();

        // Test saving different states
        stateHandler.saveAppState(ApplicationState.LOGIN);
        assertEquals(ApplicationState.LOGIN, stateHandler.getCurrentState());

        stateHandler.saveAppState(ApplicationState.REGISTER);
        assertEquals(ApplicationState.REGISTER, stateHandler.getCurrentState());

        // Test saving the same state multiple times
        stateHandler.saveAppState(ApplicationState.REGISTER);
        assertEquals(ApplicationState.REGISTER, stateHandler.getCurrentState());

        // Test saving null state
        stateHandler.saveAppState(null);
        assertNull(stateHandler.getCurrentState());

        // Test initial state is null
        AppStateHandler stateHandlerWithNullState = new AppStateHandler();
        assertNull(stateHandlerWithNullState.getCurrentState());
    }
}