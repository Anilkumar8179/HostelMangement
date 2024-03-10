package com.example.hostelMangement.Controller;

import com.example.hostelMangement.Entity.User;
import com.example.hostelMangement.Service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody User user) {
        return userService.login(user);
    }


    @GetMapping("/findUser")
    public ResponseEntity<String> findUser(@RequestBody User user) {
        String fullname = user.getFullname();
        String password = user.getPassword();
        User byFullnameAndPassword = userService.findByFullnameAndPassword(fullname, password);

        logger.info("Searching for user with fullname: {} and password: {}", fullname, password);

        if (byFullnameAndPassword!=null) {
            logger.info("login sucssfully");
            return ResponseEntity.ok("Succssfully logined"); // Return user data with status 200 (OK)

        } else {
            logger.info("not Login");
            return ResponseEntity.notFound().build(); // Return 404 (Not Found) if user is not found
        }

    }
    @PostMapping("/forgot-password")
    public ResponseEntity <String>forgotPass(@RequestParam String fullname){
        String response = userService.forgotPass(fullname);

        if(!response.startsWith("Invalid")){
            response= "http://localhost:8080/reset-password?token=" + response;
        }
        return ResponseEntity.ok("forrgetpassword");
    }

    @PutMapping("/reset-password")
    public String resetPass(@RequestParam String token, @RequestParam String password){
        return userService.resetPass(token,password);
    }

}