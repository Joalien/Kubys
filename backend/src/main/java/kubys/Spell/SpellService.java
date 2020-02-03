package kubys.Spell;

import kubys.Player.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Data
@AllArgsConstructor
public class SpellService {

    private SpellPlayerDao spellPlayerDao;

    @Transactional
    public List<Spell> getSpellsByPlayer(Player player) {
        return spellPlayerDao.findAllByPlayer(player).stream()
                .map(spellPlayer -> Spell.getSpells().get(spellPlayer.getSpell_id()))
                .collect(Collectors.toList());
    }

    public List<Spell> getSpellsByBreed(Breed breed) {
        return Spell.getSpells().values().stream()
                .filter(spell -> spell.getBreed().equals(breed))
                .collect(Collectors.toList());
    }
}
