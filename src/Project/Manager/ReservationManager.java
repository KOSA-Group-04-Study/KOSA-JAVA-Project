package Project.Manager;

import Project.*;
import Project.User.Client;
import Project.User.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReservationManager {
    // 상영관 및 시간대 정보
    private static String[] theaters = {"1관", "2관", "3관"};
    private static ScreeningTime[] times = {ScreeningTime.time_09, ScreeningTime.time_12, ScreeningTime.time_18};

    //1) 예매하기
    public static void makeMovieReservation(User user) {

        Map<String, Map<Movie,Schedule[][]>> data = FileDataManager.readMovieScheduleFromFile();    // MovieSchedule.txt
        List<User> usersList = FileDataManager.readUserInfoFromFile();                              //유저정보읽기
        List<Reservation> reservationsList = FileDataManager.readReservationsFromFile();            //예매 정보 읽기

        Optional<User> foundUserOptional  = usersList.stream()
                .filter(findUser -> findUser.getEmail().equals(user.getEmail()))
                .findFirst();

        Client client = (Client) foundUserOptional.orElse(null);

        Map<Integer, MovieScreaningInfo> scheduleNumbersMap = new HashMap<Integer, MovieScreaningInfo>();   //Schedule에 넘버링

        Scanner sc = new Scanner(System.in);

        //1. 사용자에게 영화 상영 날짜를 입력받는다. (while 문)
        System.out.println("상영할 영화 날짜를 입력해 주세요 ex) 2024-03-23");
        String selectedDate = sc.nextLine();
        //1.1 입력 날짜 검증 필요 (형식이 올바른지)
        //1.2 입력 날짜 스케쥴 존재 여부 검증 필요

        Map<Movie, Schedule[][]> movieMap = data.get(selectedDate); //선택한 날짜의 영화상영정보
        System.out.println("선택하신 날짜 : " + selectedDate);
        System.out.println("------------------------------------");

        //2. 입력받은 날짜의 상영 정보를 보여준다.
        System.out.println("=====영화 상영 일정====");
        getMovieSchedules(movieMap, scheduleNumbersMap);

        //3.사용자에게 원하는 영화 스케쥴을 입력받는다. (while 문)
        System.out.println("상영관을 선택해 주세요. (ex) [38] 선택시 : 38)");
        Integer choiceSchedule = Integer.parseInt(sc.nextLine());
        //상영관 검증 로직 필요 (존재 여부, 올바른 입력 확인)

        Movie choiceMovie = scheduleNumbersMap.get(choiceSchedule).getMovie();  //선택한 영화 정보
        Integer row = scheduleNumbersMap.get(choiceSchedule).getI();              //선택한 상영관
        Integer col = scheduleNumbersMap.get(choiceSchedule).getJ();              //선택한 시간
        System.out.println("영화 : " + choiceMovie.getTitle());
        System.out.printf("%s, (%s)\n", ReservationManager.times[col], ReservationManager.theaters[row]);

        //4 좌석 선택 (while 문)
        //4.1 해당 상영관의 좌석을 보여주기
        Seat[][] seats = movieMap.get(choiceMovie)[row][col].getSeats();
        getSeats(seats);

        //4.2 사용자에게 좌석을 입력받기
        System.out.println("좌석을 선택하세요. (예: A1 또는 a1)");
        String selectSeat = sc.nextLine();  //선택한 좌석번호
        Integer[] seatNumber = convertSelectSeat(selectSeat); // 선택한 좌석번호 변환 -> 좌석 번호 [행, 열]
        //4.3 예매 좌석 검증 필요 (null인지 아닌지 여부(예매 여부), 올바른 입력 여부)

        //5. 결제
        //...
        //... 결제 성공시 선택한 좌석 정보 저장
        //... 결제 실패시 -> 멘트 + return


        //6. 예매 정보 저장 (결제 성공시)
        //6.1 좌석 정보를 저장한다.
        seats[seatNumber[0]][seatNumber[1]] = new Seat(seatNumber[0], seatNumber[1]); //예매한 좌석 정보 저장
        movieMap.get(choiceMovie)[row][col].setEmpty(movieMap.get(choiceMovie)[row][col].getEmpty() - 1);   //빈좌석 - 1

        //6.2 예매번호 생성 후 저장
        String ticketNumber = ticketNumberGenerator(selectedDate, row, col ,selectSeat);
        System.out.println("예매가 완료되었습니다.");
        System.out.printf("예매번호 : [%s]\n", ticketNumber);

        //6.3 예매정보 내역 저장 -> Reservation
        LocalDate choiceMovieDate = LocalDate.parse(selectedDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        Reservation myReservation = Reservation.builder()
                .reservationDate(LocalDateTime.now())
                .reservationNumber(ticketNumber)
                .user(client)
                .movie(choiceMovie)
                .movieDate(choiceMovieDate)
                .theater(new Integer[] {row, col})
                .seat(seats[seatNumber[0]][seatNumber[1]])
                .moviePrice(10000)
                .build();

        //6.4 유저에 예매 정보 저장 -> User
        client.getReservationList().add(myReservation);

        //7. 파일 덮어쓰기
        //MovieSchedule.txt
        FileDataManager.writeMovieScheduleToFile(data);

        //Reservations.txt
        if (reservationsList == null) {
            reservationsList = new LinkedList<>();
        }
        reservationsList.add(myReservation);
        FileDataManager.writeReservationToFile(reservationsList);

        //Users.txt
        if (usersList == null) {
            usersList = new LinkedList<>();
            usersList.add(client);
        }

        usersList.stream().filter(finduser -> finduser.getEmail()
                        .equals(client.getEmail()))
                        .findFirst()
                        .ifPresent(finduser -> ((Client) finduser).getReservationList().add(myReservation));
        FileDataManager.writeUserInfoToFile(usersList);

    }

    //입력받은 날짜의 상영정보를 보여준다
    public static void getMovieSchedules(Map<Movie, Schedule[][]> movieMap, Map<Integer, MovieScreaningInfo> scheduleNumbersMap ) {

        Integer scheduleNumber = 1;
        for (Map.Entry<Movie, Schedule[][]> entry : movieMap.entrySet()) {
            System.out.println("제목 : " + entry.getKey().getTitle());
            System.out.print("       ");
            for (ScreeningTime time : ReservationManager.times) {
                System.out.printf("%10s", time);
            }
            System.out.println();
            for (int i = 0; i < entry.getValue().length; i++) {
                System.out.printf("%s   ", theaters[i]);                    // 상영관 출력
                for (int j = 0; j < entry.getValue()[0].length; j++) {
                    int emptySeats = entry.getValue()[i][j].getEmpty();     //빈좌석
                    int totalSeats = entry.getValue()[i][j].getTotal();     //총좌석
                    String seatInfo = emptySeats + "/" + totalSeats;
                    scheduleNumbersMap.put(scheduleNumber, new MovieScreaningInfo(entry.getKey(), i, j));   //schedule 넘버
                    System.out.printf("[%d] %5s |", scheduleNumber++, seatInfo);
                }
                System.out.println();
                System.out.print("       ");
                for (int j = 0; j < times.length; j++) {
                    System.out.print("----------");
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    //좌석 조회하기
    public static void getSeats(Seat[][] seats) {
        // 좌석 맵 출력
        System.out.println("============= 좌석 정보 ============");
        System.out.println("============= SCREEN =============");
        System.out.print("   ");

        // 열 번호 출력
        for (int j = 0; j < seats[0].length; j++) {
            System.out.printf("%-6s", "  " + (j + 1));
        }
        System.out.println();


        for (int i = 0; i < seats.length; i++) {
            System.out.print(((char) (i + 65)) + " "); // 행 알파벳 출력
            for (int j = 0; j < seats[0].length; j++) {
                if (seats[i][j] == null) {
                    System.out.printf("%-6s", "[ " + ((char) (i + 65)) + "" + (j + 1) + " ]");
                } else {
                    System.out.printf("%-6s", "[ X ]"); // 예약된 좌석
                }
            }
            System.out.println();
        }
        System.out.println("=================================");
        System.out.println("=================================");
    }

    //사용자가 입력한 좌석 번호(알파벳 + 숫자 조합)를 행, 열 정수형으로 변환
    public static Integer[] convertSelectSeat (String selectSeat) {
        selectSeat = selectSeat.trim().toUpperCase();

        // 알파벳과 숫자 분리
        String rowStr = ""; // 알파벳
        String colStr = ""; // 숫자
        for (char c : selectSeat.toCharArray()) {
            if (Character.isLetter(c)) {
                rowStr += c;
            } else if (Character.isDigit(c)) {
                colStr += c;
            }
        }
        Integer row = rowStr.charAt(0) - 'A';
        Integer col = Integer.parseInt(colStr) - 1;
        return new Integer[] {row, col};
    }

    //예매번호 생성기 (16자리)
    public static String ticketNumberGenerator(String selectedDate, Integer row, Integer col, String selectSeat) {
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
        sb.append(row).append(col).append(selectSeat).append("-");

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
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //2) 예매 조회하기
    public static void getReservation(User user) {
        Scanner sc = new Scanner(System.in);
        List<User> usersList = FileDataManager.readUserInfoFromFile(); // User.txt

        Optional<User> foundUserOptional  = usersList.stream()
                                            .filter(findUser -> findUser.getEmail().equals(user.getEmail()))
                                            .findFirst();

        Client client = (Client) foundUserOptional.orElse(null);
        List<Reservation> userReservationsList = client.getReservationList();

        String menu = "";
        do {
            System.out.println("전체 예매조회 - [1]");
            System.out.println("기간별 예매조회 - [2]"); //영화를 예매한 날짜에 대해
            System.out.println("영화별 전체조회 - [3]");
            System.out.println("메뉴로 돌아가기 - [0]");
            System.out.print("메뉴를 선택해주세요 : ");
            menu = sc.nextLine();

            switch (menu) {
                case "1":   //전체 예매 조회
                    getAllReservations(userReservationsList);
                    break;
                case "2":
                    //기간별 예매조회
                    String periodMenu = "";
                    do {
                        System.out.println("최근 1개월 조회 - [1]");
                        System.out.println("지정 기간 조회 - [2]");
                        System.out.println("뒤로가기 - [-1]");
                        System.out.println("메뉴로 돌아가기 - [0]");
                        periodMenu = sc.nextLine();
                        //예외처리

                        String[] findPeriod = new String[2];    //조회 기간 [시작 날짜, 끝날짜]
                        String findDate = "";
                        //한달 전 기간 찾기
                        switch (periodMenu) {
                            case "1":
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                findPeriod[0] = LocalDate.now().minusMonths(1).format(formatter);   //현재 날짜 기준 한달전
                                findPeriod[1] = LocalDate.now().format(formatter);  //현재 날짜
                                getReservationsByPeriod(userReservationsList, findPeriod);   //해당 기간의 예매 정보 출력
                                break;
                            case "2":
                                System.out.println("시작 날짜 입력 ex) 2024-03-23");
                                findDate = sc.nextLine();
                                //날짜 예외 처리 필요 -> 예외처리 후 저장
                                findPeriod[0] = findDate;

                                System.out.println("끝 날짜 입력 ex) 2024-03-23");
                                findDate = sc.nextLine();
                                //날짜 예외 처리 필요 -> 예외처리 후 저장
                                findPeriod[1] = findDate;
                                getReservationsByPeriod(userReservationsList, findPeriod);   //해당 기간의 예매 정보 출력
                                break;
                            case "-1":
                                System.out.println("예매 조회 메뉴로 돌아갑니다.");
                                break;
                            case "0":
                                System.out.println("메인 메뉴로 돌아갑니다.");
                                menu = periodMenu;
                                break;
                            default:
                                System.out.println("잘못된 입력입니다.");
                        }
                    } while(!(periodMenu.equals("0") || periodMenu.equals("-1")));
                    break;
                //getReservationsByPeriod(reservationList);
                //기간별 조회는 최근 1개월, 사용자 기간 지정
                case "3":
                    //영화의 예매조회
                    String movieTitleToFilter = "";
                    System.out.printf("영화 제목 입력 ex) 서울의봄 : ");
                    movieTitleToFilter = sc.nextLine();
                    getReservationsForMovie(userReservationsList, movieTitleToFilter);
                    break;
                case "0":
                    System.out.println("메뉴로 돌아갑니다.\n");
                    return;
                default:
                    System.out.println("올바른 입력이 아닙니다.\n");
            }
        } while (!menu.equals("0"));
    }

    //전체 예매 조회
    private static void getAllReservations(List<Reservation> reservationList) {
        System.out.println("==================전체 예매 조회====================");
        for (Reservation reservation : reservationList) {
            System.out.println(reservation);
        }
        System.out.println("=================================================");
        System.out.println();
    }

    //기간 별 예매 조회(예매 날짜 기준)
    private static void getReservationsByPeriod(List<Reservation> reservationList, String[] findPeriod) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(findPeriod[0], formatter);
        LocalDate endDate = LocalDate.parse(findPeriod[1], formatter);

        List<Reservation> filteredReservationList = reservationList.stream()
                .filter(reservation -> {
                    LocalDate reservationDate = reservation.getReservationDate().toLocalDate();
                    return (reservationDate.isEqual(startDate) || reservationDate.equals(endDate) ||
                            (reservationDate.isAfter(startDate) && reservationDate.isBefore(endDate)));
                })
                .toList();

        // 필터링된 예약 리스트 출력
        System.out.printf("=== 예약 목록 (%s - %s) ===\n", findPeriod[0], findPeriod[1]);
        if (filteredReservationList.isEmpty()) {
            System.out.println("해당 가긴의 예매 내역을 찾을 수 없습니다.");
        } else {
            for (Reservation reservation : filteredReservationList) {
                System.out.println(reservation);
            }
        }
        System.out.println("=================================================");
        System.out.println();
    }

    //특정 영화 예매 조회
    private static void getReservationsForMovie(List<Reservation> reservationList, String movieTitleToFilter) {
        List<Reservation> filteredReservationList = reservationList.stream()
                .filter(reservation -> reservation.getMovie().getTitle().equals(movieTitleToFilter))
                .toList();
        // 필터링된 예약 리스트 출력
        System.out.println("=== " + movieTitleToFilter + " 예약 목록 ===");
        if (filteredReservationList.isEmpty()) {
            System.out.println("해당 영화의 예매 내역을 찾을 수 없습니다.");
        } else {
            for (Reservation reservation : filteredReservationList) {
                System.out.println(reservation);
            }
        }
        System.out.println("=================================================");
        System.out.println();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //3) 예매 취소하기
    public static void deleteReservation(User user) {
        Scanner sc = new Scanner(System.in);
        Map<String, Map<Movie,Schedule[][]>> data = FileDataManager.readMovieScheduleFromFile();    // MovieSchedule.txt
        List<User> usersList = FileDataManager.readUserInfoFromFile();                              // User.txt
        List<Reservation> reservationsList = FileDataManager.readReservationsFromFile();            // reservationsList

        //현재 유저 정보 가져오기
        Optional<User> foundUserOptional  = usersList.stream()
                .filter(findUser -> findUser.getEmail().equals(user.getEmail()))
                .findFirst();

        Client client = (Client) foundUserOptional.orElse(null);

        //현재 유저의 예매 목록
        List<Reservation> userReservationsList = client.getReservationList();
        getAllReservations(userReservationsList);   //목록 보여주기

        String check = "";
        //유저에게 삭제할 예매번호 입력받기
        do {
            String inputReservationNumber = sc.nextLine();

            Optional<Reservation> foundReservationOptional = userReservationsList.stream()
                    .filter(reservation -> reservation.getReservationNumber().equals(inputReservationNumber))
                    .findFirst();

            Reservation foundReservation = foundReservationOptional.orElse(null);

            if (foundReservation == null) {
                System.out.println("잘못된 예매 번호 입니다. 다시입력하시겠습니까? (Y / N)");
                check = sc.nextLine();
                if (check.equals("Y")) continue;
                else return;
            }

            System.out.println("정말로 예매를 취소하시겠습니까? (Y / N)");
            check = sc.nextLine();
            if (check.equals("N")) return;





        } while(true);


        //유저 정보로부터 유저가 가지고 있는 예약 정보 출력

        //유저가 취소를 원하는 예매 번호를 입력받음

        // 변경이 필요한 데이터
        // 1. movieSchedule에 있는 좌석 -> null로 변경 and 빈좌석 +1 변경
        // 2. Users 가 가지고 있는 예약 지워야 ... 근데.. 이거 ... ArrayList로 괜찮은 건가... (2번이 효율 면에서 문제가 좀...)
        // 2. 유저한테 돈도 환불해줘야 함...

        // 3. Reservations에 있는 것 순회 하면서 예매번호와 일치하는 하나만 빼고 다시 덮어써야 할듯



        //예약정보 보여주고 선택받아야함 while

        //유저객체에서 예약정보 삭제
//        Client client = (Client) user;
//        List<Reservation> reservationList = client.getReservationList();

        //유저파일에서 예약정보 삭제

        //무비스케줄에서도 예약정보 삭제

        //돈 돌려주기 유저한테

        // 유저 파일 쓰기

    }
}