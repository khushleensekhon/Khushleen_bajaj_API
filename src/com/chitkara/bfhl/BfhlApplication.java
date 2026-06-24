package com.chitkara.bfhl;

import com.chitkara.bfhl.controller.BfhlHandler;
import com.chitkara.bfhl.service.BfhlService;
import com.chitkara.bfhl.service.BfhlServiceImpl;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class BfhlApplication {

    // ── Update these before submission ────────────────────────────────────────
    private static final String FULL_NAME   = System.getenv().getOrDefault("USER_FULL_NAME",   "john doe");
    private static final String DOB         = System.getenv().getOrDefault("USER_DOB",         "17091999");
    private static final String EMAIL       = System.getenv().getOrDefault("USER_EMAIL",       "john@xyz.com");
    private static final String ROLL_NUMBER = System.getenv().getOrDefault("USER_ROLL_NUMBER", "ABCD123");
    // ─────────────────────────────────────────────────────────────────────────

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));

        BfhlService service = new BfhlServiceImpl(FULL_NAME, DOB, EMAIL, ROLL_NUMBER);
        BfhlHandler handler = new BfhlHandler(service);

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/bfhl", handler);
        server.setExecutor(Executors.newVirtualThreadPerTaskExecutor()); // Java 21 virtual threads
        server.start();

        System.out.println("BFHL API running on port " + port);
        System.out.println("POST http://localhost:" + port + "/bfhl");
    }
}
