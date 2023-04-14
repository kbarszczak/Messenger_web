package dbmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document
public class User {

    @Id
    private String id;
    private String login;
    private String password;
    private String email;
    private LocalDateTime created;
    private String authority;

    public User(String login, String password, String email, LocalDateTime created, String authority) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.created = created;
        this.authority = authority;
    }

    public User(String login, String password, String email) {
        this(login, password, email, LocalDateTime.now(), "ROLE_USER");
    }
}



