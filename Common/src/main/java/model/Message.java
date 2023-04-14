package model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class Message {

    private User author;
    private String text;
    private LocalDateTime sent;

    public Message(User author, String text, LocalDateTime sent) {
        this.author = author;
        this.text = text;
        this.sent = sent;
    }

    public Message(User author, String text) {
        this(author, text, LocalDateTime.now());
    }
}
