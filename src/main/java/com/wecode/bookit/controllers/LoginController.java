package com.wecode.bookit.controllers;

import com.wecode.bookit.dto.LoginRequest;
import com.wecode.bookit.entity.User;
import com.wecode.bookit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

    @Autowired
    private UserRepository userRepository; //Repository

    //Create the tool to check hashes
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {

        // Extract data
        String inputEmail = loginRequest.getemail();
        String inputPassword = loginRequest.getPassword();

        // Find the user in the Database
        Optional<User> userOptional = userRepository.findByEmail(inputEmail);

        // Check if user exists AND if password matches
        if (userOptional.isPresent()) {
            User dbUser = userOptional.get();
            if (passwordEncoder.matches(inputPassword, dbUser.getPasswordHash())) {
                return ResponseEntity.ok("Login successful");
            }
        }

        //If User not found OR Password wrong -> Fail
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }

    @PostMapping("/debugLogin")
    public ResponseEntity<String> debugLogin(@RequestBody LoginRequest loginRequest) {
        System.out.println("Debug Login Attempt: " + loginRequest.getemail());
        return ResponseEntity.ok("Debug received");
    }
}