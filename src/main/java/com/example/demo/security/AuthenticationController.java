package com.example.demo.security;

import com.example.demo.enums.Role;
import com.example.demo.exceptions.ErrorResponse;
import com.example.demo.exceptions.InvalidRoleException;
import com.example.demo.exceptions.UserNotConfirmedException;
import com.example.demo.request.AuthenticationRequest;
import com.example.demo.request.RegistrationRequest;
import com.example.demo.response.AuthenticationResponse;
import com.example.demo.response.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequest registrationRequest) throws InvalidRoleException {
        return ResponseEntity.ok(authenticationService.register(registrationRequest));
    }

    @PostMapping("/login")
    ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
        } catch (UserNotConfirmedException e) {
            return new ResponseEntity<>(new ErrorResponse("UserNotConfirmedException", e.getMessage()), HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout/{id}")
    public void logout(HttpServletRequest httpRequest, @PathVariable Long id) {
        authenticationService.logout(httpRequest, id);
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirmRegistration (@RequestParam Long id, @RequestParam String token, @RequestParam Role role) {
        if (authenticationService.confirmRegistration(id, token, role)) {
            return new ResponseEntity<>(new GenericResponse("Utente verificato con successo. "),HttpStatus.OK);
        }
        return new ResponseEntity<>(new ErrorResponse("UtenteNotConfirmedExcception","non è possibile verificare l'utente"), HttpStatus.BAD_REQUEST);
    }
}
