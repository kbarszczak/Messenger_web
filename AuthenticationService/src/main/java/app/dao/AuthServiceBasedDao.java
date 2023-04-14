package app.dao;

import dto.RegistrationRequest;
import dto.UserDetailsRequest;
import model.ExtendedUser;
import model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import utils.token.TokenExchangeable;
import utils.token.TokenExchangerThread;

import java.util.Objects;

@Repository
public class AuthServiceBasedDao implements AuthDao, TokenExchangeable, AutoCloseable {

    private final TokenExchangerThread thread;
    private String dataAccessToken;

    @Value("${data-access-service.get-user.endpoint}")
    private String getUserURL;

    @Value("${data-access-service.add-user.endpoint}")
    private String addUserURL;

    public AuthServiceBasedDao(@Value("${data-access-service.auth.login.endpoint}") String authURL,
                               @Value("${data-access-service.auth.prolong.endpoint}") String prolongURL,
                               @Value("${data-access-service.credentials.login}") String dataAccessServiceLogin,
                               @Value("${data-access-service.credentials.password}") String dataAccessServicePassword
    ){
        thread = new TokenExchangerThread(
                this,
                dataAccessServiceLogin,
                dataAccessServicePassword,
                authURL,
                prolongURL
        );
        thread.start();
    }

    @Override
    public void exchangeToken(String token) {
        this.dataAccessToken = token;
    }

    @Override
    public ExtendedUser findUserByLogin(String login) throws UsernameNotFoundException {
        try{
            RestTemplate rest = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(dataAccessToken);
            HttpEntity<UserDetailsRequest> entity = new HttpEntity<>(new UserDetailsRequest(login), headers);
            User user = rest.postForObject(getUserURL, entity, User.class);
            return new ExtendedUser(Objects.requireNonNull(user));
        } catch (HttpClientErrorException exception){
            throw new UsernameNotFoundException("No user was found");
        }
    }

    @Override
    public void addNewUser(String login, String password, String email) throws IllegalStateException {
        try{
            RestTemplate rest = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(dataAccessToken);
            HttpEntity<RegistrationRequest> entity = new HttpEntity<>(new RegistrationRequest(login, password, email), headers);
            rest.postForObject(addUserURL, entity, String.class);
        }catch (HttpClientErrorException exception){
            if(exception.getStatusCode() == HttpStatus.BAD_REQUEST) throw new IllegalStateException(exception.getResponseBodyAsString());
            if(exception.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) throw new IllegalStateException("Data access service is not accessible");
            throw new IllegalStateException("Data access unknown exception");
        }
    }

    @Override
    public void close() throws Exception {
        thread.disable();
        thread.join();
    }
}
