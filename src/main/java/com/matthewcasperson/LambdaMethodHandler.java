package com.matthewcasperson;

import com.amazonaws.services.lambda.runtime.Context;

import java.lang.reflect.Proxy;
import java.util.Map;

public class LambdaMethodHandler {
    public ProxyResponse handleRequest(final Map<String,Object> input, final Context context) {
        context.getLogger().log("Input: " + input);
        return new ProxyResponse("200", "success");
    }
}
