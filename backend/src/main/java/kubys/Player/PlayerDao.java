package kubys.Player;

import kubys.User.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerDao extends CrudRepository<Player, String> {
    List<Player> findPlayersByUser(User user);
}
