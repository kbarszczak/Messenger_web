package app.controller;

import dto.*;
import app.service.AuthService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/authenticate")
    public ResponseEntity<Token> authenticate(@RequestBody AuthenticationRequest request) {
        try {
            return ResponseEntity.ok(authService.authenticate(request));
        } catch (IllegalArgumentException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Token(null, exception.getMessage()));
        } catch (AuthenticationException | BadCredentialsException | UsernameNotFoundException exception){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Token(null, exception.getMessage()));
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Token(null, exception.getMessage()));
        }
    }

    @PostMapping("/valid")
    public ResponseEntity<Validation> valid(@RequestBody ValidationRequest request) {
        try {
            return ResponseEntity.ok(authService.validate(request));
        } catch (IllegalArgumentException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Validation(null, null, null, false, exception.getMessage()));
        } catch (IllegalStateException | UsernameNotFoundException | JwtException exception){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Validation(null, null, null, false, exception.getMessage()));
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Validation(null, null, null, false, exception.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Validation> register(@RequestBody RegistrationRequest request) {
        try {
            return ResponseEntity.ok(authService.register(request));
        } catch (IllegalArgumentException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Validation(null, null, null, false, exception.getMessage()));
        } catch (IllegalStateException exception){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new Validation(null, null, null, false, exception.getMessage()));
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Validation(null, null, null, false, exception.getMessage()));
        }
    }

}
