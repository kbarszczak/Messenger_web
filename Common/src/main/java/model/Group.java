package model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class Group {

    private String id;
    private String name;
    private List<Message> messages;
    private List<User> users;
    private LocalDateTime created;

    public Group(String name, List<Message> messages, List<User> users, LocalDateTime created) {
        this.name = name;
        this.messages = messages;
        this.users = users;
        this.created = created;
    }

    public Group(String name, List<Message> messages, List<User> users) {
        this(name, messages, users, LocalDateTime.now());
    }
}
