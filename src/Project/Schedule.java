package Project;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
public class Schedule {
    private Seat[][] seats;
    private Integer empty;

    public Schedule(Seat[][] seats) {
        this.seats = seats;
        this.empty = 0;
    }

}
