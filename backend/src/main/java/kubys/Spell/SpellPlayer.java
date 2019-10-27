package kubys.Spell;

import kubys.Player.Player;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Builder
@Entity(name = "spell_player")
@NoArgsConstructor // Hibernate requirement
@AllArgsConstructor
public class SpellPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(cascade=CascadeType.ALL)
    Player player;
    @Column
    @NotNull
    long spell_id;
}
