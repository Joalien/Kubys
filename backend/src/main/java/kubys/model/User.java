package kubys.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@Data
@Entity
@Table(name = "Person")
public class User {
    @Id
    private String uid;

    @Column
    private String displayName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Player> players = new LinkedHashSet<>();

}