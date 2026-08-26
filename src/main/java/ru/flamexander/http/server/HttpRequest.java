package ru.flamexander.http.server;

import java.util.Map;

public record HttpRequest(
        String uri,
        HttpMethod method,
        Map<String, String> parameters,
        Map<String, String> headers,
        String body
) {
    public String getRouteKey() {
        return String.format("%s %s", method, uri);
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }
}
