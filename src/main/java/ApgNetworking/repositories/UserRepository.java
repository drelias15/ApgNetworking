package ApgNetworking.repositories;

import ApgNetworking.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByUsername(String username);
    Long countByUsername(String username);
}
