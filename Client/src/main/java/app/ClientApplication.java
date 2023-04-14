package app;

import dto.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientApplication {

    public static RestTemplate TEMPLATE = new RestTemplate();
    public static Scanner SCANNER = new Scanner(System.in);
    public static String AUTH_URL = "http://localhost:8090/api/v1/auth/authenticate";
    public static String REGISTER_URL = "http://localhost:8090/api/v1/auth/register";
    public static String ALL_USERS_URL = "http://localhost:8092/api/v1/conv/users/all";
    public static String ALL_GROUPS_URL = "http://localhost:8092/api/v1/conv/group/all";
    public static String CREATE_GROUP_URL = "http://localhost:8092/api/v1/conv/group/create";
    public static String GET_GROUP_ID_URL = "http://localhost:8092/api/v1/conv/group/id?name=";
    public static String GET_GROUP_MESSAGES_URL = "http://localhost:8092/api/v1/conv/group/messages?id=";
    public static String MESSAGE_URL = "http://localhost:8092/api/v1/conv/message";

    public static void main(String[] args) {
        try {
            Logger.getGlobal().setLevel(Level.OFF);
            String token = null, login = null, activeGroupId = null;
            boolean run = true;
            while (run) {
                try {
                    int action = action("Exit", "Login", "Logout", "Register", "Print all users", "Print all groups", "Create group", "Set active group", "Write message", "Print active group messages");
                    switch (action) {
                        case 0 -> { // exit
                            run = false;
                        }
                        case 1 -> { // login
                            if(token != null) throw new IllegalStateException("User is already logged in");

                            // read data
                            String password;
                            System.out.print("Login: ");
                            login = SCANNER.nextLine();
                            System.out.print("Password: ");
                            password = SCANNER.nextLine();

                            // send request & handle the response
                            try {
                                HttpEntity<AuthenticationRequest> entity = new HttpEntity<>(new AuthenticationRequest(login, password));
                                Token t = TEMPLATE.postForObject(AUTH_URL, entity, Token.class);
                                if (t == null) throw new IllegalStateException("Could not read the response body");
                                token = t.getToken();
                            } catch (HttpClientErrorException exception) {
                                System.err.println("The message from the server: " + exception.getMessage());
                                if (exception.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                                    System.err.println("Bad credentials");
                                }
                            }
                        }
                        case 2 -> { // logout
                            if(token == null) throw new IllegalStateException("Cannot logout");
                            token = null;
                            activeGroupId = null;
                        }
                        case 3 -> { // register
                            // read data
                            String l, password, email;
                            System.out.print("Login: ");
                            l = SCANNER.nextLine();
                            System.out.print("Password: ");
                            password = SCANNER.nextLine();
                            System.out.print("Email: ");
                            email = SCANNER.nextLine();

                            // send request & handle the response
                            try {
                                HttpEntity<RegistrationRequest> entity = new HttpEntity<>(new RegistrationRequest(l, password, email));
                                Validation validation = TEMPLATE.postForObject(REGISTER_URL, entity, Validation.class);
                                if (validation == null)
                                    throw new IllegalStateException("Could not read the response body");
                                System.out.printf("Registered user '%s' with id '%s'\n", validation.getLogin(), validation.getId());
                            } catch (HttpClientErrorException exception) {
                                System.err.println("The message from the server: " + exception.getMessage());
                                if (exception.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                                    System.err.println("User with this login or email already exists");
                                }
                            }
                        }
                        case 4 -> { // print all users
                            // send request & handle the response
                            try {
                                HttpEntity<Void> entity = new HttpEntity<>(prepareHeader(token));
                                String[] logins = TEMPLATE.exchange(ALL_USERS_URL, HttpMethod.GET, entity, String[].class).getBody();
                                if (logins == null)
                                    throw new IllegalStateException("Could not read the response body");

                                System.out.println("All users:");
                                for(String l : logins){
                                    System.out.printf("User: '%s'\n", l);
                                }
                            } catch (HttpClientErrorException exception) {
                                System.err.println("The message from the server: " + exception.getMessage());
                                if (exception.getStatusCode() == HttpStatus.UNAUTHORIZED || exception.getStatusCode() == HttpStatus.FORBIDDEN) {
                                    System.err.println("Token is not valid. Please log in once again");
                                }
                            }
                        }
                        case 5 -> { // print all groups
                            // send request & handle the response
                            try {
                                HttpEntity<Void> entity = new HttpEntity<>(prepareHeader(token));
                                String[] names = TEMPLATE.exchange(ALL_GROUPS_URL, HttpMethod.GET, entity, String[].class).getBody();
                                if (names == null)
                                    throw new IllegalStateException("Could not read the response body");

                                System.out.println("All groups:");
                                for(String n : names){
                                    System.out.printf("Group name: '%s'\n", n);
                                }
                            } catch (HttpClientErrorException exception) {
                                System.err.println("The message from the server: " + exception.getMessage());
                                if (exception.getStatusCode() == HttpStatus.UNAUTHORIZED || exception.getStatusCode() == HttpStatus.FORBIDDEN) {
                                    System.err.println("Token is not valid. Please log in once again");
                                }
                            }
                        }
                        case 6 -> { // create group
                            // read data
                            int size;
                            String name;
                            System.out.print("Group name:");
                            name = SCANNER.nextLine();
                            System.out.print("Group size:");
                            size = Integer.parseInt(SCANNER.nextLine());

                            if(size < 2) throw new IllegalStateException("Cannot create group of size less than 2");


                            String []logins = new String[size];
                            logins[0] = login;
                            for(int i=1; i<size; ++i){
                                System.out.printf("User %d login: ", (i+1));
                                logins[i] = SCANNER.nextLine();
                            }

                            for(String l : logins) System.out.println(l);

                            // send request & handle the response
                            try {
                                HttpEntity<CreateGroupRequest> entity = new HttpEntity<>(new CreateGroupRequest(name, logins), prepareHeader(token));
                                String message = TEMPLATE.postForObject(CREATE_GROUP_URL, entity, String.class);
                                if (message == null)
                                    throw new IllegalStateException("Could not read the response body");
                                System.out.println(message);
                            } catch (HttpClientErrorException exception) {
                                System.err.println("The message from the server: " + exception.getMessage());
                                if (exception.getStatusCode() == HttpStatus.UNAUTHORIZED || exception.getStatusCode() == HttpStatus.FORBIDDEN) {
                                    System.err.println("Token is not valid. Please log in once again");
                                } else if (exception.getStatusCode() == HttpStatus.BAD_REQUEST) {
                                    System.err.println("Could not create the group. Consider the following issues:\n1. Some of the users added to the group does not exist.\n2. The group containing those users already exists.\n3. You are not logged in into your account");
                                }
                            }
                        }
                        case 7 -> { // set active group
                            String name;
                            System.out.print("Group name: ");
                            name = SCANNER.nextLine();

                            // send request & handle the response
                            try {
                                HttpEntity<Void> entity = new HttpEntity<>(prepareHeader(token));
                                activeGroupId = TEMPLATE.exchange(GET_GROUP_ID_URL+name, HttpMethod.GET, entity, String.class).getBody();
                                if (activeGroupId == null)
                                    throw new IllegalStateException("Could not read the response body");
                                System.out.printf("Active group is now '%s'\n", name);
                            } catch (HttpClientErrorException exception) {
                                System.err.println("The message from the server: " + exception.getMessage());
                                if (exception.getStatusCode() == HttpStatus.UNAUTHORIZED || exception.getStatusCode() == HttpStatus.FORBIDDEN) {
                                    System.err.println("Token is not valid. Please log in once again");
                                } else if(exception.getStatusCode() == HttpStatus.BAD_REQUEST){
                                    System.err.println("The group either not exists or you do not belong to this group");
                                }
                            }
                        }
                        case 8 -> { // write message
                            if(activeGroupId == null) throw new IllegalStateException("The active group is not set");

                            String message;
                            System.out.print("Message: ");
                            message = SCANNER.nextLine();

                            // send request & handle the response
                            try {
                                HttpEntity<NewMessageRequest> entity = new HttpEntity<>(new NewMessageRequest(message, activeGroupId), prepareHeader(token));
                                String response = TEMPLATE.postForObject(MESSAGE_URL, entity, String.class);
                                if (response == null)
                                    throw new IllegalStateException("Could not read the response body");

                                System.out.println(response);
                            } catch (HttpClientErrorException exception) {
                                System.err.println("The message from the server: " + exception.getMessage());
                                if (exception.getStatusCode() == HttpStatus.UNAUTHORIZED || exception.getStatusCode() == HttpStatus.FORBIDDEN) {
                                    System.err.println("Token is not valid. Please log in once again");
                                }
                            }
                        }
                        case 9 -> { // print active group messages
                            if(activeGroupId == null) throw new IllegalStateException("The active group is not set");

                            // send request & handle the response
                            try {
                                HttpEntity<Void> entity = new HttpEntity<>(prepareHeader(token));
                                PoorMessage []messages = TEMPLATE.exchange(GET_GROUP_MESSAGES_URL+activeGroupId, HttpMethod.GET, entity, PoorMessage[].class).getBody();
                                if (messages == null)
                                    throw new IllegalStateException("Could not read the response body");

                                System.out.printf("Messages (%d):\n", messages.length);
                                for(PoorMessage message : messages){
                                    System.out.printf("%s: (%s) '%s'\n", message.getAuthorLogin(), message.getSent(), message.getMessage());
                                }
                            } catch (HttpClientErrorException exception) {
                                System.err.println("The message from the server: " + exception.getMessage());
                                if (exception.getStatusCode() == HttpStatus.UNAUTHORIZED || exception.getStatusCode() == HttpStatus.FORBIDDEN) {
                                    System.err.println("Token is not valid. Please log in once again");
                                } else if(exception.getStatusCode() == HttpStatus.BAD_REQUEST){
                                    System.err.println("User does not belong to this group");
                                }
                            }
                        }
                        default -> throw new IllegalArgumentException("Unknown option");
                    }
                } catch (HttpClientErrorException exception) {
                    System.err.printf("Unexpected http '%s' error occurred.\nDetails: '%s'\n", exception.getStatusCode(), exception.getMessage());
                } catch (HttpServerErrorException exception) {
                    System.err.printf("Server exception occurred. Status code: '%s'. Please try again later.\nDetails: '%s'\n", exception.getStatusCode(), exception.getMessage());
                } catch (IllegalStateException | IllegalArgumentException exception) {
                    System.err.printf("Illegal state occurred. Details: '%s'\n", exception.getMessage());
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.err.printf("Fatal exception occurred. Details: '%s'\n", exception.getMessage());
        }
    }

    public static int action(String... options) throws IllegalArgumentException {
        int i = 0;
        for (String option : options) {
            System.out.printf("%d: %s\n", i++, option);
        }

        boolean ok;
        do {
            try {
                i = Integer.parseInt(SCANNER.nextLine());
                if (i < 0 || i >= options.length) throw new IllegalArgumentException("Index out of range");
                ok = true;
            } catch (IllegalArgumentException exception) {
                System.err.println(exception.getMessage());
                ok = false;
            }
        } while (!ok);

        return i;
    }

    public static HttpHeaders prepareHeader(String token){
        if(token == null) throw new IllegalStateException("Please log in");
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return headers;
    }

}
