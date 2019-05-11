package kubys.service;

import kubys.model.Spell;
import kubys.model.common.SpellType;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;

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
                .type(SpellType.CLASSIC)
                .name("Tir simple")
                .ammunition(-1)
                .minScope(1)
                .maxScope(12)
                .zone(1)
                .build());

        spells.add(Spell.builder()
                .type(SpellType.DROP)
                .name("Lacher simple")
                .ammunition(-1)
                .minScope(1)
                .maxScope(3)
                .zone(1)
                .build());

        spells.add(Spell.builder()
                .type(SpellType.THROW)
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
