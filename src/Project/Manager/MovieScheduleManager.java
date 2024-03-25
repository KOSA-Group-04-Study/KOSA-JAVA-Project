package Project.Manager;

import Project.*;
import lombok.*;

import java.text.SimpleDateFormat;
import java.util.*;

public class MovieScheduleManager {

    private static final String[] SCREENING_TIMES = {"9시", "12시", "18시"};
    private static final String[] THEATER_NUMBERS = {"1상영관", "2상영관", "3상영관"};
    private static final int TOTAL_SEAT_NUMBER = 25;
    private static final String dataFormat = "yyyy-MM-dd";
    private static final String EXIT_COMMAND = "exit";
    private static final String REGISTRATION = "등록";
    private static final String DELETION = "삭제";
    private static Map<String, Map<Movie, Schedule[][]>> movieSchedules;
    private static final Map<String, AdminSchedule[][]> adminSchedules = new HashMap<>();
    private static final Scanner sc = new Scanner(System.in);


    //영화상영등록
    public static void registerMovieToSchedule() {

        System.out.println("****영화 상영 등록을 진행합니다.****");

        //날짜 입력받기 +검증 + 데이터 초기화, 업데이트 + 해당 영화 스케줄보여주기
        String selectedDate = printScheduleForInputDate();
        if (selectedDate.equals("exit")) return;
        //스케줄 선택한 입력받아오기, 입력에서 row,col 받아오기
        SelectedScheduleInfo selectedScheduleInfo = inputNumber(selectedDate, REGISTRATION);

        // 영화이름 입력받고 영화객체 반환
        selectedScheduleInfo.setMovie(inputMovieName());
        // movieSchedules 에 값추가
        addDataToMovieSchedule(selectedDate, selectedScheduleInfo.movie, selectedScheduleInfo.theaterNum, selectedScheduleInfo.screenTimeNum);

        // adminSchedule 에 값 추가
        updateAdminSchedule(selectedDate);
        // movieSchedules 에 덮어쓰기
        FileDataManager.writeMovieScheduleToFile(movieSchedules);

        System.out.println("****영화 상영 등록이 완료되었습니다.****");
    }

    public static void deleteMovieFromSchedule() {

        System.out.println("****영화 상영 종료를 진행합니다.****");

        //날짜 입력받기 +검증 + 데이터 초기화, 업데이트 + 해당 영화 스케줄보여주기
        String selectedDate = printScheduleForInputDate();
        if (selectedDate.equals(EXIT_COMMAND)) return;
        //스케줄 선택한 입력받아오기, 입력에서 row,col 받아오기
        SelectedScheduleInfo selectedScheduleInfo = inputNumber(selectedDate, DELETION);
        // 날짜 ,영화 row col을 이용해서 해당 스케줄을 null로 수정 -> 삭제처리
        deleteDataFromMovieSchedule(selectedDate, selectedScheduleInfo.movie, selectedScheduleInfo.theaterNum, selectedScheduleInfo.screenTimeNum);
        updateAdminSchedule(selectedDate); // AdminSchedule 도 최신화
        FileDataManager.writeMovieScheduleToFile(movieSchedules); // 덮어쓰기

        System.out.println("****영화 상영 종료가 완료되었습니다.****");

    }

