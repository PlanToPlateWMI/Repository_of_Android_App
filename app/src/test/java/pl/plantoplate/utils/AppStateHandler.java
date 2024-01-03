package pl.plantoplate.utils;

import org.junit.Test;

public class AppStateHandler implements ApplicationStateController {
    private ApplicationState currentState;

    @Override
    public void saveAppState(ApplicationState applicationState) {
        // Save the application state
        this.currentState = applicationState;
    }

    public ApplicationState getCurrentState() {
        return currentState;
    }
}
