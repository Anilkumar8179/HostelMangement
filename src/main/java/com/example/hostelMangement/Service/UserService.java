package com.example.hostelMangement.Service;


import com.example.hostelMangement.Entity.User;
import com.example.hostelMangement.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private static final long EXPIRE_TOKEN=30;


    @Autowired
    private UserRepository userRepository;


    public ResponseEntity<String> login(User user) {
        userRepository.save(user);
        return ResponseEntity.ok("Registered successful");
    }

    public User findByFullnameAndPassword(String fullname, String password) {
        return userRepository.findByFullnameAndPassword(fullname, password);
    }



    public String forgotPass(String email){
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(email));

        if(!userOptional.isPresent()){
            return "Invalid email id.";
        }

        User user=userOptional.get();
        user.setToken(generateToken());
        user.setTokenCreationDate(LocalDateTime.now());

        user=userRepository.save(user);
        return user.getToken();
    }

    public String resetPass(String token, String password){
        Optional<User> userOptional= Optional.ofNullable(userRepository.findByToken(token));

        if(!userOptional.isPresent()){
            return "Invalid token";
        }
        LocalDateTime tokenCreationDate = userOptional.get().getTokenCreationDate();

        if (isTokenExpired(tokenCreationDate)) {
            return "Token expired.";

        }

        User user = userOptional.get();

        user.setPassword(password);
        user.setToken(null);
        user.setTokenCreationDate(null);

        userRepository.save(user);

        return "Your password successfully updated.";
    }

    private String generateToken() {
        StringBuilder token = new StringBuilder();

        return token.append(UUID.randomUUID().toString())
                .append(UUID.randomUUID().toString()).toString();
    }

    private boolean isTokenExpired(final LocalDateTime tokenCreationDate) {

        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(tokenCreationDate, now);

        return diff.toMinutes() >=EXPIRE_TOKEN;
    }
    }



