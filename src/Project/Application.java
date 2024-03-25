package Project;

import Project.Manager.FileDataManager;
import Project.User.Client;
import Project.User.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application {
    public static void main(String[] args) {

        // 영화 정보 생성
        List<Movie> movies = createMovies();
        // 영화 정보를 파일에 저장
        FileDataManager.writeMoviesToFile(movies);

        // 영화 및 스케줄 데이터 생성
        Map<String, Map<Movie, Schedule[][]>> data = new HashMap<>();
        // 영화 스케줄을 날짜별로 저장하는 맵
        Map<Movie, Schedule[][]> scheduleData;

        // 3월 23일의 영화와 스케줄 데이터 생성
        scheduleData = createMovieSchedule();
        data.put("2024-03-23", scheduleData);

        // 3월 24일의 영화와 스케줄 데이터 생성
        scheduleData = createMovieSchedule();
        data.put("2024-03-24", scheduleData);

        // 3월 25일의 영화와 스케줄 데이터 생성
        scheduleData = createMovieSchedule();
        data.put("2024-03-25", scheduleData);

        // 생성한 데이터를 파일에 저장
        FileDataManager.writeMovieScheduleToFile(data);

        System.out.println("데이터가 성공적으로 파일에 저장되었습니다.");

        Cinema cinema = new Cinema();
        //cinema.setMovieSchedule(data);
        cinema.run();
    }

    // 영화 정보 생성
    private static List<Movie> createMovies() {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie("파묘", 110, 15000));
        movies.add(new Movie("엘리멘탈", 120, 15000));
        movies.add(new Movie("윙카", 120, 15000));
        movies.add(new Movie("서울의봄", 100, 15000));
        return movies;
    }

    // 영화 및 스케줄 데이터를 생성하는 메소드
    private static Map<Movie, Schedule[][]> createMovieSchedule() {
        Map<Movie, Schedule[][]> movieSchedule = new HashMap<>();
        for (Movie movie : FileDataManager.readMoviesFromFile()) {
            Schedule[][] schedules = createSchedules();
            movieSchedule.put(movie, schedules);
        }
        return movieSchedule;
    }

    // 스케줄 배열을 생성하는 메소드
    private static Schedule[][] createSchedules() {
        Schedule[][] schedules = new Schedule[3][3];
        for (int i = 0; i < 3; i++) {
            schedules[i] = new Schedule[3];
            for (int j = 0; j < 3; j++) {
                schedules[i][j] = new Schedule(5, 5);
            }
        }
        return schedules;
    }


}