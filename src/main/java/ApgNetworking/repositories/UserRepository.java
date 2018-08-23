package ApgNetworking.repositories;

import ApgNetworking.models.User;
import ApgNetworking.models.UserCourse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;

public interface UserRepository extends CrudRepository<User,Long> {
    User findByUsername(String username);
    Long countByUsername(String username);
    @Query("SELECT u FROM User as u JOIN u.roles as r WHERE r.id = ?1")
    public List<User> findByRole(long roleID);
}
