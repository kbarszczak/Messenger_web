package app.dao;

import model.Group;
import model.User;

public interface ConversationDao {

    User[] getUsers();
    String[] getUsersLogins();

    Group[] getGroups();
    String[] getGroupsNames();
    void addGroup(Group group);
    void updateGroup(Group group);
}
