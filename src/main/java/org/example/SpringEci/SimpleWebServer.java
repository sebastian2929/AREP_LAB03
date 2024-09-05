package org.example.SpringEci;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

public class SimpleWebServer {
    private static Map<String, Method> services = new HashMap<>();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new StaticFileHandler());
        server.createContext("/greeting", new RestHandler(GreetingController.class));
        server.start();
        System.out.println("Server started on http://localhost:8080/");
    }

    static class StaticFileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestURI = exchange.getRequestURI().toString();
            if ("/".equals(requestURI)) {
                requestURI = "/index.html";
            }

            File file = new File("src/main/resources" + requestURI);
            if (file.exists() && !file.isDirectory()) {
                String contentType = "text/html";
                if (requestURI.endsWith(".png")) {
                    contentType = "image/png";
                } else if (requestURI.endsWith(".css")) {
                    contentType = "text/css";
                } else if (requestURI.endsWith(".js")) {
                    contentType = "application/javascript";
                }
                exchange.getResponseHeaders().add("Content-Type", contentType);
                exchange.sendResponseHeaders(200, file.length());
                try (FileInputStream fis = new FileInputStream(file);
                     OutputStream os = exchange.getResponseBody()) {
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = fis.read(buffer)) != -1) {
                        os.write(buffer, 0, read);
                    }
                }
            } else {
                String response = "404 Not Found";
                exchange.sendResponseHeaders(404, response.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            }
        }
    }

    static class RestHandler implements HttpHandler {
        private final Object controllerInstance;

        RestHandler(Class<?> controllerClass) throws Exception {
            this.controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
            loadServices(controllerClass);
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestURI = exchange.getRequestURI().toString();
            Method method = services.get(requestURI);
            String response = "404 Not Found";

            if (method != null) {
                try {
                    response = (String) method.invoke(controllerInstance);
                } catch (Exception e) {
                    response = "500 Internal Server Error";
                    exchange.sendResponseHeaders(500, response.length());
                }
            } else {
                exchange.sendResponseHeaders(404, response.length());
            }

            exchange.getResponseHeaders().add("Content-Type", "text/plain");
            exchange.sendResponseHeaders(200, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }

        private void loadServices(Class<?> controllerClass) throws Exception {
            Method[] methods = controllerClass.getDeclaredMethods();
            for (Method m : methods) {
                if (m.isAnnotationPresent(GetMapping.class)) {
                    String path = m.getAnnotation(GetMapping.class).value();
                    services.put(path, m);
                }
            }
        }
    }
}
