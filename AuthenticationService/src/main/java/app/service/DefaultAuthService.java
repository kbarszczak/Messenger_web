package app.service;

import app.config.JWTUtil;
import app.dao.AuthDao;
import dto.*;
import lombok.RequiredArgsConstructor;
import model.ExtendedUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultAuthService implements AuthService{

    private final AuthenticationManager authenticationManager;
    private final AuthDao authDAO;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder encoder;

    @Override
    public Token authenticate(AuthenticationRequest request) throws Exception {
        if(request == null) throw new IllegalArgumentException("The body was not provided");
        if(request.getLogin() == null || request.getLogin().isEmpty()) throw new IllegalArgumentException("The login is either null or empty string");
        if(request.getPassword() == null || request.getPassword().isEmpty()) throw new IllegalArgumentException("The password is either null or empty string");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
        UserDetails user = authDAO.findUserByLogin(request.getLogin());

        if(user == null) throw new Exception("Cannot generate token. The unknown exception occurred");
        return new Token(jwtUtil.generateToken(user), "Token successfully generated");
    }

    @Override
    public Validation validate(ValidationRequest request) throws Exception {
        if(request == null) throw new IllegalArgumentException("The body was not provided");
        if(request.getToken() == null || request.getToken().isEmpty()) throw new IllegalArgumentException("The token is either null or empty string");

        final ExtendedUser user = authDAO.findUserByLogin(jwtUtil.extractUsername(request.getToken()));
        if (!jwtUtil.isTokenValid(request.getToken(), user)) throw new IllegalStateException("The user is unauthorized");
        return new Validation(user.getId(), user.getLogin(), user.getAuthority(), true, "Authorized successfully");
    }

    @Override
    public Validation register(RegistrationRequest request) throws Exception {
        if(request == null) throw new IllegalArgumentException("The body was not provided");
        if(request.getLogin() == null || request.getLogin().isEmpty()) throw new IllegalArgumentException("The login is either null or empty string");
        if(request.getPassword() == null || request.getPassword().isEmpty()) throw new IllegalArgumentException("The password is either null or empty string");
        if(request.getEmail() == null || request.getEmail().isEmpty()) throw new IllegalArgumentException("The email is either null or empty string");

        String encryptedPassword = encoder.encode(request.getPassword());
        authDAO.addNewUser(request.getLogin(), encryptedPassword, request.getEmail());
        final ExtendedUser user = authDAO.findUserByLogin(request.getLogin());
        return new Validation(user.getId(), user.getLogin(), user.getAuthority(), false, "User registered successfully");
    }
}
