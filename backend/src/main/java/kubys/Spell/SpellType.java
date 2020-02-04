package kubys.Spell;

import java.util.Map;

public enum SpellType {

    CLASSIC("Classique"),
    DROP("Laché"),
    THROW("En cloche");

    private String label;

    SpellType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return Map.of(
                "value", this.name(),
                "label", label
        ).toString();
    }
}