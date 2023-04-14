package app.service;

import app.config.JWTUtil;
import app.dao.common.MessengerRepository;
import app.dao.service.DefaultServiceDao;
import dto.*;
import lombok.RequiredArgsConstructor;
import dbmodel.Group;
import dbmodel.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultDataAccessService implements DataAccessService {

    private final MessengerRepository dao;
    private final JWTUtil jwtUtil;
    private final DefaultServiceDao defaultServiceDao;
    private final AuthenticationManager authenticationManager;

    @Override
    public String authenticate(AuthenticationRequest request) throws Exception {
        if(request == null) throw new IllegalArgumentException("The body was not provided");
        if(request.getLogin() == null || request.getLogin().isEmpty()) throw new IllegalArgumentException("The login is either null or empty string");
        if(request.getPassword() == null || request.getPassword().isEmpty()) throw new IllegalArgumentException("The password is either null or empty string");

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword()));
        UserDetails user = defaultServiceDao.loadUserByUsername(request.getLogin());

        if(user == null) throw new Exception("Cannot generate token. The unknown exception occurred");
        return jwtUtil.generateToken(user);
    }

    @Override
    public String prolong(ValidationRequest request) throws Exception {
        if(request == null) throw new IllegalArgumentException("The body was not provided");
        if(request.getToken() == null || request.getToken().isEmpty()) throw new IllegalArgumentException("The token is either null or empty string");

        String login = jwtUtil.extractUsername(request.getToken());
        UserDetails user = defaultServiceDao.loadUserByUsername(login);
        if(user == null) throw new Exception("Cannot generate token. The unknown exception occurred");
        if(!jwtUtil.isTokenValid(request.getToken(), user)) throw new IllegalStateException("Token is not valid");
        return jwtUtil.generateToken(user);
    }

    @Override
    public User[] getUsers() {
        List<User> all = dao.getUsers();
        User []users = new User[all.size()];

        int i=0;
        for(User a : all){
            users[i++] = a;
        }

        return users;
    }

    @Override
    public String[] getUsersLogins() {
        List<User> users = dao.getUsers();
        String []logins = new String[users.size()];
        int i=0;
        for(User user : users) logins[i++] = user.getLogin();
        return logins;
    }

    @Override
    public User getUser(UserDetailsRequest request) throws IllegalStateException, IllegalArgumentException {
        if (request == null) throw new IllegalArgumentException("The body was not provided");
        if (request.getLogin() == null || request.getLogin().isEmpty())
            throw new IllegalArgumentException("The login either was not provided or is an empty string");

        List<User> users = dao.getUsersByLogin(request.getLogin());

        if (users.size() > 1) throw new IllegalStateException("Found many users with the given login");
        if (users.isEmpty())
            throw new IllegalArgumentException("The user with login '" + request.getLogin() + "' was not found");
        return users.get(0);
    }

    @Override
    public String addUser(RegistrationRequest request) throws IllegalStateException, IllegalArgumentException {
        if (request == null) throw new IllegalArgumentException("The body was not provided");
        if (request.getLogin() == null || request.getLogin().isEmpty())
            throw new IllegalArgumentException("The login either was not provided or is an empty string");
        if (request.getPassword() == null || request.getPassword().isEmpty())
            throw new IllegalArgumentException("The password either was not provided or is an empty string");
        if (request.getEmail() == null || request.getEmail().isEmpty())
            throw new IllegalArgumentException("The email either was not provided or is an empty string");

        List<User> users = dao.getUsersByLoginOrEmail(request.getLogin(), request.getEmail());
        if (!users.isEmpty())
            throw new IllegalStateException("The user either with this login or email already exists");

        User user = new User(request.getLogin(), request.getPassword(), request.getEmail());
        dao.insertUser(user);
        return "Successfully added";
    }

    @Override
    public String deleteUser(UserDetailsRequest request) throws IllegalStateException, IllegalArgumentException {
        if (request == null) throw new IllegalArgumentException("The body was not provided");
        if (request.getLogin() == null || request.getLogin().isEmpty())
            throw new IllegalArgumentException("The login either was not provided or is an empty string");

        User user = getUser(request);
        dao.deleteUser(user);
        return "Successfully deleted";
    }

    @Override
    public Group[] getGroups() {
        List<Group> all = dao.getGroups();
        Group []groups = new Group[all.size()];

        int i=0;
        for(Group a : all){
            groups[i++] = a;
        }

        return groups;
    }

    @Override
    public String[] getGroupsNames() {
        List<Group> groups = dao.getGroups();
        String []names = new String[groups.size()];
        int i=0;
        for(Group group : groups) names[i++] = group.getName();
        return names;
    }

    @Override
    public void addGroup(Group group) {
        List<Group> groups = dao.getGroups();
        for(Group g : groups) {
            if(g.getName().equals(group.getName())) throw new IllegalArgumentException("The group with specified name already exists");
        }

        List<User> addedUsers = group.getUsers();
        for(Group g : groups){
            if(g.getUsers().size() != group.getUsers().size()) continue;

            boolean isAnyDifferent = false;
            for(User user : g.getUsers()){
                if(addedUsers.stream().filter(p -> p.getId().equals(user.getId())).findFirst().isEmpty()){
                    isAnyDifferent = true;
                    break;
                }
            }
            if(!isAnyDifferent) throw new IllegalArgumentException("Cannot add the group because the group containing those users already exists");
        }
        dao.insertGroup(group);
    }

    @Override
    public void updateGroup(Group group) {
        dao.updateGroup(group);
    }
}
