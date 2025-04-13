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

        assertEquals("HTTP/1.1", response.toString().split(" ")[0]);
        assertTrue(response.toString().contains("200 OK"));
        assertTrue(response.toString().contains("Content-Type: text/plain"));
        assertTrue(response.toString().contains("Hello, World!"));
    }
}