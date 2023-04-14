package app.dao.common;

import dbmodel.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> getUserByLogin(String login);
    Optional<User> getUserByEmail(String email);
    List<User> getUsersByLogin(String login);
    List<User> getUsersByLoginOrEmail(String login, String email);
}
