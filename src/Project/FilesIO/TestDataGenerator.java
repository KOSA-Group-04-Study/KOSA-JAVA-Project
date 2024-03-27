package Project.FilesIO;

import Project.Movie;
import Project.Reservation.Reservation;
import Project.Schedule;
import Project.User.Admin;
import Project.User.Client;
import Project.User.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestDataGenerator {

    public static void testDataGenerate() {
        List<Movie> movies = createMovies();
        List<User> users = createUser();
        Map<String, Map<Movie, Schedule[][]>> movieSchedules = createMovieSchedules();

        // 영화 정보를 파일에 저장
        FileDataManager.writeMoviesToFile(movies);
        // 사용자 정보 파일에 저장
        FileDataManager.writeUserInfoToFile(users);
        // 영화스케줄 정보 파일에 저장
        FileDataManager.writeMovieScheduleToFile(movieSchedules);

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

    // 관리자,사용자 한명씩
    private static List<User> createUser() {
        List<User> list = new ArrayList<>();
        list.add(new Client("q@naver.com", "1q2w3e4r!", "a", "010-1234-1234", false, 0, new ArrayList<>()));
        list.add(new Admin("q2@naver.com", "1q2w3e4r!", "a", "010-1234-1234", true));
        return list;
    }

    private static Map<String, Map<Movie, Schedule[][]>> createMovieSchedules() {
        // 날짜 , <영화,스케줄>
        Map<String, Map<Movie, Schedule[][]>> movieSchedules = new HashMap<>();
        // <영화,스케줄>
        Map<Movie, Schedule[][]> scheduleData = new HashMap<>();
        // 영화리스트
        List<Movie> movieList = createMovies();

        Schedule[][] schedules = createSchedules();
        schedules[0][0] = new Schedule(5, 5); // 1상영관 9시
        schedules[1][0] = new Schedule(5, 5); // 2상영관 9시
        scheduleData.put(movieList.get(0), schedules);
        movieSchedules.put("2024-03-26", scheduleData);

        schedules = createSchedules();
        schedules[0][2] = new Schedule(5, 5); // 1상영관 18시
        schedules[2][0] = new Schedule(5, 5); // 3상영관 9시
        scheduleData.put(movieList.get(1), schedules); // 엘리멘탈
        movieSchedules.put("2024-03-26", scheduleData);

        schedules = createSchedules();
        schedules[0][1] = new Schedule(5, 5); // 1상영관 12시
        schedules[2][1] = new Schedule(5, 5); // 3상영관 12시
        scheduleData.put(movieList.get(2), schedules); // 윙카
        movieSchedules.put("2024-03-27", scheduleData);

        schedules = createSchedules();
        schedules[1][2] = new Schedule(5, 5); // 2상영관 18시
        schedules[2][2] = new Schedule(5, 5); // 3상영관 18시
        scheduleData.put(movieList.get(3), schedules); // 서울의봄
        movieSchedules.put("2024-03-27", scheduleData);

        return movieSchedules;
    }

    // 스케줄 배열을 생성 및 초기화, 설정 , 반환
    private static Schedule[][] createSchedules() {
        Schedule[][] schedules = new Schedule[3][3];
        for (int i = 0; i < 3; i++) {
            schedules[i] = new Schedule[3];
        }
        return schedules;
    }


}
