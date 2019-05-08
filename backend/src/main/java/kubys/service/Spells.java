package kubys.service;

import kubys.model.Spell;
import kubys.model.common.SpellType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
@Getter
@Slf4j
public class Spells {

//    String name;
//    SpellType spellType;
//    int pa;
//    int minScope;
//    int maxScope;
//    int damage;
//    int ammunition;// -1 if infinity
//    int zone;

    static LinkedHashSet<Spell> spells;

    static {
        spells = new LinkedHashSet<>();
        spells.add(Spell.builder()
                .spellType(SpellType.CLASSIC)
                .name("Tir simple")
                .ammunition(-1)
                .minScope(1)
                .maxScope(8)
                .zone(1)
                .build());

        spells.add(Spell.builder()
                .spellType(SpellType.DROP)
                .name("Lacher simple")
                .ammunition(-1)
                .minScope(1)
                .maxScope(2)
                .zone(1)
                .build());

        spells.add(Spell.builder()
                .spellType(SpellType.THROW)
                .name("Tir en cloche simple")
                .ammunition(-1)
                .minScope(1)
                .maxScope(10)
                .zone(1)
                .build());
    }

    public static LinkedHashSet<Spell> getSpells(){
        return spells;
    }


}
