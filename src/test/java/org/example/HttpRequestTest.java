package org.example;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class HttpRequestTest {

    @Test
    public void shouldParseWithNoErrors() throws IOException {
        String request = """
                POST /path HTTP/1.1\r
                Host: localhost\r
                User-Agent: curl/7.64.1\r
                Accept: */*\r
                \r
                Hello, world!""";

        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        HttpRequest parser = new HttpRequest(inputStream);

        // Asserting request line...
        assertEquals(HttpMethod.POST, parser.method);
        assertEquals("/path", parser.target);
        assertEquals("HTTP/1.1", parser.version);

        // Asserting headers...
        assertEquals("localhost", parser.headers.get("Host"));
        assertEquals("curl/7.64.1", parser.headers.get("User-Agent"));
        assertEquals("*/*", parser.headers.get("Accept"));

        // Asserting body...
        assertEquals("Hello, world!", parser.readBodyAsString());
    }
}