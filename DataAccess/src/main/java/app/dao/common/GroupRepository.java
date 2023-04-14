package app.dao.common;

import dbmodel.Group;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface GroupRepository extends MongoRepository<Group, String> {

    Optional<Group> getGroupByName(String name);

}
