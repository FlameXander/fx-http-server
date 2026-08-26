package ru.flamexander.http.server;

import java.util.Map;

public class HttpRequest {
    private String uri;
    private HttpMethod method;
    private Map<String, String> parameters;
    private Map<String, String> headers;
    private String body;

    public String getRouteKey() {
        return String.format("%s %s", method, uri);
    }

    public String getUri() {
        return uri;
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }

    public String getBody() {
        return body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public HttpRequest(String uri, HttpMethod method, Map<String, String> parameters, Map<String, String> headers, String body) {
        this.uri = uri;
        this.method = method;
        this.parameters = parameters;
        this.headers = headers;
        this.body = body;
    }

    public void log() {
        System.out.println("method: " + method);
        System.out.println("uri: " + uri);
        System.out.println("parameters: " + parameters);
        System.out.println("headers: " + headers);
        System.out.println("body: " + body);
    }
}
