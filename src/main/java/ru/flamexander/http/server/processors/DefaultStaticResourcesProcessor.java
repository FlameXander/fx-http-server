package ru.flamexander.http.server.processors;

import ru.flamexander.http.server.HttpRequest;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DefaultStaticResourcesProcessor implements RequestProcessor {
    @Override
    public void execute(HttpRequest httpRequest, OutputStream output) throws IOException {
        if (httpRequest.uri().length() < 2) {
            return;
        }
        String filename = httpRequest.uri().substring(1);
        Path filePath = Paths.get("static/", filename);
        String fileType = filename.substring(filename.lastIndexOf(".") + 1);
        byte[] fileData = Files.readAllBytes(filePath);

        String contentDisposition = "";
        if (fileType.equals("pdf")) {
            contentDisposition = "Content-Disposition: attachment;filename=" + filename + "\r\n";
        }

        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Length: " + fileData.length + "\r\n" +
                "Cache-Control: no-cache, no-store, must-revalidate\r\n" +
                contentDisposition +
                "\r\n";
        output.write(response.getBytes());
        output.write(fileData);
    }
}
