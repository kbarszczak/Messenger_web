package app.service;

import dto.AuthenticationRequest;
import dto.RegistrationRequest;
import dto.UserDetailsRequest;
import dto.ValidationRequest;
import dbmodel.Group;
import dbmodel.User;

public interface DataAccessService {

    String authenticate(AuthenticationRequest request) throws Exception;
    String prolong(ValidationRequest request) throws Exception;

    User[] getUsers();
    String[] getUsersLogins();
    User getUser(UserDetailsRequest request) throws IllegalStateException, IllegalArgumentException;
    String addUser(RegistrationRequest request) throws IllegalStateException, IllegalArgumentException;
    String deleteUser(UserDetailsRequest request) throws IllegalStateException, IllegalArgumentException;

    Group[] getGroups();
    String[] getGroupsNames();
    void addGroup(Group group);
    void updateGroup(Group group);
}
