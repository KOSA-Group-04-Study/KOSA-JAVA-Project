package Project;

import java.util.HashMap;
import java.util.Map;

public class Application {
    public static void main(String[] args) {

        Map<String, Map<Movie,Schedule[][]>> data = new HashMap<>();

        Map<Movie, Schedule[][]> data1 = new HashMap<>();

        Movie 파묘 = new Movie("파묘", 60, 15000);
        Movie 엘리멘탈 = new Movie("엘리멘탈", 60, 25000);
        Movie 윙카 = new Movie("윙카", 60, 35000);
        Movie 서울의봄 = new Movie("서울의봄", 60, 45000);


        Schedule[][] schedules = new Schedule[3][3];
        for (int i = 0; i < 3; i++) {
            schedules[i] = new Schedule[3];
            for (int j = 0; j < 3; j++) {
                schedules[i][j] = new Schedule(new Seat[5][5]);
            }
        }
        Schedule[][] schedules2 = new Schedule[3][3];
        for (int i = 0; i < 3; i++) {
            schedules2[i] = new Schedule[3];
            for (int j = 0; j < 3; j++) {
                schedules2[i][j] = new Schedule(new Seat[5][5]);
            }
        }
        Schedule[][] schedules3 = new Schedule[3][3];
        for (int i = 0; i < 3; i++) {
            schedules3[i] = new Schedule[3];
            for (int j = 0; j < 3; j++) {
                schedules3[i][j] = new Schedule(new Seat[5][5]);
            }
        }
        Schedule[][] schedules4 = new Schedule[3][3];
        for (int i = 0; i < 3; i++) {
            schedules4[i] = new Schedule[3];
            for (int j = 0; j < 3; j++) {
                schedules4[i][j] = new Schedule(new Seat[5][5]);
            }
        }

        data1.put(파묘,schedules4);
        data1.put(엘리멘탈,schedules2);
        data1.put(윙카,schedules3);
        data1.put(서울의봄,schedules4);

        data.put("2024-03-22", data1);


        Cinema cinema = new Cinema();
        cinema.setMovieSchedule(data);

        cinema.run();

    }
}