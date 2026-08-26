package ru.flamexander.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Parser {
    private ThreadLocal<ByteBuffer> buffer;

    private static final byte SPACE = (byte) ' ';
    private static final byte QUESTION = (byte) '?';
    private static final byte CR = (byte) '\r';
    private static final byte LF = (byte) '\n';
    private static final byte AMP = (byte) '&';
    private static final byte EQ = (byte) '=';
    private static final byte COLON = (byte) ':';

    public Parser(int bufferSize) {
        this.buffer = ThreadLocal.withInitial(() -> new ByteBuffer(bufferSize));
    }

    // Request Example:
    // ========================================================
    // POST /demo?a=1&b=2 HTTP/1.1[CR][LF]
    // User-Agent: PostmanRuntime/7.37.3[CR][LF]
    // Accept: */*[CR][LF]
    // Host: localhost:8189[CR][LF]
    // Accept-Encoding: gzip, deflate, br[CR][LF]
    // Connection: keep-alive[CR][LF]
    // Content-Type: application/x-www-form-urlencoded[CR][LF]
    // Content-Length: 29[CR][LF]
    // [CR][LF]
    // email=a%40gmail.com&phone=111

    public HttpRequest parse(InputStream in) throws IOException {
        ByteBuffer buf = buffer.get();
        buf.init(in);
        String s = buf.readUntil(SPACE);
        if (s == null) {
            return null;
        }
        HttpMethod method = HttpMethod.valueOf(s);
        String uri = buf.readUntilOr(SPACE, QUESTION);
        Map<String, String> parameters = null;
        if (buf.getLeftValue() == QUESTION) {
            parameters = new HashMap<>();
            do {
                String key = buf.readUntil(EQ);
                String value = buf.readUntilOr(AMP, SPACE);
                parameters.put(key, value);
            } while (buf.getLeftValue() != SPACE);
        }
        buf.skipRestOfLine();
        Map<String, String> headers = new HashMap<>();
        while (true) {
            String key = buf.readUntilAnd(COLON, SPACE);
            if (key == null) { // TODO простой, но неправильный путь
                break;
            }
            String value = buf.readUntilAnd(CR, LF);
            headers.put(key, value);
        }
        //String body = buf.readBody();
        String body = null;
        return new HttpRequest(uri, method, parameters, headers, body);
    }
}
