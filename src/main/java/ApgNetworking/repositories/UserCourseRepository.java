package ApgNetworking.repositories;

import ApgNetworking.models.Course;
import ApgNetworking.models.Role;
import ApgNetworking.models.User;
import ApgNetworking.models.UserCourse;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Collection;

public interface UserCourseRepository extends CrudRepository<UserCourse, Long> {
    ArrayList<UserCourse> findAllByRoleAndCourse(Role role, Course course);
    Collection<UserCourse> findAllByUserAndRole(User user, Role role);
}
