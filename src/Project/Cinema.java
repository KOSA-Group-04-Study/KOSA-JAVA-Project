package Project;

import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter @Setter
public class Cinema { // 영화관

    Map<String, Map<Movie,Schedule[][]>> movieSchedule;


    public static void run() {

        // 시스템 시작
    }


    public void setMovieSchedule(Map<String, Map<Movie, Schedule[][]>> movieSchedule) {
        this.movieSchedule = movieSchedule;
    }

    public Map<String, Map<Movie, Schedule[][]>> getMovieSchedule() {
        return movieSchedule;
    }

}
