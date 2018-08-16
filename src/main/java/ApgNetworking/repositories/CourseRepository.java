package ApgNetworking.repositories;

import ApgNetworking.models.User;
import ApgNetworking.models.Course;
import ApgNetworking.models.UserCourse;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface CourseRepository extends CrudRepository<Course,Long> {
    Collection<Course> findCoursesByActiveIsTrue();
    Collection<Course> findCoursesByUsersNotContains(User user);
    Collection<Course> findAllByUserCourses(UserCourse userCourse);
    Course findByCrn(String crn);
}
