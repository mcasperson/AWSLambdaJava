package com.matthewcasperson;

import com.amazonaws.services.lambda.runtime.Context;

public class LambdaMethodHandler {
    public String handleRequest(final String input, final Context context) {
        context.getLogger().log("Input: " + input);
        return "Hello World - " + input;
    }
}
