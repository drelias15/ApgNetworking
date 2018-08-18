package ApgNetworking.repositories;

import ApgNetworking.models.Course;
import org.springframework.data.repository.CrudRepository;
import java.util.ArrayList;

public interface CourseRepository extends CrudRepository<Course,Long> {
    Course findByCrn(String crn);
    ArrayList<Course> findAllBySemester (String semester);
}
