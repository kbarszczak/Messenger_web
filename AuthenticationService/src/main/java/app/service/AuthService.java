package app.service;

import dto.*;

public interface AuthService {

    Token authenticate(AuthenticationRequest request) throws Exception;
    Validation validate(ValidationRequest request) throws Exception;
    Validation register(RegistrationRequest request) throws Exception;

}
