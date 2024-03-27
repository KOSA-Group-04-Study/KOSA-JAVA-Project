package Project.MovieSchedule;

import Project.*;
import Project.Exception.ExitException;
import Project.FilesIO.FileDataManager;
import Project.User.Client;
import Project.User.User;
import lombok.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
영화 상영등록, 영화 상영종료 ,영화 상영스케줄 조회 ,회원 스케줄 정보 조회 기능이 있는 헬퍼클래스
 */
public class MovieScheduleManager {

    private static final String[] SCREENING_TIMES = {"9시", "12시", "18시"};
    private static final String[] THEATERS = {"1상영관", "2상영관", "3상영관"};
    private static final int TOTAL_SEAT_NUMBER = 25;
    private static final int SEAT_ROW_COUNT = 5;
    private static final int SEAT_COLUMN_COUNT = 5;
    private static final String DATA_FORMAT = "yyyy-MM-dd";
    private static final String EXIT_COMMAND = "exit";
    private static final String REGISTRATION = "등록";
    private static final String DELETION = "삭제";
    private static Map<String, Map<Movie, Schedule[][]>> movieSchedules;
    private static final Map<String, AdminSchedule[][]> adminSchedules = new HashMap<>();
    private static final Scanner sc = new Scanner(System.in);

    private MovieScheduleManager() {
        // 헬퍼클래스의 객체생성을 막는다.
    }

    //영화상영등록
    public static void registerMovieToSchedule() {

        System.out.println("****영화 상영 등록을 진행합니다.****");

        try {
            //날짜를 입력받아 관리자용 영화 스케줄보여주기
            String selectedDate = printScheduleForInputDate();

            //선택한 스케줄 입력번호 받아오기(+검증) , 해당 입력번호에 대한 스케줄정보 반환 ( 등록시 -> 스케줄정보(무비,영화관인덱스,상영시간인덱스))
            SelectedScheduleInfo selectedScheduleInfo = inputNumber(selectedDate, REGISTRATION);

            // 영화이름 입력받고 영화객체 반환 후 선택된 상영스케줄정보에 저장
            selectedScheduleInfo.setMovie(inputMovieName());

            // movieSchedules 에  입력된 날짜,영화,상영관번호,상영시간의 정보를 이용해서 새로운 영화상영스케줄 저장
            addNewScheduleToMovieSchedule(selectedDate, selectedScheduleInfo);

            // movieSchedules 의 변경사항을 adminSchedule 에도 업데이트한다.
            updateAdminSchedule(selectedDate);

            // movieSchedules 을 movieSchedules.txt 에 파일쓰기하여 movieSchedules 의 변경사항을 업데이트한다.
            FileDataManager.writeMovieScheduleToFile(movieSchedules);

        } catch (ExitException e) { // 종료커맨드입력시 발생되는 예외
            System.out.println(e.getMessage());
            return; // 메뉴로 보낸다.
        }
        System.out.println("****영화 상영 등록이 완료되었습니다.****");
    }

    public static void deleteMovieFromSchedule() {

        System.out.println("****영화 상영 종료를 진행합니다.****");

        try {
            //날짜를 입력받아 관리자용 영화 스케줄보여주기
            String selectedDate = printScheduleForInputDate();

            //선택한 스케줄 입력번호 받아오기(+검증) , 해당 입력번호에 대한 스케줄정보 반환 ( 삭제시 -> 스케줄정보(영화관인덱스,상영시간인덱스))
            SelectedScheduleInfo selectedScheduleInfo = inputNumber(selectedDate, DELETION);

            // movieSchedules 에  입력된 날짜,영화,상영관번호,상영시간의 정보를 이용해서 원래있던 영화상영스케줄 삭제
            deleteDataFromMovieSchedule(selectedDate, selectedScheduleInfo);

            // movieSchedules 의 변경사항을 adminSchedule 에도 업데이트한다.
            updateAdminSchedule(selectedDate);

            // movieSchedules 을 movieSchedules.txt 에 파일쓰기하여 movieSchedules 의 변경사항을 업데이트한다.
            FileDataManager.writeMovieScheduleToFile(movieSchedules); // 덮어쓰기

        } catch (ExitException e) { // 종료커맨드입력시 발생되는 예외
            System.out.println(e.getMessage());
            return; // 메뉴로 보낸다.
        }

        System.out.println("****영화 상영 종료가 완료되었습니다.****");
    }

