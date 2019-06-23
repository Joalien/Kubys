package kubys.User;

import com.fasterxml.jackson.annotation.JsonBackReference;
import kubys.Player.Player;
import lombok.*;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Entity
@Table(name = "Person")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Scope(scopeName = "websocket", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class User {
    @Id
    @EqualsAndHashCode.Include
    private String uid;

    @Column
    private String displayName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    @JsonBackReference
    @Builder.Default
    private List<Player> players = new ArrayList<>();

}