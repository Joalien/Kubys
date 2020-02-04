package kubys.Spell;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
class SpellTest {

    private SpellType spellType = SpellType.CLASSIC;
    private Spell spell = Spell.builder().type(spellType).build();

    @Test
    void testSpellTypeSerialization() {
        verifySpellTypeSerialization(spellType.toString());
    }

    @Test
    void testSpellSerialization() {
        verifySpellTypeSerialization(spell.toString());
    }

    @Test
    void testSpellArraySerialization() {
        System.out.println(Arrays.toString(List.of(spell).toArray(new Spell[0])));
        verifySpellTypeSerialization(Arrays.toString(List.of(spell).toArray(new Spell[0])));
    }

    private void verifySpellTypeSerialization(String s) {
        assertTrue(s.contains("CLASSIC"));
        assertTrue(s.contains("Classique"));
    }
}