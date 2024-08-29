package org.example.SpringEci;
@RestController
public class HelloService {

    @GetMapping("/hello")
    public static String hello(){
        return "Hello World";
    }
}
