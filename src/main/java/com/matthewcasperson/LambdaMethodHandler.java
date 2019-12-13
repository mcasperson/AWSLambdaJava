package com.matthewcasperson;

import com.amazonaws.services.lambda.runtime.Context;

import java.util.Map;

public class LambdaMethodHandler {
    public String handleRequest(final Map<String,Object> input, final Context context) {
        context.getLogger().log("Input: " + input);
        return "success";
    }
}
