package kubys.model.common;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Position {
    private int x;
    private int y;
    private int z;


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

}
