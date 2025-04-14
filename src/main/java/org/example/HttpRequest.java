package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

public class HttpRequest {

    HttpMethod method;
    String target;
    String version;
    Map<String, String> headers = new HashMap<>();
    byte[] body;

    public HttpRequest(InputStream in) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        parseRequestLine(inputStreamReader);
        parseHeaders(inputStreamReader);
        parseBody(inputStreamReader);
    }

    public void parseRequestLine(InputStreamReader reader) throws IOException {
        System.out.println("Parsing request line...");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        for (int nextByte; (nextByte = reader.read()) != -1; ) {
            if (nextByte == '\r') break;
            buffer.write(nextByte);
        }

        String requestLine = buffer.toString(StandardCharsets.UTF_8);
        System.out.println("Request line: " + requestLine);
        String[] pieces = requestLine.split(" ");

        if (pieces.length != 3) {
            throw new RuntimeException("Invalid request line");
        }

        this.method = HttpMethod.fromString(pieces[0]);
        this.target = pieces[1];
        this.version = pieces[2];

        if (!Objects.equals(version, "HTTP/1.1")) {
            throw new IllegalArgumentException("Wrong version! keep using HTTP/1.1 please :)");
        }

        System.out.println("Request line parsed!");
    }

    public void parseHeaders(InputStreamReader reader) throws IOException {
        System.out.println("Parsing headers...");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int nextByte; (nextByte = reader.read()) != -1; ) {
            if (nextByte == '\r') {
                if(buffer.size() == 0) break;
                String line = buffer.toString(StandardCharsets.UTF_8);
                System.out.println("Header: " + line);
                String[] header = line.split(": ");
                String key = header[0];
                String value = header[1];
                this.headers.put(key, value);
                buffer.reset();
            } else if (nextByte != '\n') {
                buffer.write(nextByte);
            }
        }


        // Trick to consume the '\n' character after the '\r' after the headers >:)
        if (reader.read() != '\n') {
            throw new IOException("Malformed header: expected '\\n' after '\\r'");
        }

        System.out.println("All headers parsed!");
    }

    public void parseBody(InputStreamReader reader) throws IOException {
        System.out.println("Parsing body...");
        String headerContentLength = headers.get("Content-Length");
        if (headerContentLength != null) {
            int length = Integer.parseInt(headerContentLength);
            char[] buffer = new char[length];
            int readBytes = reader.read(buffer, 0, length);
            if (readBytes > length){
                throw new RuntimeException("Invalid content length");
            }
            this.body = new String(buffer).getBytes(StandardCharsets.UTF_8);
        } else {
            char[] buffer = new char[1024];
            ByteArrayOutputStream bodyBuffer = new ByteArrayOutputStream();
            for(int readBytes; (readBytes = reader.read(buffer)) != -1;) {
                bodyBuffer.write(new String(buffer, 0, readBytes).getBytes(StandardCharsets.UTF_8));
            }
            this.body = bodyBuffer.toByteArray();
        }
        System.out.println("Body parsed!");
    }

    public String readBodyAsString() {
        return new String(this.body, StandardCharsets.UTF_8);
    }
}
