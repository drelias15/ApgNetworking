package ApgNetworking.repositories;

import ApgNetworking.models.PrivateMessage;
import ApgNetworking.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface PrivateMessageRepository extends CrudRepository<PrivateMessage,
        Long> {
    ArrayList<PrivateMessage> findAllBySender(User sender);
    ArrayList<PrivateMessage> findAllByReceiver(User receiver);
}