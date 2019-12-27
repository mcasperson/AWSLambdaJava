package com.matthewcasperson;

import com.amazonaws.services.lambda.runtime.Context;
import com.google.gson.Gson;

import java.util.Map;

public class LambdaMethodHandler {
    public ProxyResponse handleRequest(final Map<String,Object> input, final Context context) {
        return new ProxyResponse("200", new Gson().toJson(input));
    }
}
