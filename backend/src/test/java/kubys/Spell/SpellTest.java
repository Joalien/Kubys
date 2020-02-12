package kubys.Spell;

import kubys.Player.Player;
import kubys.TestHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Slf4j
class SpellTest {

    @Nested
    class SpellDeserialization {
        private SpellType spellType = SpellType.CLASSIC;
        private Spell spell = new Dwarf().getSpells().stream().findFirst().get().spell;

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

        private void verifySpellTypeSerialization(String s) { // TODO improve me
            assertTrue(s.contains("CLASSIC"));
            assertTrue(s.contains("Classique"));
        }
    }

    @Nested
    class SpellAccess {

        @Test
        void testAccessRole() {
            Player player = TestHelper.createRandomNewPlayer();

        }
    }
}