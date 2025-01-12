package ru.radya.ycfunc;

import java.util.HashMap;
import java.util.Map;
import yandex.cloud.sdk.functions.Context;
import yandex.cloud.sdk.functions.YcFunction;

public class Handler implements YcFunction<Request, Response> {

    private Integer statusCode = 200;
    private Boolean isBase64Encoded = false;

    @Override
    public Response handle(Request request, Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "text/html");

        String name = request.queryStringParameters.get("name");
        return new Response(statusCode, headers, isBase64Encoded, String.format("Hello, %s!", context.getTokenJson()));
    }
}

class Request {
    Map<String, String> queryStringParameters;
}

class Response {
    private int statusCode;
    private Map<String, String> headers;
    private Boolean isBase64Encoded;
    private String body;

    public Response(int statusCode, Map<String, String> headers, Boolean isBase64Encoded, String body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.isBase64Encoded = isBase64Encoded;
        this.body = body;
    }
}