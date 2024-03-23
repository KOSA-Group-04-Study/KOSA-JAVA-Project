package Project.Manager;

import Project.*;

import java.util.Map;

public class MovieScheduleManager {

    private static final String[] SCREENING_TIME = {"9시", "12시", "18시"};
    private static final String[] THEATER_NUMBER = {"1상영관", "2상영관", "3상영관"};
    private static final int TOTAL_SEAT_NUMBER = 25;

    public static void temp(Cinema cinema, String selectedDate) {

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
    // 영화 스케줄보여주기 -> 좌석 보여주기 -> 선택된 좌석 넣기

    // 관리자용 영화 스케줄 보여주기 -> 상영등록
    public static void registerMovieToSchedule() {
        Map<String, Map<Movie, Schedule[][]>> movieSchedule = FileDataManager.readMovieScheduleFromFile();
        //날짜를 입력받는다. "2024-03-25"
        //영화목록을 쭉 보여준다. ( 모든 영화 목록) -> 파일 읽기
        //영화 스케줄보여주기 -> 파일읽기 , 빈곳선택하게하고, 채우기 , 파일쓰기
    }

    public static void deleteMovieFromSchedule() {
        Map<String, Map<Movie, Schedule[][]>> movieSchedule = FileDataManager.readMovieScheduleFromFile();
        // 날짜를 입력받는다. "2024-03-25"
        // 영화를 입력시 해당 영화에 관한 스케줄을, 영화를 입력 안하면 전제 보여준다. -> 파일읽기
        //
    }

    public static Map<Movie, Schedule[][]> getMovieSchedule() {
//        System.out.println("선택하신 날짜 : "+selectedDate);
//        System.out.println("------------------------------------");
//        movieMap.forEach((movie, schedules) -> {
//            System.out.println("영화제목 = " + movie.getTitle());
//            for (int i = 0; i < 3; i++) {
//                System.out.printf("%s\n",THEATER_NUMBER[i]); //상영관 출력
//                for (int j = 0; j < 3; j++) {
//                    Schedule schedule = schedules[i][j];
//                    Integer emptySeats = schedule.getEmpty();
//                    System.out.printf("%s 좌석정보 ->  %d/%d   %d,%d \n",SCREENING_TIME[j],emptySeats,TOTAL_SEAT_NUMBER,i,j);
////                    System.out.printf("%s 좌석정보 -> \n %d/%d\n",SCREENING_TIME[j],emptySeats,TOTAL_SEAT_NUMBER);
//                }
//                System.out.println("------------------------------------");
//            }
//        });

        return null;


    }



}

