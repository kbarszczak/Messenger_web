package utils.token;

import dto.AuthenticationRequest;
import dto.ValidationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public class TokenExchangerThread extends Thread{

    public static final int RETRY_GET_TOKEN_AFTER_MS = 500;
    public static final int EXCHANGE_TOKEN_AFTER_MS = 8000;

    private final TokenExchangeable exchangeable;
    private final String login;
    private final String password;
    private final String authURL;
    private final String prolongURL;

    private boolean run = false;

    public void disable(){
        run = false;
    }

    @Override
    public synchronized void start() {
        super.start();
        run = true;
    }

    @Override
    public void run() {
        try{
            // get the valid token
            RestTemplate template = new RestTemplate();
            while(run){
                String token = null;
                while(token == null){
                    try{
                        HttpEntity<AuthenticationRequest> entity = new HttpEntity<>(new AuthenticationRequest(login, password));
                        token = template.postForObject(authURL, entity, String.class);
                    }catch (RestClientException ignore){}
                    Thread.sleep(RETRY_GET_TOKEN_AFTER_MS);
                }
                exchangeable.exchangeToken(token);

                // prolong the token
                while(true){
                    try{
                        HttpEntity<ValidationRequest> entity = new HttpEntity<>(new ValidationRequest(token));
                        token = template.postForObject(prolongURL, entity, String.class);
                        exchangeable.exchangeToken(token);
                    }catch (RestClientException exception){
                        System.out.println("Could not prolong. Details: " + exception.getMessage());
                        break;
                    }
                    Thread.sleep(EXCHANGE_TOKEN_AFTER_MS);
                }
            }
        }catch (InterruptedException ignore){}
    }
}
