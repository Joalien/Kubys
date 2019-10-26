package kubys.Spell;

import kubys.Player.Player;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Entity(name = "spell_player")
public class SpellPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne
    Player player;
    @Column
    @NotNull
    long spell_id;
}
