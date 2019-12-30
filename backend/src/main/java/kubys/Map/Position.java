package kubys.Map;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

@Getter
@ToString
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Position {
    @Column private int x;
    @Column private int y;
    @Column private int z;

    private static HashSet<Position> instancesStore = new HashSet<>();



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
        if (Math.abs(x) > Map.MAX_X_SIZE ||
                Math.abs(y) > Map.MAX_Y_SIZE ||
                Math.abs(z) > Map.MAX_Z_SIZE)
            throw new IndexOutOfBoundsException("Cell ( + " + x + ";" + y + ";" + z + ") is outside the map");
        else {
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
}
