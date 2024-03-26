package Project.Reservation;

import Project.Movie;
import Project.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MovieScreaningInfo {
    private Movie movie;
    private int i;
    private int j;
    private Schedule schedule;

}
