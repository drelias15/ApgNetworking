package ApgNetworking.repositories;

import ApgNetworking.models.Post;
import ApgNetworking.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface PostRepository extends CrudRepository<Post,Long> {
Collection<Post> findApgPostsByApguser(User user);

}
