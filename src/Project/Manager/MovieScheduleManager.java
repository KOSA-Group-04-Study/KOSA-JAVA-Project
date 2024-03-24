package Project.Manager;

import Project.*;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MovieScheduleManager {

    private static final String[] SCREENING_TIME = {"9시", "12시", "18시"};
    private static final String[] THEATER_NUMBER = {"1상영관", "2상영관", "3상영관"};
    private static final int TOTAL_SEAT_NUMBER = 25;
    private static final String dataFormat = "yyyy-MM-dd";
    private static Map<String, Map<Movie, Schedule[][]>> movieSchedules;
    private static final Map<String, AdminSchedule[][]> adminSchedules = new HashMap<>();
    private static final Scanner sc = new Scanner(System.in);


    //영화상영등록
    public static void registerMovieToSchedule() {

        //날짜를 입력받는다. 예시) "2024-03-25"   날짜받고 그 이후 3일치 -> 날짜 검증
        String selectedDate = inputDate();
        if (selectedDate.equals("exit")) return;

        //movieSchedules,adminSchedules 값을 초기화 or 업데이트한다.
        prepareData(selectedDate);

        //해당 영화 스케줄보여주기
        printScheduleFordate(selectedDate);
        //스케줄 선택한 입력받아오기
        String inputData = InputNumber(selectedDate);
        if (inputData.equals("exit")) return;

        /*
        선택한 스케줄에 영화 등록하기
        1. 영화입력받기
        2. 영화목록 읽어와서 있는영화인지 체크하기 (검증)
        3. movieSchedules 에 데이터 추가하기, adminSchedules 에 데이터 추가하기
        4. movieSchedules 을 movieSchedules.txt 에 파일쓰기
        5. movieSchedules.txt 를 읽어와서 movieSchedules 에 덮어쓰기
         */

        //파일에서 영화목록 가져와서 저장
        List<Movie> movieList;
        //영화 목록 출력
//        while (true) {
//            System.out.println("등록할 영화제목을 입력하세요");
//        }



    }

    public static void deleteMovieFromSchedule() {
        Map<String, Map<Movie, Schedule[][]>> movieSchedule = FileDataManager.readMovieScheduleFromFile();
        /*
        날짜받고 상영스케줄 보여주고 삭제할 스케줄 입력받는다.
        movieSchedules,adminSchedules 자료구조에서 각각 데이터 null 로 바꾸고
        movieSchedules 는 파일 덮어쓰기한다.
        validation -> 날짜 입력, 상영스케줄 입력(채워져있는걸 선택했는지+좌석이 하나도 안나간걸 선택한건지)
         */
    }

    //관리자용 영화 스케줄 보여주기
    public static void printScheduleFordate( String selectedDate) {
        AdminSchedule[][] adminSchedule = adminSchedules.get(selectedDate);
        System.out.println("선택하신 날짜 : " + selectedDate);

        for (int i = 0; i < 3; i++) {
            System.out.printf("%13s)\n", "(" + THEATER_NUMBER[i]); //상영관 출력
            System.out.print("(상영 시간)  :");
            for (String screen_time : SCREENING_TIME) { // 상영시간 출력
                System.out.printf("  %-10s", screen_time);
            }

            System.out.print("\n(  영화  )  : ");
            for (int j = 0; j < 3; j++) {
                if (adminSchedule[i][j] == null) { // 빈 상영시간이라면 공백을 출력
                    System.out.print("            ");
                    continue;
                }
                AdminSchedule AdminSchedule = adminSchedule[i][j];
                System.out.printf(" %-10s", AdminSchedule.getMovie().getTitle()); //영화이름 출력
            }

            System.out.print("\n(좌석 정보)  :");
            for (int j = 0; j < 3; j++) {
                if (adminSchedule[i][j] == null) { // 빈 상영시간이라면 공백을 출력
                    System.out.print("             ");
                    continue;
                }
                Schedule schedule = adminSchedule[i][j].getSchedule();
                System.out.printf("  %d/%d       ", TOTAL_SEAT_NUMBER-schedule.getEmpty(), TOTAL_SEAT_NUMBER); // 채워진좌석/총좌석 을 출력

            }
            System.out.print("\n( 입력번호 ) :");
            for (int j = 0; j < 3; j++) {
                if (adminSchedule[i][j] == null) { // 빈 상영시간이라면 공백을 출력
                    System.out.print("             ");
                    continue;
                }
                System.out.printf("   [%d]       ", (i * 3) + j + 1); // 1~9 까지의 입력번호를 가진다.
            }
            System.out.println("\n----------------------------------------");
        }
    }


    //날짜 입력 받는 메소드
    private static String inputDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(dataFormat);
        sdf.setLenient(false); // 지정한 포맷과 다르다면 예외발생
        String inputdata = "";

        while (true) {
            try {
                System.out.println("날짜를 입력하세요  예시 -> 2024-03-23 , 나가기 -> exit");
                inputdata = sc.nextLine();
                if (inputdata.equals("exit")) return inputdata;
                sdf.parse(inputdata); //포맷팅 검사
            } catch (Exception e) {
                System.out.println("잘못된 날짜입니다. 다시입력하세요 ");
                continue;
            }
            break;
        }
        return inputdata;
    }

    // 상영등록 또는 상영종료 할 스케줄의 입력번호를 받는 메소드 (입력 검증까지)
    private static String InputNumber(String selectedDate) {
        Scanner sc = new Scanner(System.in);
        String inputData = "";
        AdminSchedule[][] adminSchedule = adminSchedules.get(selectedDate);
        while (true) {
            System.out.println("입력번호를 입력해주세요 나가기 -> exit");
            try {
                inputData = sc.nextLine();
                if (inputData.equals("exit")) return inputData;
                int inputNumber = Integer.parseInt(inputData);
                if (inputNumber == 0) break;
                if (inputNumber >= 1 && inputNumber <= 9 && !validateInputNumber(inputNumber,selectedDate)) { //입력숫자의 범위와 비어있는 스케줄인지 검증
                    return inputData;
                } else {
                    throw new IllegalArgumentException();
                }
            } catch (IllegalArgumentException e) {
                System.out.println("잘못된 입력입니다.");
            }
        }
        return inputData;
    }


    private static boolean validateInputNumber(int inputNumber,String selectedDate) {
        /*
        0,0 ~ 2,2  row,col 을 1~9로 매핑했던것을 원래대로 바꾸고 해당 상영시간이 비어있으면 true 비어있지않으면 false 리턴
        1~3 -> i == 0
        4~6 -> i == 1
        7~9 -> i == 2
         */
        int i=(inputNumber-1)/3;
        int j=(inputNumber-1)%3;

        AdminSchedule[][] adminSchedule = adminSchedules.get(selectedDate);
        return adminSchedule[i][j] == null;
    }




    private static void prepareData(String selectedDate) {
        //MovieSchedule 이 null 이라면 파일을 읽어온다.
        if (movieSchedules == null) {
            movieSchedules = FileDataManager.readMovieScheduleFromFile();
        }
        //해당 날짜에 관리자스케줄 데이터가 없으면 영화스케줄 -> 관리자스케줄 데이터 변환해서 추가
        if (!adminSchedules.containsKey(selectedDate)) {
            updateAdminSchedule(selectedDate);
        }
    }

    private static void updateAdminSchedule(String selectedDate) {
        Map<Movie, Schedule[][]> movieSchedule = movieSchedules.get(selectedDate);
        AdminSchedule[][] convertedData = new AdminSchedule[3][3];
        movieSchedule.forEach((movie, schedules) -> {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (schedules[i][j] == null) continue;
                    convertedData[i][j] = new AdminSchedule(movie, schedules[i][j]);
                }
            }
        });
        adminSchedules.put(selectedDate, convertedData); // 추가,삭제된 업데이트사항을 덮어쓰기
    }


}
