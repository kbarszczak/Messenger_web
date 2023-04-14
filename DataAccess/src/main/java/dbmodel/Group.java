package dbmodel;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Document
public class Group {

    @Id
    private String id;
    private String name;
    private List<Message> messages;
    @DBRef
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
