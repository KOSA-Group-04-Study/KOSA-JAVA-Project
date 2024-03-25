package Project;

import Project.Manager.FileDataManager;
import Project.Movie;
import Project.Schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieSchedulePrinter {
    public static void main(String[] args) {
        String[] SCREENING_TIME = {"9시", "12시", "18시"};
        String[] THEATER_NUMBER = {"1상영관", "2상영관", "3상영관"};
        int TOTAL_SEAT_NUMBER = 25;

        // Movies.txt 파일에서 영화 데이터 읽어오기
        List<Movie> movies = FileDataManager.readMoviesFromFile();

        // 읽어온 영화 데이터 출력
        if (movies != null) {
            System.out.println("\nMovies.txt:");
            System.out.println("------------------------------------");
            for (Movie movie : movies) {
                System.out.println("제목: " + movie.getTitle());
                System.out.println("상영시간: " + movie.getRunningTime() + " 분");
                System.out.println("금액: " + movie.getPrice() + " 원");
                System.out.println("------------------------------------");
            }
        } else {
            System.out.println("파일에서 읽어오는 것을 실패했습니다.");
        }

        System.out.println();

        // MovieSchedule.txt 파일에서 무비 스케줄 데이터 읽어오기
        Map<String, Map<Movie, Schedule[][]>> movieSchedule = FileDataManager.readMovieScheduleFromFile();

        // 읽어온 무비 스케줄 데이터 출력
        if (movieSchedule != null) {
            for (Map.Entry<String, Map<Movie, Schedule[][]>> entry : movieSchedule.entrySet()) {
                String date = entry.getKey();
                Map<Movie, Schedule[][]> scheduleMap = entry.getValue();

                System.out.println("------------------------------------");
                System.out.println("날짜: " + date);
                System.out.println("------------------------------------");

                for (Map.Entry<Movie, Schedule[][]> movieEntry : scheduleMap.entrySet()) {
                    Movie movie = movieEntry.getKey();
                    Schedule[][] schedules = movieEntry.getValue();

                    System.out.println("영화: " + movie.getTitle());
                    System.out.println("------------------------------------");
                    for (int i = 0; i < schedules.length; i++) {
                        for (int j = 0; j < schedules[i].length; j++) {
                            if (schedules[i][j] != null) {
                                System.out.printf("상영관: %s, 상영시간: %s,", THEATER_NUMBER[i] ,SCREENING_TIME[j]);
                                System.out.printf(" 빈 좌석수: %d%n", schedules[i][j].getEmpty());
                                // 여기서 좌석 정보를 출력할 수 있습니다.
                            }

                        }
                        System.out.println("------------------------------------");
                    }
                }
            }
        } else {
            System.out.println("파일에서 읽어오는 것을 실패했습니다.");
        }


    }
}