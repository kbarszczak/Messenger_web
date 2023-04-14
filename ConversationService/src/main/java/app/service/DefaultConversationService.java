package app.service;

import app.dao.ConversationDao;
import dto.CreateGroupRequest;
import dto.PoorMessage;
import lombok.RequiredArgsConstructor;
import model.Group;
import model.Message;
import model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class DefaultConversationService implements ConversationService{

    private final ConversationDao conversationDao;

    @Override
    public String[] getUsersLogins() {
        return conversationDao.getUsersLogins();
    }

    @Override
    public String[] getGroupsNames() {
        return conversationDao.getGroupsNames();
    }

    @Override
    public String getGroupId(String groupName, String userLogin) {
        Group []groups = conversationDao.getGroups();
        Optional<Group> group = Arrays.stream(groups).filter(p -> p.getName().equals(groupName)).findFirst();
        if(group.isEmpty()) throw new IllegalArgumentException("Group with the name '" + groupName + "' does not exists");
        if(group.get().getUsers().stream().filter(p -> p.getLogin().equals(userLogin)).findFirst().isEmpty()) throw new IllegalArgumentException("User with login '" + userLogin + "' does not belong to the group '" + groupName + "'");

        return group.get().getId();
    }

    @Override
    public PoorMessage[] getGroupMessages(String groupId, String userLogin) {
        Group []groups = conversationDao.getGroups();
        Optional<Group> group = Arrays.stream(groups).filter(p -> p.getId().equals(groupId)).findFirst();
        if(group.isEmpty()) throw new IllegalArgumentException("Group with the id '" + groupId + "' does not exists");

        Optional<User> user = group.get().getUsers().stream().filter(p -> p.getLogin().equals(userLogin)).findFirst();
        if(user.isEmpty()) throw new IllegalArgumentException("User '" + userLogin + "' does not belong to the group '" + group.get().getName() + "'");

        List<Message> messages = group.get().getMessages();
        PoorMessage[] poorMessages = new PoorMessage[messages.size()];

        int i=0;
        for(Message message : messages){
            poorMessages[i++] = new PoorMessage(
                    message.getAuthor().getLogin(),
                    message.getText(),
                    message.getSent()
            );
        }

        return poorMessages;
    }

    @Override
    public void createGroup(CreateGroupRequest request, String userLogin) {
        if(Arrays.stream(request.getLogins()).filter(p -> p.equals(userLogin)).findFirst().isEmpty()) throw new IllegalArgumentException("Cannot add the group because adding person is not specified in the logins");

        User[] allUsers = conversationDao.getUsers();
        List<User> usersInGroup = new ArrayList<>();
        for(String login : request.getLogins()){
            boolean isPresent = false;
            for(User user : allUsers){
                if(user.getLogin().equals(login)){
                    isPresent = true;
                    usersInGroup.add(user);
                    break;
                }
            }
            if(!isPresent) throw new IllegalArgumentException("User with login '" + login + "' is not in the database");
        }
        conversationDao.addGroup(new Group(
                request.getName(),
                new ArrayList<>(),
                usersInGroup
        ));
    }

    @Override
    public void addMessage(String message, String groupId, String userLogin) {
        Group []groups = conversationDao.getGroups();
        Optional<Group> group = Arrays.stream(groups).filter(p -> p.getId().equals(groupId)).findFirst();
        if(group.isEmpty()) throw new IllegalArgumentException("Group with the id '" + groupId + "' does not exists");

        Optional<User> user = group.get().getUsers().stream().filter(p -> p.getLogin().equals(userLogin)).findFirst();
        if(user.isEmpty()) throw new IllegalArgumentException("User with login '" + userLogin + "' does not belong to the group '" + group.get().getName() + "'");

        Message msg = new Message(user.get(), message);
        group.get().getMessages().add(msg);
        conversationDao.updateGroup(group.get());
    }
}
