package kubys.Spell;

import kubys.Player.Player;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Data
public class SpellService {

    private SpellPlayerDao spellPlayerDao;

    @Autowired
    public SpellService(SpellPlayerDao spellPlayerDao) {
        this.spellPlayerDao = spellPlayerDao;
    }

    @Transactional
    public List<Spell> getSpellsByPlayer(Player player) {
        return spellPlayerDao.findAllByPlayer(player).stream()
                .map(spellPlayer -> Spell.getSpells().get(spellPlayer.getSpell_id()))
                .collect(Collectors.toList());
    }
}
