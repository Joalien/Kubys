package kubys.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Position {
    @Column private int x;
    @Column private int y;
    @Column private int z;

    public static PositionBuilder builder() {
        return new PositionBuilder();
    }


    public Position addX(int x){
        return builder()
                .x(this.getX()+x)
                .y(this.getY())
                .z(this.getZ())
                .build();
    }
    public Position addY(int y){
        return builder()
                .x(this.getX())
                .y(this.getY()+y)
                .z(this.getZ())
                .build();
    }
    public Position addZ(int z){
        return builder()
                .x(this.getX())
                .y(this.getY())
                .z(this.getZ()+z)
                .build();
    }

    public static class PositionBuilder {
        private int x;
        private int y;
        private int z;

        PositionBuilder() {
        }

        public PositionBuilder x(int x) {
            this.x = x;
            return this;
        }

        public PositionBuilder y(int y) {
            this.y = y;
            return this;
        }

        public PositionBuilder z(int z) {
            this.z = z;
            return this;
        }

        public Position build() {
            return Math.abs(x) > Map.MAX_X_SIZE ||
                    Math.abs(y) > Map.MAX_Y_SIZE ||
                    Math.abs(z) > Map.MAX_Z_SIZE ? null : new Position(x, y, z);
        }

        public String toString() {
            return "Position.PositionBuilder(x=" + this.x + ", y=" + this.y + ", z=" + this.z + ")";
        }
    }
}
