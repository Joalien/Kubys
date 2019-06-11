package kubys.model.common;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Builder
@Data
@Embeddable
public class Position {
    @Column private int x;
    @Column private int y;
    @Column private int z;


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
