package org.example;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class HttpParserTest extends TestCase {

    public void testShouldParseWithNoErrors() throws IOException {
        String request = "POST /path HTTP/1.1\r\n" +
        "Host: localhost\r\n" +
        "User-Agent: curl/7.64.1\r\n" +
        "Accept: */*\r\n" +
        "\r\n" + "Hello, world!";

        InputStream inputStream = new ByteArrayInputStream(request.getBytes());
        HttpParser parser = new HttpParser(inputStream);

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