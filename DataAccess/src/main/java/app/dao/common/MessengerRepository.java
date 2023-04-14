package app.dao.common;

import dbmodel.Group;
import dbmodel.Message;
import dbmodel.User;

import java.util.List;
import java.util.Optional;

public interface MessengerRepository {

    // users
    Optional<User> getUserByLogin(String login);
    Optional<User> getUserByEmail(String email);
    List<User> getUsers();
    List<User> getUsersByLogin(String login);
    List<User> getUsersByLoginOrEmail(String login, String email);
    void deleteUser(User user);
    void insertUser(User user);

    // messages
    List<Message> getMessagesByAuthorLogin(String login);

    // groups
    List<Group> getGroups();
    Optional<Group> getGroupByName(String name);
    void insertGroup(Group group);
    void updateGroup(Group group);
}
