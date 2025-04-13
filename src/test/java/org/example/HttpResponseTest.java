package org.example;

import junit.framework.TestCase;

public class HttpResponseTest extends TestCase {

    public void testBuild() {
        HttpResponse response = HttpResponse.builder()
                .version("HTTP/1.1")
                .status(HttpStatus.OK)
                .header("Content-Type", "text/plain")
                .body("Hello, World!".getBytes())
                .build();
        String expected = """
                HTTP/1.1 200 OK\r
                Content-Length: 13\r
                Content-Type: text/plain\r
                \r
                Hello, World!""";

        String result = response.toString();

        assertEquals(expected, result);
    }
}