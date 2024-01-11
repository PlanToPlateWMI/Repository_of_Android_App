package pl.plantoplate.tools;

import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;

public class MockHelper {

    public static void enqueueResponse(MockWebServer server, Integer statusCode, String object) {
        MockResponse userInfoResponse = new MockResponse()
                .setResponseCode(statusCode)
                .setBody(object);
        server.enqueue(userInfoResponse);
    }
}