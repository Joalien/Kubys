package kubys.Spell;

import java.util.Map;

public enum SpellType {


    //TODO Add conditions about casting spell and serialize them in order to avoid switch instruction inside front
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