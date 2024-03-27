package Project.Reservation;

import Project.Exception.ExitException;
import Project.FilesIO.FileDataManager;
import Project.Movie;
import Project.OutputView;
import Project.Payment.PaymentManager;
import Project.Reservation.MovieScreeningInfo;
import Project.Reservation.Reservation;
import Project.Reservation.ScreeningTime;
import Project.Schedule;
import Project.Seat;
import Project.User.Client;
import Project.User.User;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReservationManager {

    private static final Scanner sc = new Scanner(System.in);
    private static final String EXIT_COMMAND = "exit";
    private static String menu;
    private static String seatInfo;         //좌석 정보 (ex: a1)
    private static Integer[] seatNumber;    //좌석 정보 (ex: [0, 0])

    private static String selectedDate; //영화 선택 날짜
    // 상영관 및 시간대 정보
    private static String[] theaters = {"1관", "2관", "3관"};
    private static ScreeningTime[] times = {ScreeningTime.time_09, ScreeningTime.time_12, ScreeningTime.time_18};

    static final String[] RAINBOW_COLORS = {
            "\u001B[31m",   // Red
            "\u001B[38;5;208m", //Orange
            "\u001B[33m",   // Yellow
            "\u001B[32m",   // Green
            "\u001B[34m",   // Blue
            "\u001B[36m",   // Cyan
            "\u001B[35m",   // Purple
    };

    public  static void start(User user) {
//        //예매 하기 출력
//        printReservationStat();

        //파일 데이터 불러오기
        Map<String, Map<Movie, Schedule[][]>> data = FileDataManager.readMovieScheduleFromFile();    // MovieSchedule.txt
        List<User> usersList = FileDataManager.readUserInfoFromFile();                              //유저정보읽기
        List<Reservation> reservationsList = FileDataManager.readReservationsFromFile();            //예매 정보 읽기

        //유저 정보 최신화 (예외처리 불필요)
        Client client = (Client) getUserInfo(user, usersList);

        //날짜 문자열만 관리
        Set<String> scheduleDatesSet = data.keySet();

        try {
            //데이터에서 현재 날짜 이후의 예매 가능 날짜 출력
            String availableDates = displayScheduleDates(scheduleDatesSet);

            //1. 사용자에게 영화 상영 날짜를 입력받는다.
            inputSelectDate(scheduleDatesSet,availableDates);

            //2. 입력받은 날짜의 상영 정보를 저장하고 보여준다.
            Map<Integer, MovieScreeningInfo> scheduleNumbersMap = getAndPrintMovieSchedules(data);      //영화 상영 스케쥴에 넘버링한 자료구조(메인 데이터 관리)

            //3.사용자에게 원하는 영화 스케쥴을 입력받는다.
            Integer choiceSchedule = inputChoiceSchedule(scheduleNumbersMap);

            // 필요한 데이터 저장 (선택한 영화, 상영관 번호, 상영 시간 번호, 상영관 좌석 정보)
            Movie choiceMovie = scheduleNumbersMap.get(choiceSchedule).getMovie();  //선택한 영화 정보
            int row = scheduleNumbersMap.get(choiceSchedule).getI();              //선택한 상영관
            int col = scheduleNumbersMap.get(choiceSchedule).getJ();              //선택한 시간
            Seat[][] seats = scheduleNumbersMap.get(choiceSchedule).getSchedule().getSeats();   //이걸로 변경

            //4 좌석 선택
            String movieScheduleInfo = printOneLine(new String[]{choiceMovie.getTitle(), String.valueOf(times[col].getTime()), theaters[row]});
            choiceSeatProcess(seats,movieScheduleInfo);

            //5. 결제 여부 확인
            checkPaymentMovie();

            //6. 결제 (결제 실패시 메뉴로 이동)
            moviePaymentProcess(choiceMovie, client);

            //7. 예매 정보 저장
            updateScheduleInfo(scheduleNumbersMap, choiceSchedule);

            //8. 예매 내역 생성
            //8.1 예매 번호 생성
            String ticketNumber = ticketNumberGenerator(row, col);

            //8.2 예매 정보 생성
            Reservation myReservation = Reservation.builder()
                    .reservationDate(LocalDateTime.now())
                    .reservationNumber(ticketNumber)
                    .user(client)
                    .movie(choiceMovie)
                    .movieDate(LocalDate.parse(selectedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .theater(new Integer[] {row, col})
                    .seat(seats[seatNumber[0]][seatNumber[1]])
                    .moviePrice(choiceMovie.getPrice())
                    .build();

            OutputView.movietiketPrint(myReservation);
            //8 파일 덮어쓰기
            FileDataManager.writeMovieScheduleToFile(data);     //MovieSchedule.txt
            writeTextData(reservationsList, myReservation);     //Reservations.txt
            writeTextData(usersList, myReservation, client);    //Users.txt


        } catch (ExitException e) {
            System.out.println(e.getMessage());
        } finally {
            menu = null;
            seatInfo = null;
            seatNumber = null;
            selectedDate = null;
        }
    }

    ///////////////////////////////////////   Service   /////////////////////////////////////////////////////
    //유저 정보 가져오기
    private static User getUserInfo(User user, List<User> usersList) {
        Optional<User> foundUserOptional  = usersList.stream()
                .filter(findUser -> findUser.getEmail().equals(user.getEmail()))
                .findFirst();

        return foundUserOptional.orElse(null);
    }

    private static String checkInputMenu() {
        menu = sc.nextLine();
        if(menu.equals(EXIT_COMMAND)) throw new ExitException();
        return menu;
    }


    private static void checkInputSeatInfo() {
        seatInfo = sc.nextLine();
        if(seatInfo.equals(EXIT_COMMAND)) throw new ExitException();
    }

    private static String displayScheduleDates(Set<String> scheduleDatesSet) {
        List<LocalDate> sortedDates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (String dateString : scheduleDatesSet) {
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            sortedDates.add(localDate);
        }
        Collections.sort(sortedDates);
        return printSortedMovieScheduleDates(sortedDates);
    }


    //예매를 선택하는 날짜 입력받기
    private static void inputSelectDate(Set<String> scheduleDatesSet,String availableDates) {
        availableDates += "영화 상영 날짜를 입력하세요 [ex) 2024-03-26]";
        OutputView.printInputMessage(availableDates);
        do {
            selectedDate = sc.nextLine();
            if(selectedDate.equals(EXIT_COMMAND)) throw new ExitException();
            if (!isValidationDate(selectedDate) || !isLocalDateFormat(selectedDate)) {
                //잘못 입력하셨습니다.
                OutputView.printExceptionMessage("유효하지 않은 날짜형식입니다.");
//                checkInputMenu();
            } else if (!scheduleDatesSet.contains(selectedDate)) {
                //상영 가능한 날짜 없습니다.
                OutputView.printExceptionMessage("해당 날짜에는 상영 정보가 없습니다.");
//                checkInputMenu();
            } else {
                break;
            }
        } while(true);
    }

    //날짜 문자열 유효성 검사
    private static boolean isValidationDate(String inputDate) {
        String regex = "\\d{4}-\\d{2}-\\d{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputDate);
        return matcher.matches();
    }

    //날짜 문자열 LocalDate 타입 변환 검증
    private static boolean isLocalDateFormat(String inputDate) {
        try {
            LocalDate localDate = LocalDate.parse(inputDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return true;
        } catch (DateTimeException e) {
            return false;
        }
    }

    //입력받은 날짜의 상영정보를 보여준다
    private static Map<Integer, MovieScreeningInfo> getAndPrintMovieSchedules(Map<String, Map<Movie, Schedule[][]>> data) {
        Map<Integer, MovieScreeningInfo> scheduleNumbersMap = new HashMap<>(); //날짜 변경에 따른 초기화
        Map<Movie, Schedule[][]> movieMap = data.get(selectedDate);     //선택한 날짜의 영화상영정보
        StringBuilder sb = new StringBuilder();

        sb.append(String.format("======== [ %s ] 영화 상영 일정] ========\n", selectedDate));

//        System.out.printf("======== [ %s ] 영화 상영 일정] ========\n", selectedDate);

        //영화 순서 정렬
        List<Map.Entry<Movie, Schedule[][]>> movieEntryList = new ArrayList<>(movieMap.entrySet());
        movieEntryList.sort(Comparator.comparing(entry -> entry.getKey().getTitle()));

        Integer scheduleNumber = 1;
        for (Map.Entry<Movie, Schedule[][]> entry : movieEntryList) {
            sb.append("제목 : " + entry.getKey().getTitle() + "\n");
            sb.append("       ");
//            System.out.println("제목 : " + entry.getKey().getTitle());
//            System.out.print("       ");
            for (ScreeningTime time : times) {
                sb.append(String.format("%10s", time.getTime()));
//                System.out.printf("%10s", time.getTime());
            }
            sb.append("\n");
//            System.out.println();
            for (int i = 0; i < entry.getValue().length; i++) {
                sb.append(String.format("%s   ", theaters[i]));
//                System.out.printf("%s   ", theaters[i]);                    // 상영관 출력
                for (int j = 0; j < entry.getValue()[0].length; j++) {
                    if (entry.getValue()[i][j] == null) {
                        sb.append("\t\t\t  |");
//                        System.out.print("\t\t\t  |");
                        continue;
                    }

                    int emptySeats = entry.getValue()[i][j].getEmpty();     //빈좌석
                    int totalSeats = entry.getValue()[i][j].getTotal();     //총좌석
                    String seatInfo = emptySeats + "/" + totalSeats;
                    scheduleNumbersMap.put(scheduleNumber, new MovieScreeningInfo(entry.getKey(), i, j, entry.getValue()[i][j]));   //schedule 넘버
                    sb.append(String.format("[%d]\t%s |", scheduleNumber++, seatInfo));
//                    System.out.printf("[%d]\t%s |", scheduleNumber++, seatInfo);
                }
                sb.append("\n");
//                System.out.println();
                sb.append("       ");
//                System.out.print("       ");
                for (int j = 0; j < times.length; j++) {
                    sb.append("----------");
//                    System.out.print("----------");
                }
                sb.append("\n");
//                System.out.println();
            }
            sb.append("\n");
//            System.out.println();
        }
        OutputView.printMovieScheduleForClient(sb.toString());
        return scheduleNumbersMap;
    }

    //3.사용자에게 원하는 영화 스케쥴을 입력받는다.
    private static Integer inputChoiceSchedule(Map<Integer, MovieScreeningInfo> scheduleNumbersMap) {
        OutputView.printInputMessageNotJump("원하는 영화의 스케쥴을 선택해 주세요. [ex) 38]    > ");
        do {
            String inputData = sc.nextLine();
            if(inputData.equals(EXIT_COMMAND)) throw new ExitException();
            if (!inputData.matches("\\d+")) {
                //숫자형식으로 입력하지 않은 경우
                OutputView.printExceptionMessage("숫자만 입력받습니다.");
//                checkInputMenu();
            } else if (!scheduleNumbersMap.containsKey(Integer.parseInt(inputData))) {
                //상영 스케쥴에 존재하지 않는 번호인 경우
                OutputView.printExceptionMessage("해당 번호에는 배치된 영화가 없습니다.");
//                checkInputMenu();
            } else {
                return Integer.parseInt(inputData);
            }
        } while(true);
    }

    //4. 좌석 선택
    private static void choiceSeatProcess(Seat[][] seats,String message) {
        String seatPosition = printMovieScheduleSeats(seats);
        OutputView.printInputMessageNotJump(message+seatPosition+"\n좌석을 선택하세요. [ex) A1 또는 a1]    > ");
        outLoop:
        do {
            //해당 상영관의 좌석 출력

            //좌석을 입력받기
            do {
//                printInputMessage("좌석을 선택하세요. [ex) A1 또는 a1]    > ");
                checkInputSeatInfo();
                if (!isValidationSeat()) {
                    //유효한 형식이 아닌 경우
                    OutputView.printExceptionMessage("올바른 입력값이 아닙니다.");
//                    checkInputSeatInfo();

                } else {
                    convertSelectSeat();    //seatinfo -> seatNumber 로 변환
                    if (!isExistSeat(seats)) {
                        OutputView.printExceptionMessage("해당 좌석은 존재하지 않습니다.");
//                        checkInputSeatInfo();

                    } else if (!isCheckEmptySeat(seats)) {
                        //이미 예매된 경우
                        OutputView.printExceptionMessage("해당 좌석은 이미 예매되었습니다");
//                        checkInputSeatInfo();
                    } else {
                        break outLoop;
                    }
                }
            } while (true);
        } while (true);
    }

    //좌석 번호 선택 이력 검증 로직(a1 또는 A1 형식으로 받도록)
    private static boolean isValidationSeat() {
        String seatPattern = "[A-Za-z][0-9]+"; // 알파벳 + 숫자 형식으로 구성되어야 함
        return seatInfo.matches(seatPattern);
    }

    //좌석 번호가 선택 범위를 벗어나는 경우
    private static boolean isExistSeat(Seat[][] seats) {
        return ((0 <= seatNumber[0]) && (seats.length > seatNumber[0]) && (0 <= seatNumber[1]) && (seats[0].length > seatNumber[1]));
    }


    //좌석 번호가 이미 예매된 경우
    public static boolean isCheckEmptySeat(Seat[][] seats) {
        return (seats[seatNumber[0]][seatNumber[1]] == null);
    }

    //좌석 변환 (seatinfo -> seatNumber 로 변환)
    private static void convertSelectSeat () {
        seatInfo = seatInfo.trim().toUpperCase();

        // 알파벳과 숫자 분리
        String rowStr = ""; // 알파벳
        String colStr = ""; // 숫자
        for (char c : seatInfo.toCharArray()) {
            if (Character.isLetter(c)) rowStr += c;
            else if (Character.isDigit(c)) colStr += c;
        }
        seatNumber = new Integer[] {rowStr.charAt(0) - 'A', Integer.parseInt(colStr) - 1};
    }

    private static void moviePaymentProcess(Movie choiceMovie,Client client) {
        Integer moviePrice = choiceMovie.getPrice();
        if (!PaymentManager.payPoint(client, moviePrice)) throw new ExitException();
    }


    private static void checkPaymentMovie() {
        OutputView.printInputMessageNotJump("해당 예매를 확정하시겠습니까? \n1 -> 예매 확정 ");
        do {
            menu = sc.nextLine();
            if(menu.equals(EXIT_COMMAND)) throw new ExitException();
            if(!menu.equals("1")) {
                OutputView.printExceptionMessage("잘못된 입력입니다. 다시 입력해주시길 바랍니다.");
            } else {
                return;
            }
        } while(true);
    }

    private static void updateScheduleInfo(Map<Integer, MovieScreeningInfo> scheduleNumbersMap, Integer choiceSchedule) {
        //좌석 정보 업데이트
        scheduleNumbersMap.get(choiceSchedule).getSchedule().getSeats()[seatNumber[0]][seatNumber[1]] = new Seat(seatNumber[0], seatNumber[1]);
        scheduleNumbersMap.get(choiceSchedule).getSchedule().seatCountDown();           //빈좌석 - 1

    }

    public static String ticketNumberGenerator(Integer row, Integer col) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        //1. 날짜(2024-03-23) -> 10진수 (20240323) -> 16진수로 변환
        String dateString = selectedDate.replace("-", "");
        int dateAsInt = Integer.parseInt(dateString);       // 날짜를 정수로 변환 (10진수)
        String hexString = Integer.toHexString(dateAsInt);  // 10진수를 16진수로 변환

        if (hexString.length() > 4) {   // 만약 16진수가 4자리를 넘으면 앞에서부터 4자리만 사용
            hexString = hexString.substring(0, 4);
        }
        sb.append(hexString).append("-");

        //2. 상영관, 상영시각, 좌석번호
        sb.append(row).append(col).append(seatInfo).append("-");

        //3. 8자리는 알파벳, 숫자의 랜덤 조합
        for (int i = 0; i < 8; i++) {
            // 0: 숫자, 1: 대문자 알파벳, 2: 소문자 알파벳
            int randomType = random.nextInt(3);

            switch (randomType) {
                case 0:
                    // 숫자 0부터 9까지의 ASCII 코드는 48부터 57까지
                    char digit = (char) (random.nextInt(10) + '0');
                    sb.append(digit);
                    break;
                case 1:
                    // 대문자 알파벳 ASCII 코드는 65부터 90까지
                    char uppercase = (char) (random.nextInt(26) + 'A');
                    sb.append(uppercase);
                    break;
                case 2:
                    // 소문자 알파벳 ASCII 코드는 97부터 122까지
                    char lowercase = (char) (random.nextInt(26) + 'a');
                    sb.append(lowercase);
                    break;
            }
            if (i == 3) sb.append("-");
        }
        return sb.toString().toUpperCase();
    }

    private static void writeTextData(List<Reservation> reservationsList, Reservation myReservation) {
        if (reservationsList == null) {
            reservationsList = new LinkedList<>();
        }
        reservationsList.add(myReservation);
        FileDataManager.writeReservationToFile(reservationsList);
    }

    private static void writeTextData(List<User> usersList, Reservation myReservation, Client client) {
        client.getReservationList().add(myReservation);
        FileDataManager.writeUserInfoToFile(usersList);
    }


    //////////////////////////////   OutputPrint   //////////////////////////////////////
    public static void printInputMessage(String message) {
        System.out.print(message);
    }

    public static void printInvalidInputMessage(String message) {
        System.out.println();
        System.out.printf("%s [1] 다시 입력 / [exit] 메뉴로 돌아가기\n", message);
    }

    public static String printOneLine(String[] strArr) {
        StringBuilder sb = new StringBuilder();
        for (String str : strArr) {
            sb.append(String.format("%s\t", str));
//            System.out.printf("%s\t", str);
        }
        sb.append("\n");
        return sb.toString();
//        System.out.println();
    }

    public static void printReservationStat() {
        System.out.println("===============================================");
        System.out.println("==================  예매 하기  ==================");
        System.out.println("===============================================");
        System.out.println();
    }

    public static String printSortedMovieScheduleDates(List<LocalDate> sortedDates) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        System.out.print("영화 상영 날짜 : ");
        StringBuilder sb = new StringBuilder();
        sb.append("선택가능한 영화 상영날짜는 다음과 같습니다.\n");
        for (LocalDate localDate : sortedDates) {
            String formattedDate = localDate.format(formatter);
            sb.append(formattedDate).append("\n");
//            System.out.printf("\t[%s]", formattedDate);.
        }
//        System.out.println();
        return sb.toString();
    }

    public static String printMovieScheduleSeats(Seat[][] seats) {
        StringBuilder sb = new StringBuilder();
        // 좌석 맵 출력
        sb.append("============= 좌석 정보 ============\n");
//        System.out.println("============= 좌석 정보 ============");
        sb.append("============= SCREEN =============\n");
//        System.out.println("============= SCREEN =============");
        sb.append("   ");
//        System.out.print("   ");

        // 열 번호 출력
        for (int j = 0; j < seats[0].length; j++) {
            sb.append(String.format("%-6s", "  " + (j + 1)));
//            System.out.printf("%-6s", "  " + (j + 1));
        }
        sb.append("\n");
//        System.out.println();


        for (int i = 0; i < seats.length; i++) {
//            System.out.print(((char) (i + 65)) + " "); // 행 알파벳 출력
            sb.append(((char) (i + 65)) + " ");
            for (int j = 0; j < seats[0].length; j++) {
                if (seats[i][j] == null) {
                    sb.append(String.format("%-6s", "[ " + ((char) (i + 65)) + "" + (j + 1) + " ]"));
//                    System.out.printf("%-6s", "[ " + ((char) (i + 65)) + "" + (j + 1) + " ]");
                } else {
                    sb.append(String.format("%-6s", "[ X ]"));
//                    System.out.printf("%-6s", "[ X ]"); // 예약된 좌석
                }
            }
            sb.append("\n");
//            System.out.println();
        }
        sb.append("=================================\n");
        sb.append("=================================\n");
//        System.out.println("=================================");
//        System.out.println("=================================");
        return sb.toString();
    }



}