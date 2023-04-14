package app.dao.common;

import lombok.RequiredArgsConstructor;
import dbmodel.Group;
import dbmodel.Message;
import dbmodel.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DefaultMessengerRepository implements MessengerRepository {

    private final MessageRepository messageDao;
    private final UserRepository userDao;
    private final GroupRepository groupDao;

    @Override
    public Optional<User> getUserByLogin(String login) {
        return userDao.getUserByLogin(login);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userDao.getUserByEmail(email);
    }

    @Override
    public List<User> getUsers() {
        return userDao.findAll();
    }

    @Override
    public List<User> getUsersByLogin(String login) {
        return userDao.getUsersByLogin(login);
    }

    @Override
    public List<User> getUsersByLoginOrEmail(String login, String email) {
        return userDao.getUsersByLoginOrEmail(login, email);
    }

    @Override
    public void insertUser(User user) {
        userDao.insert(user);
    }

    @Override
    public void deleteUser(User user) {
        userDao.delete(user);
    }

    @Override
    public List<Message> getMessagesByAuthorLogin(String login) {
        return messageDao.getMessagesByAuthorLogin(login);
    }

    @Override
    public List<Group> getGroups() {
        return groupDao.findAll();
    }

    @Override
    public Optional<Group> getGroupByName(String name) {
        return groupDao.getGroupByName(name);
    }

    @Override
    public void insertGroup(Group group) {
        groupDao.insert(group);
    }

    @Override
    public void updateGroup(Group group) {
        groupDao.save(group);
    }

}
