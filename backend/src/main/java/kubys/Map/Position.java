package kubys.Map;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@ToString
@Embeddable
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class Position {
    @Column private int x;
    @Column private int y;
    @Column private int z;

    private static Set<Position> instancesStore = ConcurrentHashMap.newKeySet();

    public Position plusX(int x) {
        return Position.of(this.getX() + x, this.getY(), this.getZ());
    }
    public Position plusY(int y) {
        return Position.of(this.getX(), this.getY() + y, this.getZ());
    }
    public Position plusZ(int z) {
        return Position.of(this.getX(), this.getY(), this.getZ() + z);
    }

    public static Position of(int x, int y, int z) {
        Optional<Position> optionalPosition = instancesStore.parallelStream()
                                                            .filter(p -> p.x == x && p.y == y && p.z == z)
                                                            .findFirst();
        if (optionalPosition.isPresent()) {
            return optionalPosition.get();
        } else {
            Position position = new Position(x, y, z);
            instancesStore.add(position);
            return position;
        }
    }
}
