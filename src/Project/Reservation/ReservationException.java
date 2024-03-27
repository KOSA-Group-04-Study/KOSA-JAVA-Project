package Project.Reservation;

import Project.Movie;
import Project.Schedule;
import Project.Seat;

import java.util.Map;
import java.util.Scanner;


 public final class ReservationException {
    //예매하기 예외
    static Scanner sc = new Scanner(System.in);

    // 1.1
    // 사용자에게 영화 상영 날짜를 입력받고 입력 잘못되면 다시 입력할 여부에 대해 물어서 검증을 도와준 뒤 맞게 입력하면 날짜 리턴
    public static String DataInputFormat () {
        String dataFormat = "^\\d{4}-\\d{2}-\\d{2}$";

        while(true) {
            System.out.println("상영할 영화 날짜를 입력해 주세요 ex) 2024-03-23");
            String selectedDate = sc.nextLine();

            if (!selectedDate.matches(dataFormat)) {
                System.out.println("잘못된 형식 입니다.");
                if (!askForRetry()) {
                    return null;
                    // 안하겠다고 하면 null값 리턴 받아서 처리 필요
                }
                continue;
            }
            return selectedDate;
        }
    }
    
    // 1.2
    // 입력 날짜에 스케쥴 존재 여부 검증 
    public static void checkScheduleExistence(String selectDate,  Map<String, Map<Movie,Schedule[][]>> data) {
        if ( data.get(selectDate) == null) {
            System.out.println("해당 날짜에는 상영 정보가 없습니다.");
        }
    }


    // 3
    // 올바른 입력 확인
    // 매니저에서 try catch 보다
    public static Integer choiceSchedule( Map<Integer, MovieScreaningInfo> scheduleNumbersMap) {
        while(true) {
            System.out.println("원하는 영화의 스케쥴을 선택해 주세요. (ex) [38] 선택시 : 38)");
            Integer result = null;
            try {
                int choiceSchedule = Integer.parseInt(sc.nextLine());
                result = (Integer) choiceSchedule;
                boolean check = checkInputScheduleExistence(choiceSchedule, scheduleNumbersMap);
                if (check) {
                    return result;
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자만 입력받습니다.");
            }

            // null 값 받으면 처리하는 로직은 매니저에서 해결 필요

            boolean retry = askForRetry(); // 재시도 여부 확인
            if (!retry) {
                return null; // 재시도를 하지 않는 경우
            }
        }
    }

    // 3-1
    // 스케쥴 존재 확인
    private static boolean checkInputScheduleExistence(int choiceSchedule, Map<Integer, MovieScreaningInfo> scheduleNumbersMap) {
        if (scheduleNumbersMap.containsKey(choiceSchedule)) {
            return true;
        } else {
            System.out.println("해당 번호에는 배치된 영화가 없습니다.");
            return false;
        }
    }

    //4.2 좌석 번호 선택 이력 검증 로직(a1 또는 A1 형식으로 받도록)
    public static String validateSeatSelection() {
        // 입력된 좌석 정보가 올바른 형식인지 확인
        while(true) {
            String seatPattern = "[A-Za-z][0-9]+"; // 알파벳 + 숫자 형식으로 구성되어야 함
            System.out.println("좌석을 선택하세요. (예: A1 또는 a1)");
            String selectSeat = sc.nextLine();
            if (!selectSeat.matches(seatPattern)) {
                System.out.println("올바른 입력값이 아닙니다.");
                // 다시 물어봐서 안한다고 하면 null 값 리턴
                boolean check = askForRetry();
                if (check == false) {
                    return null;
                }
            } else {
                return selectSeat;
            }
        }
    }

    // 4.3 예매 좌석 검증 null 이어야 예약가능
    public static boolean checkEmptySeat(Integer[] seatNumber,Map<Integer, MovieScreaningInfo> scheduleNumbersMap, Integer choiceSchedule) {
        MovieScreaningInfo movieScreaningInfo = scheduleNumbersMap.get(choiceSchedule);

            Seat[][] seats = movieScreaningInfo.getSchedule().getSeats();
            int row = seatNumber[0];
            int col = seatNumber[1];

            if( seats[row][col] == null) {
                return true;
            } else if(seats[row][col] != null) {
                System.out.println("해당 좌석은 이미 예매되었거나 존재하지 않습니다. ");
            }
        return false; // 예매 불가능
    }


    // 입력 다시 받기 여부 묻기
    private static boolean askForRetry() {
        System.out.println("다시 입력하시려면 1을, 메뉴로 돌아가시려면 0을 입력해주세요.");
        while(true) {
            String answer = sc.nextLine();
            if (answer.equals("1")) {
                return true;
            }
            else if (answer.equals("0")) {
                return false;
            }
            else {
                System.out.println("1 또는 0만 입력해주세요.");
            }
        }
    }
}
