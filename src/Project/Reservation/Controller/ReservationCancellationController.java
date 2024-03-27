package Project.Reservation.Controller;

import Project.FilesIO.FileDataManager;
import Project.Movie;
import Project.Payment.PaymentManager;
import Project.Reservation.Reservation;
import Project.Schedule;
import Project.User.Client;
import Project.User.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//예매 취소하기
public class ReservationCancellationController {
    private static final Scanner sc = new Scanner(System.in);
    private static final String EXIT_COMMAND = "exit";
    private static String menu;

    public static void start(User user) {
        //예매 취소 메뉴 출력
        printReservationCancellationStat();

        //파일 데이터 불러오기
        Map<String, Map<Movie, Schedule[][]>> data = FileDataManager.readMovieScheduleFromFile();    // MovieSchedule.txt
        List<User> usersList = FileDataManager.readUserInfoFromFile();                              // User.txt
        List<Reservation> reservationsList = FileDataManager.readReservationsFromFile();

        //유저 정보 최신화 (예왜처리 불필요)
        Client client = (Client) getUserInfo(user, usersList);

        //현재 유저의 예매 목록 출력
        List<Reservation> userReservationsList = client.getReservationList();
        getAllReservations(userReservationsList);

        Reservation foundReservation = null;    //예약한 예매번호
        try {
            //사용자가 예매 번호를 입력
            do {
                String inputReservationNumber = inputScreeningNumber();                                                  //예매 번호 입력 및 유효성 검사
                foundReservation = searchReservationByReservationNumber(userReservationsList, inputReservationNumber);  //유저가 예매번호를 갖고 있는지 검증
                if (foundReservation == null) {
                    printNullInputMessage();
                    continue;
                }
                if (!isReservationCancellable(foundReservation)) continue;  //예매 취소 가능 여부
                break;
            } while (true);

            //예매 취소 최종 확인
            confirmReservationCancellation(foundReservation);

            //변경된 파일 저장
            writeTextData(foundReservation, data);              //MovieSchedule.txt
            writeTextData(foundReservation, reservationsList);  //Reservations.txt
            writeTextData(foundReservation, usersList, client); //User.txt


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

    //movieSchedule.txt 저장
    private static void writeTextData(Reservation foundReservation, Map<String, Map<Movie, Schedule[][]>> data) {
        String selectedDate = foundReservation.getMovieDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Movie choiceMovie = foundReservation.getMovie();        //선택한 영화
        Integer I = foundReservation.getTheater()[0];           //상영관 번호
        Integer J = foundReservation.getTheater()[1];           //상영 시각
        Integer seatI = foundReservation.getSeat().getRow();    //좌석 행
        Integer seatJ = foundReservation.getSeat().getCol();    //좌석 열

        for (Map.Entry<Movie, Schedule[][]> entry : data.get(selectedDate).entrySet()) {
            if (entry.getKey().getTitle().equals(choiceMovie.getTitle())) {
                Schedule choiceSchedule = entry.getValue()[I][J];
                choiceSchedule.seatCountUp();                       //빈좌석 + 1
                choiceSchedule.getSeats()[seatI][seatJ] = null;     //좌석 null로 변경
                break;
            }
        }
        FileDataManager.writeMovieScheduleToFile(data);
    }

    //Reservation.txt 저장
    private static void writeTextData(Reservation foundReservation, List<Reservation> reservationsList) {
        reservationsList.remove(foundReservation);
        FileDataManager.writeReservationToFile(reservationsList);
    }

    //User.txt 저장
    private static void writeTextData (Reservation foundReservation, List<User> usersList, Client client) {
        Integer refundMoviePrice = foundReservation.getMoviePrice();
        PaymentManager.payBack(client, refundMoviePrice);
        System.out.printf("[%d] 포인트가 반환되었습니다.\n", refundMoviePrice);
        System.out.printf("현재 포인트는 [%d] 입니다\n", client.getPoint());

        client.getReservationList().remove(foundReservation);
        FileDataManager.writeUserInfoToFile(usersList);
    }

    //예매 번호가 존재하는지 확인 후 예매내역 리턴
    private static Reservation searchReservationByReservationNumber(List<Reservation> userReservationsList, String inputReservationNumber) {
        Optional<Reservation> foundReservationOptional = userReservationsList.stream()
                .filter(reservation -> reservation.getReservationNumber().equals(inputReservationNumber))
                .findFirst();
        return foundReservationOptional.orElse(null);
    }

    //해당 예매의 취소 가능 여부 판단
    private static boolean isReservationCancellable(Reservation foundReservation) {
        String inputData = "";
        if (foundReservation.getMovieDate().isBefore(LocalDate.now())) {
            //예외 처리 - 현재 날짜 이전 날짜에 상영한 영화의 경우
            printExpiredReservationWarning();
            inputData = sc.nextLine();
            if (inputData.equals(EXIT_COMMAND)) throw new ExitException();
            return false;
        } else if (foundReservation.getMovieDate().isEqual(LocalDate.now())) {
            //예외 처리 - 현재 시간 이전 시간에 상영한 영화의 경우
            LocalDateTime[] screeningTime = {
                    LocalDateTime.of(foundReservation.getMovieDate(), LocalTime.of(9, 0)),
                    LocalDateTime.of(foundReservation.getMovieDate(), LocalTime.of(12, 0)),
                    LocalDateTime.of(foundReservation.getMovieDate(), LocalTime.of(18, 0))
            };

            if (screeningTime[foundReservation.getTheater()[1]].isBefore(LocalDateTime.now())) {
                printExpiredReservationWarning();
                inputData = sc.nextLine();
                if (inputData.equals(EXIT_COMMAND)) throw new ExitException();
                return false;
            }
        }
        return true;
    }

    //예매 번호 입력 및 유효성 검사
    private static String inputScreeningNumber() {
        do {
            printInputMessage("예매 취소를 위해 예매번호를 입력해주세요: ");
            String inputReservationNumber = sc.nextLine();

            //탈출 커맨드 입력 시 탈출예외 던지기
            if(inputReservationNumber.equals(EXIT_COMMAND)) throw new ExitException();

            if (validationReservationNumber(inputReservationNumber)) {
                //유효한 예매 번호인 경우,
                return inputReservationNumber;
            } else {
                //유효한 예매 번호가 아닌 경우
                printInvalidInputMessage();
                menu = sc.nextLine();
            }
        }while(!menu.equals("exit"));
        return null;
    }

    //예매번호 유효성 검사
    public static boolean validationReservationNumber(String input) {
        String regexPattern = "^[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}-[A-Z0-9]{4}$";
        Pattern pattern = Pattern.compile(regexPattern);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    //예매 취소 확인
    private static void confirmReservationCancellation(Reservation foundReservation) {
        printConfirmReservationMessage(foundReservation);
        menu = sc.nextLine();
        if(menu.equals(EXIT_COMMAND)) throw new ExitException();
    }


    //////////////////////////////   OutputPrint   //////////////////////////////////////
    public static void printInputMessage(String message) {
        System.out.print(message);
    }

    public static void printReservationCancellationStat() {
        System.out.println("===============================================");
        System.out.println("==================  예매 취소  ==================");
        System.out.println("===============================================");
        System.out.println();
    }

    public static void getAllReservations(List<Reservation> reservationList) {
        System.out.println("==================전체 예매 조회====================");
        for (Reservation reservation : reservationList) {
            System.out.println(reservation);
        }
        System.out.println("=================================================");
        System.out.println();
    }

    public static void printInvalidInputMessage() {
        System.out.println();
        System.out.println("잘못된 입력입니다. [1] 다시 입력 / [exit] 메뉴로 돌아가기");
    }

    public static void printNullInputMessage() {
        System.out.println();
        System.out.println("유효하지 않은 입력입니다. [1] 다시 입력 / [exit] 메뉴로 돌아가기");
    }

    public static void printExpiredReservationWarning() {
        System.out.println();
        System.out.println("기간이 지난 예매내역으로 취소하실 수 없습니다. [1] 다시 입력 / [exit] 메뉴로 돌아가기");
    }

    public static void printConfirmReservationMessage(Reservation foundReservation) {
        System.out.println();
        System.out.println(foundReservation);
        System.out.println("정말로 예매를 취소하시겠습니까? [1] 예매 취소 / [exit] 메뉴로 돌아가기");
    }


    ////////////////////////////////////  Exception   ////////////////////////////////////////////
    //사용자 정의 예외
    private static class ExitException extends RuntimeException {
        public ExitException() {
            super("탈출메시지가 입력되었습니다.");
        }
    }

}