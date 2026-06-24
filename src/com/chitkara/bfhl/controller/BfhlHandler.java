package com.chitkara.bfhl.controller;

import com.chitkara.bfhl.dto.BfhlRequest;
import com.chitkara.bfhl.dto.BfhlResponse;
import com.chitkara.bfhl.server.JsonParser;
import com.chitkara.bfhl.server.JsonSerializer;
import com.chitkara.bfhl.service.BfhlService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class BfhlHandler implements HttpHandler {

    private final BfhlService service;

    public BfhlHandler(BfhlService service) {
        this.service = service;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // CORS headers
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
        exchange.getResponseHeaders().add("Content-Type", "application/json");

        String method = exchange.getRequestMethod();

        if ("OPTIONS".equalsIgnoreCase(method)) {
            sendResponse(exchange, 200, "{}");
            return;
        }

        if (!"POST".equalsIgnoreCase(method)) {
            sendResponse(exchange, 405, JsonSerializer.errorJson("Method not allowed. Use POST."));
            return;
        }

        String body;
        try (InputStream is = exchange.getRequestBody()) {
            body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }

        try {
            BfhlRequest request   = JsonParser.parseRequest(body);
            BfhlResponse response = service.processData(request);
            sendResponse(exchange, 200, JsonSerializer.toJson(response));
        } catch (IllegalArgumentException e) {
            sendResponse(exchange, 400, JsonSerializer.errorJson(e.getMessage()));
        } catch (Exception e) {
            sendResponse(exchange, 500, JsonSerializer.errorJson("Internal server error: " + e.getMessage()));
        }
    }

    private void sendResponse(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
