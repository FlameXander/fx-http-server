package ru.flamexander.http.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.flamexander.http.server.application.Storage;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private HttpServerConfig config;
    private Dispatcher dispatcher;
    private Parser parser;
    private ExecutorService executorService;

    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class.getName());

    public HttpServer() {
        this.config = HttpServerConfig.load();
    }

    public void start() {
        executorService = Executors.newFixedThreadPool(config.threadPool().size());
        try (ServerSocket serverSocket = new ServerSocket(config.server().port())) {
            logger.info("Сервер запущен на порту: {}", config.server().port());
            this.dispatcher = new Dispatcher();
            this.parser = new Parser(config.parser().buffer().size());
            Storage.init();
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.execute(() -> executeRequest(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executorService.shutdown();
        }
    }

    private void executeRequest(Socket socket) {
        try (socket) {
            HttpRequest request = parser.parse(socket.getInputStream());
            logger.debug("Получен запрос {} {} {} {} {}", request.method(), request.uri(), request.parameters(), request.headers(), request.body());
            if (request == null) {
                return;
            }
            dispatcher.execute(request, socket.getOutputStream());
            socket.getOutputStream().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
