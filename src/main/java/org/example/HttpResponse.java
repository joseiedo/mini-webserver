package org.example;

import java.util.HashMap;
import java.util.Map;

public class HttpResponse {
    private String version;
    private HttpStatus status;
    private Map<String, String> headers;
    private byte[] body;

    @Override
    public String toString() {
        StringBuilder responseBuilder = new StringBuilder()
                .append(version).append(" ")
                .append(status.code).append(" ")
                .append(status.message).append("\r\n");

        headers.forEach((key, value) ->
                responseBuilder
                        .append(key).append(": ")
                        .append(value).append("\r\n")
        );

        responseBuilder.append("\r\n");

        if (body != null) {
            responseBuilder.append(new String(body));
        }

        return responseBuilder.toString();
    }

    public static HttpResponseBuilder builder() {
        return new HttpResponseBuilder();
    }

    public static class HttpResponseBuilder {
        private String version = "HTTP/1.1"; // Default to HTTP/1.1
        private HttpStatus status;
        private final Map<String, String> headers = new HashMap<>();
        private byte[] body;

        public HttpResponseBuilder version(String version) {
            this.version = version;
            return this;
        }

        public HttpResponseBuilder status(HttpStatus statusCode) {
            this.status = statusCode;
            return this;
        }

        public HttpResponseBuilder header(String key, String value) {
            headers.put(key, value);
            return this;
        }

        public HttpResponseBuilder body(byte[] body) {
            this.body = body;
            return this;
        }

        public HttpResponse build() {
            if (version == null || version.isEmpty()) {
                throw new IllegalArgumentException("Version cannot be null or empty");
            }
            if (status == null) {
                throw new IllegalArgumentException("Status message cannot be null or empty");
            }

            if (this.body != null) {
                headers.put("Content-Length", String.valueOf(this.body.length));
            } else {
                headers.put("Content-Length", "0");
            }

            HttpResponse response = new HttpResponse();
            response.version = this.version;
            response.status = this.status;
            response.headers = this.headers;
            response.body = this.body;
            return response;
        }
    }
}
