package org.example.SpringEci;

@RestController
public class WelcomeService {

    @GetMapping("/welcome")
    public static String welcome(){
        return "Welcome to our service";
    }
}
