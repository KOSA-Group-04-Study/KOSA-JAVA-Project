package Project.MovieSchedule;

import Project.Movie;
import Project.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public class AdminSchedule {
    Movie movie;
    Schedule schedule;

    public Movie getMovie() {
        return movie;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}
