package org.example;

import java.lang.reflect.Method;

public class JUnit {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Por favor, proporciona el nombre de la clase de prueba.");
            return;
        }

        try {
            Class<?> testClass = Class.forName(args[0]);
            Method[] methods = testClass.getDeclaredMethods();
            int passed = 0;
            int failed = 0;

            for (Method method : methods) {
                if (method.isAnnotationPresent(Test.class)) {
                    try {
                        method.setAccessible(true);
                        method.invoke(null);
                        System.out.println(method.getName() + " pasó.");
                        passed++;
                    } catch (Exception e) {
                        System.out.println(method.getName() + " falló: " + e.getCause());
                        failed++;
                    }
                }
            }

            System.out.println("Pruebas pasadas: " + passed);
            System.out.println("Pruebas fallidas: " + failed);

        } catch (ClassNotFoundException e) {
            System.out.println("La clase especificada no se encontró.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
