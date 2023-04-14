package app.dao.common;

import dbmodel.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MessageRepository extends MongoRepository<Message, String> {

    List<Message> getMessagesByAuthorLogin(String login);

}