    //관리자용 영화 스케줄 보여주기 + (날짜받고검증반환) + (데이터가져오고최신화)
    public static String printScheduleForInputDate() {
        String selectedDate = inputDate();
        if(selectedDate.equals(EXIT_COMMAND)) return selectedDate;
        prepareData(selectedDate);

        AdminSchedule[][] adminSchedule = adminSchedules.get(selectedDate);
        System.out.println("선택하신 날짜 : " + selectedDate);

        for (int i = 0; i < 3; i++) {
            System.out.printf("%13s)\n", "(" + THEATER_NUMBERS[i]); //상영관 출력
            System.out.print("(상영 시간)  :");
            for (String screen_time : SCREENING_TIMES) { // 상영시간 출력
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
                System.out.printf("  %d/%d       ", schedule.getEmpty(), TOTAL_SEAT_NUMBER); // 채워진좌석/총좌석 을 출력

            }
            System.out.print("\n( 입력번호 ) :");
            for (int j = 0; j < 3; j++) {
                System.out.printf("   [%d]       ", (i * 3) + j + 1); // 1~9 까지의 입력번호를 가진다.
            }
            System.out.println("\n----------------------------------------");
        }
        return selectedDate;
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
                if (inputdata.equals(EXIT_COMMAND)) return inputdata;
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
    private static SelectedScheduleInfo inputNumber(String selectedDate, String type) {
        Scanner sc = new Scanner(System.in);
        String inputData = "";
        AdminSchedule[][] adminSchedule = adminSchedules.get(selectedDate);
        while (true) {
            System.out.println("입력번호를 입력해주세요");
            try {
                inputData = sc.nextLine();
//                if (inputData.equals(EXIT_COMMAND)) return inputData;
                int inputNumber = Integer.parseInt(inputData);
                if (inputNumber >= 1 && inputNumber <= 9) { //입력숫자의 범위와 비어있는 스케줄인지 검증
                    return validateInputNumber(inputNumber, selectedDate, type);
                } else {
                    throw new InputException("입력숫자 범위에 벗어났습니다.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("숫자만 입력가능합니다.");
            } catch (InputException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static Movie inputMovieName() {
        //파일에서 영화목록 가져오기
        List<Movie> movieList = FileDataManager.readMoviesFromFile();
        System.out.println("영화목록");
        movieList.stream().map(Movie::getTitle).forEach(System.out::println);
//        movieList.forEach(movie -> System.out.println(movie.getTitle()));
        //영화 목록 출력
        while (true) {
            System.out.println("등록할 영화제목을 입력하세요");
            String input = sc.nextLine();
            Optional<Movie> inputMovie = movieList.stream().filter((movie) -> movie.getTitle().equals(input)).findFirst();
            if (inputMovie.isPresent()) {
                return inputMovie.get();
            } else {
                System.out.println("존재하지않는 영화입니다.");
            }
        }
    }


    private static SelectedScheduleInfo validateInputNumber(int inputNumber, String selectedDate, String type) {
        /*
        0,0 ~ 2,2  row,col 을 1~9로 매핑했던것을 원래대로 바꾸고 해당 상영시간이 비어있으면 true 비어있지않으면 false 리턴
        1~3 -> i == 0
        4~6 -> i == 1
        7~9 -> i == 2
         */
        int i=(inputNumber-1)/3;
        int j=(inputNumber-1)%3;

        AdminSchedule[][] adminSchedule = adminSchedules.get(selectedDate);
        SelectedScheduleInfo selectedScheduleInfo = new SelectedScheduleInfo();
        if (type.equals(REGISTRATION)) {
            if (adminSchedule[i][j] == null) {  // 해당 스케줄에 등록된 영화가 없다면 해당 스케줄정보 반환
                selectedScheduleInfo.setTheaterNum(i);
                selectedScheduleInfo.setScreenTimeNum(j);
                return selectedScheduleInfo;
            }
            else{
                throw new InputException("해당 스케줄에는 이미 등록되어있는 영화가 있습니다.");
            }

        } else if (type.equals(DELETION) ) {
            if (adminSchedule[i][j] != null) { // 해당 스케줄에 등록된 영화가 있다면 해당 스케줄정보 반환
                if (adminSchedule[i][j].getSchedule().getEmpty() != TOTAL_SEAT_NUMBER) {
                    //해당 스케줄의 영화를 예매한 사람이 있다면 삭제 X
                    throw new InputException("해당 스케줄에는 등록되어있는 영화는 예매자가 있어 삭제가 불가합니다.");
                }
                selectedScheduleInfo.setMovie(adminSchedule[i][j].getMovie());
                selectedScheduleInfo.setTheaterNum(i);
                selectedScheduleInfo.setScreenTimeNum(j);
                return selectedScheduleInfo;
            } else {
                throw new InputException("해당 스케줄에는 등록되어있는 영화가 없습니다.");
            }

        } else {
            throw new InputException("등록 또는 삭제만 가능합니다.");
        }
    }




    private static void prepareData(String selectedDate) {
        //MovieSchedule 이 null 이라면 파일을 읽어온다.
        if (movieSchedules == null) {
            movieSchedules = FileDataManager.readMovieScheduleFromFile();
        }
        //해당 날짜에 movieSchedules 데이터가 없으면 초기화
        if (!movieSchedules.containsKey(selectedDate)) {
            movieSchedules.put(selectedDate, new HashMap<>());
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


    private static void addDataToMovieSchedule(String selectedDate,Movie movie,int theaterNum, int screenTimeNum) {
        if(!movieSchedules.get(selectedDate).containsKey(movie)) {
            movieSchedules.get(selectedDate).put(movie, new Schedule[THEATER_NUMBERS.length][SCREENING_TIMES.length]);
            Schedule[][] schedules = movieSchedules.get(selectedDate).get(movie);
            for (int i = 0; i < 3; i++) {
                schedules[i] = new Schedule[SCREENING_TIMES.length];
            }
        }
        movieSchedules.get(selectedDate).get(movie)[theaterNum][screenTimeNum] = new Schedule(5,5);
    }

    private static void deleteDataFromMovieSchedule(String selectedDate,Movie movie,int theaterNum, int screenTimeNum ) {
        movieSchedules.get(selectedDate).get(movie)[theaterNum][screenTimeNum] = null;
    }

    @Setter
    static class SelectedScheduleInfo {
        int theaterNum; // 행 == 상영관번호
        int screenTimeNum; // 열 == 상영시간번호
        Movie movie;
    }
    @AllArgsConstructor
    @ToString
    @Getter
    static public class AdminSchedule {
        Movie movie;
        Schedule schedule;
    }

    static class InputException extends RuntimeException {
        public InputException(String message) {
            super(message);
        }
    }



}
