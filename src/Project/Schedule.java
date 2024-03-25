package Project;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter
public class Schedule implements Serializable {
    private Seat[][] seats;
    private Integer empty;

    public Schedule() {
        this.seats = new Seat[5][5];
        this.empty = 25;
    }

}
