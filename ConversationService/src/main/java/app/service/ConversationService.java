package app.service;

import dto.CreateGroupRequest;
import dto.PoorMessage;

public interface ConversationService {

    String[] getUsersLogins();

    String[] getGroupsNames();
    String getGroupId(String groupName, String userLogin);
    PoorMessage[] getGroupMessages(String groupId, String userLogin);
    void createGroup(CreateGroupRequest request, String userLogin);

    void addMessage(String message, String groupId, String userLogin);
}