    //관리자용 영화 스케줄 보여주기 -> (날짜 입력받아 검증하고 날짜 반환) + (파일 읽기를 이용해 영화스케줄 전역변수 최신화) + ( 관리자용 영화 스케줄 출력)
    public static String printScheduleForInputDate() {
        String selectedDate = inputDate(); //날짜 입력받기
        prepareData(selectedDate); // 영화스케줄전역변수 업데이트

        AdminSchedule[][] adminSchedule = adminSchedules.get(selectedDate);
        OutputView.printScheduleBox(selectedDate, adminSchedule);
        return selectedDate;
    }

    //회원정보 출력 메소드
    public static void printAllUsers() {
        List<User> users = FileDataManager.readUserInfoFromFile();
        for (User user : users) {
            System.out.println("-------------------");
            System.out.println("사용자 정보:");
            System.out.println("아이디: " + user.getEmail());
            System.out.println("이름: " + user.getName());
            System.out.println("전화번호: " + user.getPhoneNumber());
            System.out.println("관리자 여부: " + (user.isAdmin() ? "O" : "X"));
            if (user instanceof Client client) {
                System.out.println("포인트 :" + client.getPoint());
            }
            System.out.println("-------------------");
        }
    }

    //날짜 입력 받는 메소드
    private static String inputDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(DATA_FORMAT);
        sdf.setLenient(false); // 지정한 포맷과 다르다면 예외발생
        String inputdata = "";
        String ment = """
                날짜를 입력하세요  예시 -> 2024-03-23
                    """;
        OutputView.printInputMessage(ment);
        while (true) {
            try {
                System.out.print("\tplease input ->  ");
                inputdata = sc.nextLine();
                if (inputdata.equals(EXIT_COMMAND)) throw new ExitException();
                sdf.parse(inputdata); //포맷팅 검사
            } catch (ParseException e) {
                OutputView.printExceptionMessage("잘못된 날짜입니다. 다시입력하세요 ");
                continue;
            }
            break;
        }
        return inputdata;
    }

    // 상영등록 또는 상영종료 할 스케줄의 입력번호를 받는 메소드 (입력 검증까지)
    private static SelectedScheduleInfo inputNumber(String selectedDate, String methodType) {
        while (true) {
            System.out.println("입력번호를 입력해주세요 나가기 -> exit");
            String inputData = sc.nextLine();
            if (inputData.equals(EXIT_COMMAND)) throw new ExitException(); //exit입력시 탈출예외던지기
            try {
                int inputNumber = Integer.parseInt(inputData); //int 값으로 변환 입력된값이 숫자가 아니면 IllegalArgumentException 예외발생
                if (inputNumber >= 1 && inputNumber <= 9) { //입력숫자의 범위와 비어있는 스케줄인지 검증
                    return validateInputNumber(inputNumber, selectedDate, methodType); //입력번호의 스케줄이 상영등록또는 취소가 가능한 상황인지 체크
                } else {
                    System.out.println("입력범위를 벗어났습니다. 1~9만 입력가능");
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
        //영화 목록 출력
        movieList.forEach(movie -> System.out.println(movie.getTitle()));
        while (true) {
            System.out.println("등록할 영화제목을 입력하세요 나가기 -> exit");
            String input = sc.nextLine();
            if (input.equals(EXIT_COMMAND)) throw new ExitException(); // 탈출커맨드 입력시 탈출예외 던지기
            //파일에서 가져온 영화리스트에서 사용자가 입력한 영화이름과 일치하는 영화 찾기
            Optional<Movie> inputMovie = movieList.stream().filter((movie) -> movie.getTitle().equals(input)).findFirst();
            if (inputMovie.isPresent()) { //Optional 안의 객체가 존재한다면 (= 영화를 찾았다면)
                return inputMovie.get();
            } else {
                System.out.println("존재하지않는 영화입니다.");
            }
        }
    }
    private static SelectedScheduleInfo validateInputNumber(int inputNumber, String selectedDate, String methodType) {
        /*
        0,0 ~ 2,2  row,col 을 1~9로 매핑했던것을 원래대로 바꾸고 해당 상영시간이 비어있으면 true 비어있지않으면 false 리턴
        1~3 -> i == 0
        4~6 -> i == 1
        7~9 -> i == 2
         */
        int theater = (inputNumber - 1) / 3; // 상수배열 THEATERS 의 인덱스로 변환
        int screeningTime = (inputNumber - 1) % 3; // 상수배열  SCREENING_TIMES 의 인덱스로 변환

        AdminSchedule[][] adminSchedule = adminSchedules.get(selectedDate);
        SelectedScheduleInfo selectedScheduleInfo = new SelectedScheduleInfo();
        if (methodType.equals(REGISTRATION)) { // 영화상영등록 메서드에서 호출했을때 실행되는 부분.
            if (adminSchedule[theater][screeningTime] == null) {  // 선택한 상영시간 상영관에 등록된 스케줄(영화)이 없어야한다.
                // 선택한 상영시간+상영관의 정보를 담아준다.
                selectedScheduleInfo.setTheater(theater);
                selectedScheduleInfo.setScreenTime(screeningTime);
                return selectedScheduleInfo; //선택된 상영스케줄정보를 반환
            } else {
                throw new InputException("해당 스케줄에는 이미 등록되어있는 영화가 있습니다.");
            }

        } else if (methodType.equals(DELETION)) { // 영화상영종료 메서드에서 호출했을때 실행되는 부분.
            if (adminSchedule[theater][screeningTime] != null) { // 해당 스케줄에 등록된 영화가 있다면 해당 스케줄정보 반환
                if (adminSchedule[theater][screeningTime].getSchedule().getEmpty() != TOTAL_SEAT_NUMBER) {
                    //해당 스케줄의 영화를 예매한 사람이 있다면 삭제 X
                    throw new InputException("해당 스케줄에는 등록되어있는 영화는 예매자가 있어 삭제가 불가합니다.");
                }
                selectedScheduleInfo.setMovie(adminSchedule[theater][screeningTime].getMovie());
                selectedScheduleInfo.setTheater(theater);
                selectedScheduleInfo.setScreenTime(screeningTime);
                return selectedScheduleInfo;
            } else {
                throw new InputException("해당 스케줄에는 등록되어있는 영화가 없습니다.");
            }
        } else {
            throw new InputException("등록 또는 삭제만 가능합니다.");
        }
    }


    // 영화스케줄을 파일에서 읽어와서 영화스케줄전역변수에 넣어준다.
    private static void prepareData(String selectedDate) {
        //영화스케줄전역변수가 null 이라면 파일을 읽어온다. -> 한번도 실행이 되지않아 채워지지않아있다면 파일에서 읽어온다.
        if (movieSchedules == null) {
            movieSchedules = FileDataManager.readMovieScheduleFromFile();
        }
        //해당 날짜 영화스케줄에 데이터가 없으면 초기화
        if (!movieSchedules.containsKey(selectedDate)) {
            movieSchedules.put(selectedDate, new HashMap<>());
        }
        //해당 날짜에 관리자스케줄 데이터가 없으면 영화스케줄를 이용해서 관리자스케줄 업데이트
        if (!adminSchedules.containsKey(selectedDate)) {
            updateAdminSchedule(selectedDate);
        }
    }

    // 영화스케줄을 이용해서 관리자용 상영스케줄 조회에 사용되는 관리자용스케줄을 업데이트하는 메소드
    private static void updateAdminSchedule(String selectedDate) {
        Map<Movie, Schedule[][]> movieSchedule = movieSchedules.get(selectedDate); // 해당 날짜의 영화들 스케줄을 가져온다.
        AdminSchedule[][] adminSchedule = new AdminSchedule[THEATERS.length][SCREENING_TIMES.length]; // 상영관개수 x 상영시간개수의 크기로 생성
        movieSchedule.forEach((movie, schedules) -> { //해당 날짜의 영화들 스케줄을 이용해서 관리자용 스케줄을 업데이트한다.
            for (int theater = 0; theater < THEATERS.length; theater++) {
                for (int screeningTime = 0; screeningTime < SCREENING_TIMES.length; screeningTime++) {
                    if (schedules[theater][screeningTime] == null) continue; //영화스케줄이 비어있다면 넘어가고
                    adminSchedule[theater][screeningTime] = new AdminSchedule(movie, schedules[theater][screeningTime]); //비어있지않다면 관리자용스케줄에도 채워준다.
                }
            }
        });
        adminSchedules.put(selectedDate, adminSchedule); // 영화스케줄에 업데이트사항을 관리자용스케줄에 덮어씌우는 형태로 업데이트가 진행된다.
    }


    // 해당 날짜, 해당 상영관, 해당 상영시간에 영화 스케줄을 추가해주는 메소드
    private static void addNewScheduleToMovieSchedule(String selectedDate, SelectedScheduleInfo selectedScheduleInfo) {
        //입력된 날짜 영화 스케줄에 입력된 영화의 상영정보가 없을때 == 그 날짜에는 해당영화 상영을 하지않는 상태
        if (!movieSchedules.get(selectedDate).containsKey(selectedScheduleInfo.getMovie())) {
            //스케줄 2차원배열 초기화
            Schedule[][] schedules = new Schedule[THEATERS.length][SCREENING_TIMES.length];
            for (int i = 0; i < THEATERS.length; i++) {
                schedules[i] = new Schedule[SCREENING_TIMES.length];
            }
            // 해당 날짜, 해당 영화에 초기화한 스케줄2차원배열을 넣어준다. -> 해당 날짜, 해당 영화는 상영스케줄을 받을수 있게 준비
            movieSchedules.get(selectedDate).put(selectedScheduleInfo.getMovie(), schedules);
        }
        // 해당 날짜,해당 영화,해당 상영관,해당 상영시간에 새로운 스케줄등록 -> 영화상영등록
        movieSchedules.get(selectedDate).get(selectedScheduleInfo.getMovie())[selectedScheduleInfo.getTheater()][selectedScheduleInfo.getScreenTime()]
                = new Schedule(SEAT_ROW_COUNT, SEAT_COLUMN_COUNT);
    }

    // 영화 상영종료시 영화스케줄에서 해당 날짜, 해당 영화 , 해당 상영관 ,해당 상영시간의 스케줄을 null 로 바꿔줘서 영화 상영종료처리하는 메소드
    private static void deleteDataFromMovieSchedule(String selectedDate, SelectedScheduleInfo selectedScheduleInfo) {
        movieSchedules.get(selectedDate).get(selectedScheduleInfo.getMovie())[selectedScheduleInfo.theater][selectedScheduleInfo.screenTime] = null;
    }


    /*
    내부 클래스, 내부 예외 클래스 접근제한자 설정 이유
    내부 클래스에 넣은이유는 해당 외부클래스에서만 사용되는 클래스이기 때문이다.
    private 는 외부클래스에서만 사용되게끔 하기 위함
    static 은 static 메소드 내부에서 사용 되어야 하고, 내부클래스 인스턴스 생성시 외부클래스의 참조를 갖지않게하여 GC의 처리대상이 되게하기위함
     */
    @Setter
    @Getter
    private static class SelectedScheduleInfo { // 선택된 상영스케줄(상영관+상영시간+영화)의 정보를 저장할 클래스
        int theater;  // 상수배열 THEATERS 의 인덱스 저장
        int screenTime; // 상수배열  SCREENING_TIMES 의 인덱스 저장
        Movie movie;
    }
    private static class InputException extends RuntimeException {
        public InputException(String message) {
            super(message);
        }
    }

}
