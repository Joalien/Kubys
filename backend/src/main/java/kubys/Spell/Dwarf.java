package kubys.Spell;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
public class Dwarf extends Breed { // TODO make singleton
    static Map<Spell, List<Spell>> ancestors = new HashMap<>();
    static {
        Spell spell1 = Spell.builder().name("Tir simple")
                .type(SpellType.CLASSIC)
                .pa(3)
                .minScope(1)
                .maxScope(11)
                .damage(100)
                .ammunition(-1)
                .zone(1)
                .build();
        Spell spell2 = Spell.builder().name("Lacher simple")
                .type(SpellType.DROP)
                .pa(10)
                .minScope(1)
                .maxScope(2)
                .damage(150)
                .ammunition(1)
                .zone(1)
                .build();
        Spell spell3 = Spell.builder().name("Tir en cloche simple")
                .type(SpellType.THROW)
                .pa(10)
                .minScope(1)
                .maxScope(8)
                .damage(100)
                .ammunition(-1)
                .zone(1)
                .build();
        Spell spell4 = Spell.builder().name("Tir simple 4")
                .type(SpellType.CLASSIC)
                .pa(10)
                .minScope(1)
                .maxScope(8)
                .damage(100)
                .ammunition(-1)
                .zone(1)
                .build();
        Spell spell5 = Spell.builder().name("Tir simple 5")
                .type(SpellType.CLASSIC)
                .pa(10)
                .minScope(1)
                .maxScope(8)
                .damage(100)
                .ammunition(-1)
                .zone(1)
                .build();

        ancestors.put(spell1, List.of());
        ancestors.put(spell2, List.of(spell1));
        ancestors.put(spell3, List.of(spell1));
        ancestors.put(spell4, List.of(spell2, spell3));
        ancestors.put(spell5, List.of(spell4));
    }
    public Dwarf() {
        this.name = "DWARF";
        this.spells = ancestors.keySet().stream().map(SpellWrapper::new).collect(Collectors.toList());
        // TODO uncouple Breed/spell/Dwarf
    }
}
