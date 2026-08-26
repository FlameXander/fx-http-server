package ru.flamexander.http.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class HttpServerConfig {
    static class Server {
        private int port;

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

    static class ThreadPool {
        private int size;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }

    static class Parser {
        private Buffer buffer;

        public Buffer getBuffer() {
            return buffer;
        }

        public void setBuffer(Buffer buffer) {
            this.buffer = buffer;
        }
    }

    static class Buffer {
        private int size;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }
    }

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public ThreadPool getThreadPool() {
        return threadPool;
    }

    public void setThreadPool(ThreadPool threadPool) {
        this.threadPool = threadPool;
    }

    public Parser getParser() {
        return parser;
    }

    public void setParser(Parser parser) {
        this.parser = parser;
    }

    private Server server;
    private ThreadPool threadPool;
    private Parser parser;

    public int getPort() {
        return server.port;
    }

    public int getThreadPoolSize() {
        return threadPool.size;
    }

    public int getBufferSize() {
        return parser.buffer.size;
    }

    public static HttpServerConfig load() {
        Path configPath = Path.of("config.yaml");
        if (!Files.exists(configPath)) {
            try {
                Files.copy(Path.of(HttpServerConfig.class.getResource("/default_config.yaml").toURI()), configPath);
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            HttpServerConfig config = mapper.readValue(new File("config.yaml"), HttpServerConfig.class);
            return config;
        } catch (Exception e) {
            throw new RuntimeException("Не удалось прочитать конфигурационный файл", e);
        }
    }
}
