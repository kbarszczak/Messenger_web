package dbmodel;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Message {

    @DBRef
    private dbmodel.User author;
    private String text;
    private LocalDateTime sent;

    public Message(dbmodel.User author, String text, LocalDateTime sent) {
        this.author = author;
        this.text = text;
        this.sent = sent;
    }

    public Message(User author, String text) {
        this(author, text, LocalDateTime.now());
    }
}
