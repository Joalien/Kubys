package kubys.Spell;

import kubys.Player.Player;
import kubys.Player.SpellPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpellPlayerDao extends JpaRepository<SpellPlayer, String> {

    List<SpellPlayer> findAllByPlayer(Player player);
}
