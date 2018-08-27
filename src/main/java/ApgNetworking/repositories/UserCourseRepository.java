package ApgNetworking.repositories;

import ApgNetworking.models.Course;
import ApgNetworking.models.Role;
import ApgNetworking.models.User;
import ApgNetworking.models.UserCourse;
import org.springframework.data.repository.CrudRepository;

import javax.jws.soap.SOAPBinding;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
@Transactional
public interface UserCourseRepository extends CrudRepository<UserCourse, Long> {
    ArrayList<UserCourse> findAllByRoleAndCourse(Role role, Course course);
    Collection<UserCourse> findAllByUserAndRole(User user, Role role);
    Collection<UserCourse> findAllByUser(User user);
    UserCourse findByCourseAndRoleAndUser(Course course, Role role, User user);
}
