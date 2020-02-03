package kubys.Player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpellPlayerDao extends JpaRepository<SpellPlayer, String> {

    List<SpellPlayer> findAllByPlayer(Player player);
}
