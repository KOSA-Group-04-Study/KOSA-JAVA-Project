package Project.Manager;

import Project.*;
import Project.User.Client;
import Project.User.User;

import java.util.*;

public class ReservationManager {

    public static void makeMovieReservation(User user) {
        Map<String, Map<Movie,Schedule[][]>> data = tempMovieShedule();
        user = tempUser();
        Map<Integer, MovieScreaningInfo> scheduleNumbersMap = new HashMap<Integer, MovieScreaningInfo>();   //Schedule에 numbering

        // 상영관 및 시간대 정보
        String[] theaters = {"1관", "2관", "3관"};
        ScreeningTime[] times = {ScreeningTime.time_09, ScreeningTime.time_12, ScreeningTime.time_18};

        Scanner sc = new Scanner(System.in);

        do {
            // 1. 사용자에게 영화 상영 날짜를 입력받는다.
            System.out.println("상영할 영화 날짜를 입력해 주세요");
            String selectedDate = sc.nextLine();

            Map<Movie, Schedule[][]> movieMap = data.get(selectedDate); //선택한 날짜의 영화상영정보
            System.out.println("선택하신 날짜 : "+selectedDate);
            System.out.println("------------------------------------");

            // 2. 입력받은 날짜의 상영 정보를 보여준다.
            System.out.println("=====영화 상영 일정====");
            Integer scheduleNumber = 1;
            for (Map.Entry<Movie, Schedule[][]> entry : movieMap.entrySet()) {
                System.out.println(entry.getValue());
                System.out.println("제목 : " + entry.getKey().getTitle());
                System.out.print("       ");
                for (ScreeningTime time : times) {
                    System.out.printf("%10s", time);
                }
                System.out.println();
                for (int i = 0; i < entry.getValue().length; i++) {
                    System.out.printf("%s   ", theaters[i]);                    // 상영관 출력
                    for (int j = 0; j < entry.getValue()[0].length; j++) {
                        int emptySeats = entry.getValue()[i][j].getEmpty();    //빈좌석
                        int totalSeats = entry.getValue()[i][j].getTotal();    //총좌석
                        String seatInfo = emptySeats + "/" + totalSeats;
                        scheduleNumbersMap.put(scheduleNumber++, new MovieScreaningInfo(entry.getKey(), i, j));   //schedule 넘버
                        System.out.printf("[%d] %5s |", scheduleNumber, seatInfo);
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

            // 원하는 스케쥴 입력받기
            System.out.println("상영관을 선택해 주세요.");
            Integer selectedSchedule = Integer.parseInt(sc.nextLine());

            Movie selectedMoive = scheduleNumbersMap.get(selectedSchedule).getMovie();
            Integer row = scheduleNumbersMap.get(selectedSchedule).getI();
            Integer col = scheduleNumbersMap.get(selectedSchedule).getJ();
            System.out.println(selectedMoive.getTitle());
            System.out.println(row);
            System.out.println(col);
            System.out.println(movieMap.get(selectedMoive));
            Seat[][] seats = movieMap.get(selectedMoive)[row][col].getSeats();
            System.out.println("영화 : " + selectedMoive);
            System.out.println("상영관 : " + theaters[row]);
            System.out.println("상영시간 : " + times[col]);

            //해당 상영관의 좌석을 보여주기
            getSeats(seats);

            //사용자에게 좌석을 입력받기
            System.out.println("좌석을 선택하세요. (예: A1 또는 a1");
            String selectSeat = sc.nextLine();
            selectSeat(seats, selectSeat);


            System.out.println("예매가 완료되었습니다.");
            System.out.println("다시 예매하시겠습니까? (Y/N)");
            if (!sc.nextLine().equals("Y")) break;
        } while (true);


        Map<String, Map<Movie, Schedule[][]>> stringMapMap = FileDataManager.readMovieScheduleFromFile();
//        while (true) {
//            MovieScheduleManager.getMovieSchedule(); // 보여주기
//            // 볼 시간대 입력 받고
//        }
//
//        while (true) {
//            // 그 시간대의 좌석을 뿌리고
//            // 좌석 입력받기
//        }

        // 좌석을 선택받고

        // Reservation 객체를 생성.. 예매번호는 알아서   + user(client) 가서 List 에 추가해주기
        // 날짜포맷 "2022-02-02"


        // Seat[][]에 seat 를 채우고  movieSchedule 을 파일 덮어쓰기

        // 결제를 하고 ... 성공시 그냥 진행, 실패시 -> 멘트 + return


        //user 정보 파일저장은 끝에서 한번
        //무비스케줄 파일저장은 끝에서 한번
        // Reservation 파일에 쓰기 파일저장은 끝에서 한번
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

    public static void selectSeat(Seat[][] seats, String selectSeat) {
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

        seats[row][col] = new Seat(row, col);
    }


    public static void getReservation(User user) {
        Client client = (Client) user;
        List<Reservation> reservationList = client.getReservationList();
        //알아서 출력.
    }

    public static void deleteReservation(User user) {
        //예약정보 보여주고 선택받아야함 while

        //유저객체에서 예약정보 삭제
        Client client = (Client) user;
        List<Reservation> reservationList = client.getReservationList();

        //유저파일에서 예약정보 삭제

        //무비스케줄에서도 예약정보 삭제

        //돈 돌려주기 유저한테

        // 유저 파일 쓰기

    }


    public static Map tempMovieShedule() {  //임시 MovieShedule.txt 정보
        Map<String, Map<Movie,Schedule[][]>> data = new HashMap<>();

        Map<Movie, Schedule[][]> data1 = new HashMap<>();

        Movie movie1 = new Movie("파묘", 60, 15000);
        Movie movie2 = new Movie("엘리멘탈", 60, 25000);
        Movie movie3 = new Movie("윙카", 60, 35000);
        Movie movie4 = new Movie("서울의봄", 60, 45000);


        Schedule[][] schedules = new Schedule[3][3];
        for (int i = 0; i < 3; i++) {
            schedules[i] = new Schedule[3];
            for (int j = 0; j < 3; j++) {
                schedules[i][j] = new Schedule(5, 5);
                //schedules[i][j] = new Schedule(new Seat[5][5]);
            }
        }
        Schedule[][] schedules2 = new Schedule[3][3];
        for (int i = 0; i < 3; i++) {
            schedules2[i] = new Schedule[3];
            for (int j = 0; j < 3; j++) {
                schedules2[i][j] = new Schedule(5, 5);
                //schedules2[i][j] = new Schedule(new Seat[5][5]);
            }
        }
        Schedule[][] schedules3 = new Schedule[3][3];
        for (int i = 0; i < 3; i++) {
            schedules3[i] = new Schedule[3];
            for (int j = 0; j < 3; j++) {
                schedules3[i][j] = new Schedule(5, 5);
                //schedules3[i][j] = new Schedule(new Seat[5][5]);
            }
        }
        Schedule[][] schedules4 = new Schedule[3][3];
        for (int i = 0; i < 3; i++) {
            schedules4[i] = new Schedule[3];
            for (int j = 0; j < 3; j++) {
                schedules4[i][j] = new Schedule(5, 5);
                //schedules4[i][j] = new Schedule(new Seat[5][5]);
            }
        }

        data1.put(movie1,schedules4);
        data1.put(movie2,schedules2);
        data1.put(movie3,schedules3);
        data1.put(movie4,schedules4);

        data.put("2024-03-23", data1);

        return data;
    }

    public static List tempUsers () {  //임시 Users.txt 정보
        User user1 = new Client("test1@test.com", "0000", "리신", "01012345678", false, 10000);
        User user2 = new Client("test2@test.com", "0000", "김유신", "01012345678", false, 10000);
        User user3 = new Client("test3@test.com", "0000", "이순신", "01012345678", false, 10000);
        List users = new ArrayList();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        return users;
    }

    public static User tempUser() {
        User user = new Client("test1@test.com", "0000", "리신", "01012345678", false, 10000);
        return user;
    }

//    public void tempReservations() {
//        Reservation reservation;
//        reservation = new Reservation();
//    }

}
