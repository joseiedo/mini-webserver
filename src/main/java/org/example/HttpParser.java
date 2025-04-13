package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class HttpParser {

    HttpMethod method;
    String target;
    String version;
    Map<String, String> headers = new HashMap<>();

    Logger logger = Logger.getLogger(this.getClass().getName());

    public HttpParser(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        parseRequestLine(reader);
        parseHeaders(reader);
    }

    public void parseRequestLine(BufferedReader reader) throws IOException {
        logger.info("Parsing request line...");
        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            throw new IOException("Invalid HTTP request line");
        }

        logger.info(requestLine);

        String[] pieces = requestLine.split(" ");
        if (pieces.length != 3) {
            throw new IOException("Invalid HTTP request line");
        }

        this.method = HttpMethod.fromString(pieces[0]);
        this.target = pieces[1];
        this.version = pieces[2];

        logger.info("Request line parsed!");
    }

    public void parseHeaders(BufferedReader reader) throws IOException {
        logger.info("Parsing headers...");
        for(String line; !(line = reader.readLine()).isEmpty();){
            System.out.println(line);
            String[] header = line.split(": ");
            String key = header[0];
            String value = header[1];
            headers.put(key, value);
        }
        logger.info("All headers parsed!");
    }
}
