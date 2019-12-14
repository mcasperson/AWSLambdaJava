package com.matthewcasperson;

import java.util.Map;

public class ProxyResponse {
    public final String statusCode;
    public final String body;
    public final Map<String, String> headers;

    public ProxyResponse(final String statusCode, final String body, final Map<String, String> headers) {
        this.headers = headers;
        this.body = body;
        this.statusCode = statusCode;
    }

    public ProxyResponse(final String statusCode, final String body) {
        this.headers = null;
        this.body = body;
        this.statusCode = statusCode;
    }
}
