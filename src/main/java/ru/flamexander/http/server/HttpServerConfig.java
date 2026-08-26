package ru.flamexander.http.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

record HttpServerConfig(
        Server server,
        ThreadPool threadPool,
        Parser parser
) {
    record Server(int port) {
    }

    record ThreadPool(int size) {
    }

    record Parser(Buffer buffer) {
    }

    record Buffer(int size) {
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
