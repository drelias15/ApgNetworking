package ApgNetworking.repositories;

import ApgNetworking.models.PrivateMessage;
import org.springframework.data.repository.CrudRepository;

public interface PrivateMessageRepository extends CrudRepository<PrivateMessage,
        Long> {
}