package Project;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter
public class Schedule implements Serializable {
    private Seat[][] seats;
    private Integer total;
    private Integer empty;

    public Schedule(Integer row, Integer col) {
        this.seats = new Seat[row][col];
        this.total = row * col;
        this.empty = row * col;
    }

//    public Schedule(Seat[][] seats) {
//        this.seats = seats;
//        this.empty = 0;
//    }

}
