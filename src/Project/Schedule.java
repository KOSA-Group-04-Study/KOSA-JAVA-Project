package Project;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter
public class Schedule implements Serializable {
    private Seat[][] seats;
    private Integer empty;

    public Schedule(Seat[][] seats) {
        this.seats = seats;
        this.empty = 0;
    }

}
