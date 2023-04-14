package app.dao;

import model.Group;
import model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import utils.token.TokenExchangeable;
import utils.token.TokenExchangerThread;

@Repository
public class ConversationServiceBasedDao implements ConversationDao, TokenExchangeable, AutoCloseable {

    private final RestTemplate rest;
    private final TokenExchangerThread thread;
    private String dataAccessToken;

    @Value("${data-access-service.endpoint.all-users-logins}")
    private String allUsersLoginsURL;

    @Value("${data-access-service.endpoint.all-users}")
    private String allUsersURL;

    @Value("${data-access-service.endpoint.all-groups-names}")
    private String allGroupsNamesURL;

    @Value("${data-access-service.endpoint.all-groups}")
    private String allGroupsURL;

    @Value("${data-access-service.endpoint.add-group}")
    private String addGroupURL;

    @Value("${data-access-service.endpoint.update-group}")
    private String updateGroupURL;

    public ConversationServiceBasedDao(@Value("${data-access-service.endpoint.login}") String authURL,
                               @Value("${data-access-service.endpoint.prolong}") String prolongURL,
                               @Value("${data-access-service.credentials.login}") String dataAccessServiceLogin,
                               @Value("${data-access-service.credentials.password}") String dataAccessServicePassword
    ){
        rest = new RestTemplate();
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
    public User[] getUsers() {
        try{
            HttpEntity<Void> entity = new HttpEntity<>(prepareHeader(dataAccessToken));
            return rest.exchange(allUsersURL, HttpMethod.GET, entity, User[].class).getBody();
        } catch (HttpClientErrorException exception){
            throw new IllegalStateException("Could not get the data from data access service. Got: '" + exception.getMessage() + "'");
        }
    }

    @Override
    public String[] getUsersLogins() {
        try{
            HttpEntity<Void> entity = new HttpEntity<>(prepareHeader(dataAccessToken));
            return rest.exchange(allUsersLoginsURL, HttpMethod.GET, entity, String[].class).getBody();
        } catch (HttpClientErrorException exception){
            throw new IllegalStateException("Could not get the data from data access service. Got: '" + exception.getMessage() + "'");
        }
    }

    @Override
    public Group[] getGroups() {
        try{
            HttpEntity<Void> entity = new HttpEntity<>(prepareHeader(dataAccessToken));
            return rest.exchange(allGroupsURL, HttpMethod.GET, entity, Group[].class).getBody();
        } catch (HttpClientErrorException exception){
            throw new IllegalStateException("Could not get the data from data access service. Got: '" + exception.getMessage() + "'");
        }
    }

    @Override
    public String[] getGroupsNames() {
        try{
            HttpEntity<Void> entity = new HttpEntity<>(prepareHeader(dataAccessToken));
            return rest.exchange(allGroupsNamesURL, HttpMethod.GET, entity, String[].class).getBody();
        } catch (HttpClientErrorException exception){
            throw new IllegalStateException("Could not get the data from data access service. Got: '" + exception.getMessage() + "'");
        }
    }

    @Override
    public void addGroup(Group group) {
        try{
            HttpEntity<Group> entity = new HttpEntity<>(group, prepareHeader(dataAccessToken));
            rest.postForObject(addGroupURL, entity, String.class);
        } catch (HttpClientErrorException exception){
            throw new IllegalStateException("Could not add the data to data access service. Got: '" + exception.getMessage() + "'");
        }
    }

    @Override
    public void updateGroup(Group group) {
        try{
            HttpEntity<Group> entity = new HttpEntity<>(group, prepareHeader(dataAccessToken));
            rest.postForObject(updateGroupURL, entity, String.class);
        } catch (HttpClientErrorException exception){
            throw new IllegalStateException("Could not add the data to data access service. Got: '" + exception.getMessage() + "'");
        }
    }

    @Override
    public void close() throws Exception {
        thread.disable();
        thread.join();
    }

    private static HttpHeaders prepareHeader(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }
}
