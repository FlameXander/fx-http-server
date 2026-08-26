package ru.flamexander.http.server.application.processors;

import com.google.gson.Gson;
import ru.flamexander.http.server.HttpRequest;
import ru.flamexander.http.server.application.Item;
import ru.flamexander.http.server.application.Storage;
import ru.flamexander.http.server.processors.RequestProcessor;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class GetAllProductsProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        List<Item> items = Storage.getItems();
        Gson gson = new Gson();
        String result = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: application/json\r\n" +
                "Connection: keep-alive\r\n" +
                "Cache-Control: public, max-age=3600\r\n" +
                "Access-Control-Allow-Origin: *\r\n\r\n" + gson.toJson(items);
        output.write(result.getBytes(StandardCharsets.UTF_8));
        output.flush();
    }
}
