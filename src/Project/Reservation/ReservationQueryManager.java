package Project.Reservation;

import Project.Exception.ExitException;
import Project.FilesIO.FileDataManager;
import Project.Reservation.Reservation;
import Project.User.Client;
import Project.User.User;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReservationQueryManager {
    private static final Scanner sc = new Scanner(System.in);
    private static final String EXIT_COMMAND = "exit";
    private static String menu;

    public static void start(User user) {
        //예매 조회 출력
        printReservationQueryStat();

        //파일 데이터 불러오기
        List<User> usersList = FileDataManager.readUserInfoFromFile();

        //유저 정보 최신화 (예왜처리 불필요)
        Client client = (Client) getUserInfo(user, usersList);

        //유저의 예약 목록 리스트
        List<Reservation> userReservationsList = client.getReservationList();

        try {
            do {
                //예매 조회 메뉴 출력
                printReservationQueryMenu();

                //예매 조회 메뉴 선택
                selectMenu();

                switch (menu) {
                    case "1":   //전체 예메 목록 조회
                        printAllReservations(userReservationsList);
                        printUserNextStepMessage(1);
                        checkInputMenu();
                        break;
                    case "2":   //기간별 예매 목록 조회
                        findReservationPeriodReservations(userReservationsList);
                        break;
                    case "3":   //특정 영화 예매 목록 조회
                        findMovieReservations(userReservationsList);
                        break;
                }

                //예매 조회
            } while (!menu.equals("exit"));
        } catch (ExitException e) {
            System.out.println(e.getMessage());
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

    //예매 조회 메뉴 선택 / 기간별 예매 조회 메뉴 선택
    private static void selectMenu() {
        do {
            printInputMessage("메뉴를 선택하세요: ");
            menu = checkInputMenu();

            if(menu.equals("1") || menu.equals("2") || menu.equals("3")) {
                return;
            } else {
                printInvalidInputMessage();
                menu = checkInputMenu();
            }
        } while (true);
    }

    //menu를 입력받아 탈출하거나 menu를 리턴
    private static String checkInputMenu() {
        menu = sc.nextLine();
        if(menu.equals(EXIT_COMMAND)) throw new ExitException();
        return menu;
    }

    //예매한 날짜로 예매 목록 조회
    private static void findReservationPeriodReservations(List<Reservation> userReservationsList) {
        do {
            printPeriodReservationQueryMenu();  //예매 조회 메뉴 출력
            selectMenu();                       //예매 조회 메뉴 선택

            switch (menu) {
                case "1":
                    findRecentlyReservations(userReservationsList);
                    break;
                case "2":
                    findSpecificReservation(userReservationsList);
                    break;
            }
            printUserNextStepMessage(2);
            checkInputMenu();
        } while (!menu.equals("1"));
    }

    //최근 1개월 기준 예매 목록 조회
    private static void findRecentlyReservations(List<Reservation> userReservationsList) {
        String[] findPeriod = new String[2];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        findPeriod[0] = LocalDate.now().minusMonths(1).format(formatter);   //현재 날짜 기준 한달전
        findPeriod[1] = LocalDate.now().format(formatter);  //현재 날짜
        getReservationsByPeriod(userReservationsList, findPeriod);   //해당 기간의 예매 정보 출력
    }

    private static void findSpecificReservation(List<Reservation> userReservationsList) {
        String[] findPeriod = new String[2];
        do {
            printInputMessage("시작 날짜를 입력하세요 [ex) 2024-03-23] : ");
            findPeriod[0] = sc.nextLine();
            if (!isValidationDate(findPeriod[0]) || !isLocalDateFormat(findPeriod[0])) {
                printInvalidInputMessage();
                checkInputMenu();
                continue;
            }
            break;
        } while (true);

        do {
            printInputMessage("종료 날짜를 입력하세요 [ex) 2024-03-23] : ");
            findPeriod[1] = sc.nextLine();
            if (!isValidationDate(findPeriod[1]) || !isLocalDateFormat(findPeriod[1])) {
                printInvalidInputMessage();
                checkInputMenu();
                continue;
            }
            break;
        } while (true);

        getReservationsByPeriod(userReservationsList, findPeriod);  //해당 기간의 예매 정보 출력
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

    //기간 별 예매 조회(예매 날짜 기준)
    private static void getReservationsByPeriod(List<Reservation> reservationList, String[] findPeriod) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if (LocalDate.parse(findPeriod[0], formatter).isAfter(LocalDate.parse(findPeriod[1], formatter))) {
            String temp = findPeriod[0];
            findPeriod[0] = findPeriod[1];
            findPeriod[1] = temp;
        }

        LocalDate startDate = LocalDate.parse(findPeriod[0], formatter);
        LocalDate endDate = LocalDate.parse(findPeriod[1], formatter);

        List<Reservation> filteredReservationList = reservationList.stream()
                .filter(reservation -> {
                    LocalDate reservationDate = reservation.getReservationDate().toLocalDate();
                    return (reservationDate.isEqual(startDate) || reservationDate.equals(endDate) ||
                            (reservationDate.isAfter(startDate) && reservationDate.isBefore(endDate)));
                })
                .toList();
        printFilteredReservation(filteredReservationList, findPeriod);  // 필터링된 예약 리스트 출력
    }

    //특정 영화 예매 목록 조회
    private static void findMovieReservations(List<Reservation> userReservationsList) {
        do {
            printInputMessage("찾으시려는 영화 제목을 입력해주세요 [ex) 서울의봄] : ");
            String movieTitleToFilter = sc.nextLine();
            getReservationsForMovie(userReservationsList, movieTitleToFilter);
            printUserNextStepMessage(2);
            menu = checkInputMenu();
        } while (!menu.equals("1"));
    }

    //특정 영화 예매 목록 필터링 조회
    private static void getReservationsForMovie(List<Reservation> reservationList, String movieTitleToFilter) {
        List<Reservation> filteredReservationList = reservationList.stream()
                .filter(reservation -> reservation.getMovie().getTitle().equals(movieTitleToFilter))
                .toList();

        printFilteredReservation(filteredReservationList, movieTitleToFilter);  // 필터링된 예약 리스트 출력
    }


    //////////////////////////////   OutputPrint   //////////////////////////////////////
    public static void printInputMessage(String message) {
        System.out.print(message);
    }

    public static void printInvalidInputMessage() {
        System.out.println();
        System.out.println("잘못된 입력입니다. [1] 다시 입력 / [exit] 메뉴로 돌아가기");
    }

    public static void printReservationQueryStat() {
        System.out.println("===============================================");
        System.out.println("==================  예매 조회  ==================");
        System.out.println("===============================================");
        System.out.println();
    }

    public static void printReservationQueryMenu() {
        System.out.println();
        System.out.println("[1] 전체 예매 조회");
        System.out.println("[2] 기간별 예매 조회"); // 영화를 예매한 날짜 기준
        System.out.println("[3] 영화별 전체 조회");
        System.out.println("[exit] 메뉴로 돌아가기");
    }

    public static void printPeriodReservationQueryMenu() {
        System.out.println();
        System.out.println("[1] 최근 1개월 조회");
        System.out.println("[2] 특정 기간별 조회"); // 영화를 예매한 날짜 기준
        System.out.println("[3] 예매 조회 메뉴로 돌아가기");
        System.out.println("[exit] 메뉴로 돌아가기");
    }

    public static void printUserNextStepMessage(int choice) {
        System.out.println();
        switch (choice) {
            case 1:
                System.out.println("[1] 예매 조회 메뉴로 돌아가기 / [exit] 메뉴로 돌아가기");
                break;
            case 2:
                System.out.println("[0] 다시 조회 하기 /[1] 예매 조회 메뉴로 돌아가기 / [exit] 메뉴로 돌아가기");
                break;
        }
    }

    //전체 예매 목록 출력
    private static void printAllReservations(List<Reservation> reservationList) {
        System.out.println();
        System.out.println("=========================================  전체 예매 조회  =========================================");
        for (Reservation reservation : reservationList) {
            System.out.println(reservation);
        }
        System.out.println("==================================================================================================");
    }

    // 영화로 필터링된 예매 목록 출력
    public static void printFilteredReservation(List<Reservation> filteredReservationList, String movieTitleToFilter) {
        System.out.println();
        System.out.printf("============================= 예약 목록 : [ %s ]=============================\n", movieTitleToFilter);
        if (filteredReservationList.isEmpty()) {
            System.out.println("해당 영화의 예매 내역을 찾을 수 없습니다.");
        } else {
            for (Reservation reservation : filteredReservationList) {
                System.out.println(reservation);
            }
        }
        System.out.println("==================================================================================================");
    }

    // 기간별로 필터링된 예매 목록 출력
    public static void printFilteredReservation(List<Reservation> filteredReservationList, String[] findPeriod) {
        System.out.println();
        System.out.printf("============================= 예약 목록 [ %s - %s ] =============================\n", findPeriod[0], findPeriod[1]);
        if (filteredReservationList.isEmpty()) {
            System.out.println("해당 영화의 예매 내역을 찾을 수 없습니다.");
        } else {
            for (Reservation reservation : filteredReservationList) {
                System.out.println(reservation);
            }
        }
        System.out.println("==================================================================================================");
    }


}
