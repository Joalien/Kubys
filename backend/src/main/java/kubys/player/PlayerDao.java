package kubys.player;

import kubys.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerDao extends CrudRepository<Player, String> {
    List<Player> findPlayersByUser(User user);
    Player findPlayersById(long id);
}
