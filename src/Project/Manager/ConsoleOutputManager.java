package Project.Manager;

import Project.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ConsoleOutputManager {

    private static final String[] SCREENING_TIME = {"9시", "12시", "18시"};
    private static final String[] THEATER_NUMBER = {"1상영관", "2상영관", "3상영관"};
    private static final int TOTAL_SEAT_NUMBER = 25;

    public static void printMovieScheduleList(Cinema cinema, String selectedDate) {

        Map<Movie, Schedule[][]> movieMap = cinema.getMovieSchedule().get(selectedDate);

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        StringBuilder sb3 = new StringBuilder();

        System.out.println("선택하신 날짜 : "+selectedDate);
        System.out.println("------------------------------------");
        movieMap.forEach((movie, schedules) -> {
            System.out.println("영화제목 = " + movie.getTitle());
            for (int i = 0; i < 3; i++) {
                System.out.printf("%s\n",THEATER_NUMBER[i]); //상영관 출력
                for (int j = 0; j < 3; j++) {
                    Schedule schedule = schedules[i][j];
                    Integer emptySeats = schedule.getEmpty();
                    System.out.printf("%s 좌석정보 ->  %d/%d   %d,%d \n",SCREENING_TIME[j],emptySeats,TOTAL_SEAT_NUMBER,i,j);
//                    System.out.printf("%s 좌석정보 -> \n %d/%d\n",SCREENING_TIME[j],emptySeats,TOTAL_SEAT_NUMBER);
                }
                System.out.println("------------------------------------");
            }
        });
    }
    

}

