package kubys.Spell;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Data
@AllArgsConstructor
public class SpellService {

    public List<Spell> getSpellsByBreed(Breed breed) {
        return new Dwarf().getSpells().stream()
                .map(SpellWrapper::getSpell)
                .collect(Collectors.toList());
    }
}
