package ru.flamexander.http.server;

import java.io.IOException;
import java.io.InputStream;

public class ByteBuffer {
    private byte[] data;
    private InputStream in;

    private int limit;
    private int position;

    private static final byte CR = (byte)'\r';
    private static final byte LF = (byte)'\n';

    public ByteBuffer() {
        this.data = new byte[8192];
    }

    public void init(InputStream in) {
        this.in = in;
        this.position = 0;
        this.limit = 0;
    }

    public String readUntil(byte symbol) throws IOException {
        checkBuffer();
        for (int i = position; i <= limit; i++) {
            if (data[i] == symbol) {
                String result = new String(data, position, i - position);
                position = i + 1;
                return result;
            }
        }
        return null;
    }

    public String readUntilOr(byte symbol1, byte symbol2) throws IOException {
        checkBuffer();
        for (int i = position; i <= limit; i++) {
            if (data[i] == symbol1 || data[i] == symbol2) {
                String result = new String(data, position, i - position);
                position = i + 1;
                return result;
            }
        }
        return null;
    }

    public String readUntilAnd(byte symbol1, byte symbol2) throws IOException {
        checkBuffer();
        for (int i = position; i <= limit; i++) {
            if (data[i] == symbol1 && data[i + 1] == symbol2) {
                String result = new String(data, position, i - position);
                position = i + 2;
                return result;
            }
        }
        return null;
    }

    public void skipRestOfLine() throws IOException {
        checkBuffer();
        for (int i = position; i <= limit; i++) {
            if (data[i] == CR && data[i + 1] == LF) {
                position = i + 2;
                return;
            }
        }
    }

    public byte getCurrentValue() {
        return data[position];
    }

    public byte getLeftValue() {
        return data[position - 1];
    }

    public boolean checkEmptyLine() {
        return data[position] == CR && data[position + 1] == LF && data[position + 2] == CR && data[position + 3] == LF;
    }

    public void skipByte(int n) {
        position += n;
    }

    public String readBody() {
        StringBuilder sb = new StringBuilder();
        sb.append(new String(data, position, limit - position));
        try {
            while (true) {
                int n = in.read(data);
                System.out.println(n);
                if (n <= 0) {
                    break;
                }
                sb.append(new String(data, 0, n));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return sb.toString();
    }

    private void checkBuffer() throws IOException {
        if (position == limit) {
            position = 0;
            limit = in.read(data);
        }
    }
}
