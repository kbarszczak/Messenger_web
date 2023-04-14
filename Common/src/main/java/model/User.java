package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

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



