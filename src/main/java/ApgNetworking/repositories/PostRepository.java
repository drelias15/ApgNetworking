package ApgNetworking.repositories;

import ApgNetworking.models.Course;
import ApgNetworking.models.Post;
import ApgNetworking.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Collection;

public interface PostRepository extends CrudRepository<Post,Long> {
    ArrayList<Post> findAllByCourse(Course course);
}
