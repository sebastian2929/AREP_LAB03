package org.example.SpringEci;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SpringECI {
    public static void main(String[] args) throws ClassNotFoundException, MalformedURLException, InvocationTargetException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        if (args.length == 0) {
            System.err.println("No class name provided");
            return;
        }

        Class<?> c = Class.forName(args[0]);
        Map<String, Method> services = new HashMap<>();
        Object instance = c.getDeclaredConstructor().newInstance(); // Create an instance of the class

        // Cargar componentes
        if (c.isAnnotationPresent(RestController.class)) {
            Method[] methods = c.getDeclaredMethods();
            for (Method m : methods) {
                if (m.isAnnotationPresent(GetMapping.class)) {
                    String key = m.getAnnotation(GetMapping.class).value();
                    services.put(key, m);
                }
            }
        }

        // Código quemado para ejemplo
        URL servicesUrl = new URL("http://localhost:8080/App/hello");
        String path = servicesUrl.getPath();
        System.out.println("path: " + path);
        String serviceName = path.substring(1);
        System.out.println("Service name: " + serviceName);

        Method ms = services.get(serviceName);
        if (ms != null) {
            Object result = ms.invoke(instance);
            System.out.println("Respuesta servicio: " + result);
        } else {
            System.out.println("Servicio no encontrado: " + serviceName);
        }
    }
}
