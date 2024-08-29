package org.example.SpringEci;

@RestController
public class UserService {

    @GetMapping("/user")
    public static String userInfo(){
        return "User Info";
    }
}
