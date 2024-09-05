package org.example.SpringEci;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

public class SpringECI {
    private static Map<String, Method> services = new HashMap<>();

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("No class name provided");
            return;
        }

        Class<?> c = Class.forName(args[0]);
        loadRestControllers(c);

        // Print registered services
        System.out.println("Registered services:");
        for (Map.Entry<String, Method> entry : services.entrySet()) {
            System.out.println("Path: " + entry.getKey() + " Method: " + entry.getValue().getName());
        }
    }

    private static void loadRestControllers(Class<?> c) throws Exception {
        if (c.isAnnotationPresent(RestController.class)) {
            Method[] methods = c.getDeclaredMethods();
            for (Method m : methods) {
                if (m.isAnnotationPresent(GetMapping.class)) {
                    String path = m.getAnnotation(GetMapping.class).value();
                    services.put(path, m);
                }
            }
        }
    }
}
