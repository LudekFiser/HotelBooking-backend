package org.gary.garyhotel.controller;

import lombok.RequiredArgsConstructor;
import org.gary.garyhotel.exception.UserAlreadyExistsException;
import org.gary.garyhotel.model.User;
import org.gary.garyhotel.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;

@CrossOrigin("http://localhost:5173")
@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;

    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok("Registration was successful!");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
