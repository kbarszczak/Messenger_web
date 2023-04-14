package app.dao;

import model.ExtendedUser;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthDao {

    ExtendedUser findUserByLogin(String login) throws UsernameNotFoundException;
    void addNewUser(String login, String password, String email) throws IllegalStateException;
}
