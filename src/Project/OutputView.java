package Project;

import Project.FilesIO.TestDataGenerator;
import Project.MovieSchedule.AdminSchedule;
import Project.Reservation.Reservation;
import Project.Reservation.ScreeningTime;
import Project.User.User;


public class OutputView {

    static final String[] THEATERS = {"1관", "2관", "3관"};

    static final ScreeningTime[] TIMES = {ScreeningTime.time_09, ScreeningTime.time_12, ScreeningTime.time_18};

    static final int TOTAL_SEAT_NUMBER = 25;
    static final String[] RAINBOW_COLORS = {
            "\u001B[31m",   // Red
            "\u001B[38;5;208m", //Orange
            "\u001B[33m",   // Yellow
            "\u001B[32m",   // Green
            "\u001B[34m",   // Blue
            "\u001B[36m",   // Cyan
            "\u001B[35m",   // Purple
    };
    static final String RESET_COLOR = "\u001B[0m"; // 리셋 ANSI 이스케이프 시퀀스 (색상을 원래대로 되돌림)

    // 사용자 메뉴
    public static void printUserMenu() {
        printMenuMessage("""
                메뉴를 입력하세요.
                1-> 예매하기  2-> 예매조회  3-> 예매취소  4-> 포인트 관리
                """);
    }
    //관리자 메뉴
    public static void printAdminMenu() {
        printMenuMessage("""
                메뉴를 입력하세요.
                1-> 영화상영등록    2-> 영화상영종료     3-> 회원정보조회
                """);
    }

    // ASCII 아트를 이용한 상자 출력 메서드
    public static void printBox(String content) {
        for (int i = 0; i < 25; i++) {
            System.out.println();
        }
        String boxTop = "\u001B[33m"+"  ╔════════════════════━━━─── • ───━━━════════════════════╗"+ RESET_COLOR;

        String cinema =   "\u001B[38;5;208m"+ "       \t\t\t\t\t  Cinema " + RESET_COLOR +"\uD83C\uDFAC\n";

        String boxBottom = "\u001B[33m"+"  ╚════════════════════━━━─── • ───━━━════════════════════╝"+ RESET_COLOR;

        String[] lines = content.split("\n");

        System.out.println(boxTop);
        System.out.println(cinema);

        for (String line : lines) {
            // 중앙 정렬을 위해 상자의 가로 길이를 기준으로 문자열을 출력
            int paddingLength = (60 - line.length()) / 2; // 60자 가정
            String leftPadding = " ".repeat(paddingLength);
//            String rightPadding = " ".repeat(60 - line.length() - paddingLength);
            String rightPadding = " ".repeat(60 - line.length() - leftPadding.length());

            System.out.println(leftPadding + line + rightPadding);
        }
        System.out.println();

        System.out.println(boxBottom);

        for (int i = 0; i < 15; i++) {
            System.out.println();
        }

        try { //결과를 볼 시간 1초 대기
            Thread.sleep(2500); // 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void printBoxNotJump(String content) {
        String boxTop = "\u001B[33m"+"  ╔════════════════════━━━─── • ───━━━════════════════════╗"+ RESET_COLOR;

        String cinema =   "\u001B[38;5;208m"+ "       \t\t\t\t\t  Cinema " + RESET_COLOR +"\uD83C\uDFAC\n";

        String boxBottom = "\u001B[33m"+"  ╚════════════════════━━━─── • ───━━━════════════════════╝"+ RESET_COLOR;

        String[] lines = content.split("\n");

        System.out.println(boxTop);
        System.out.println(cinema);

        for (String line : lines) {
            // 중앙 정렬을 위해 상자의 가로 길이를 기준으로 문자열을 출력
            int paddingLength = (60 - line.length()) / 2; // 60자 가정
            String leftPadding = " ".repeat(paddingLength);
//            String rightPadding = " ".repeat(60 - line.length() - paddingLength);
            String rightPadding = " ".repeat(60 - line.length() - leftPadding.length());

            System.out.println(leftPadding + line + rightPadding);
        }
        System.out.println();

        System.out.println(boxBottom);
    }

    public static void printMovieScheduleForClient(String content) {
        for (int i = 0; i < 25; i++) {
            System.out.println();
        }
        String boxTop = "\u001B[33m"+"  ╔════════════════════━━━─── • ───━━━════════════════════╗"+ RESET_COLOR;

        String cinema =   "\u001B[38;5;208m"+ "       \t\t\t\t\t  Cinema " + RESET_COLOR +"\uD83C\uDFAC\n";

        String boxBottom = "\u001B[33m"+"  ╚════════════════════━━━─── • ───━━━════════════════════╝"+ RESET_COLOR;

        String[] lines = content.split("\n");

        System.out.println(boxTop);
        System.out.println(cinema);

//        for (String line : lines) {
//            // 중앙 정렬을 위해 상자의 가로 길이를 기준으로 문자열을 출력
//            int paddingLength = (60 - line.length()) / 2; // 60자 가정
//            String leftPadding = " ".repeat(paddingLength);
////            String rightPadding = " ".repeat(60 - line.length() - paddingLength);
//            String rightPadding = " ".repeat(60 - line.length() - leftPadding.length());
//
//            System.out.println(leftPadding + line + rightPadding);
//        }
        for (String line : lines) {
            System.out.println("\t\t"+line);
        }
        System.out.println();

        System.out.println(boxBottom);

//        for (int i = 0; i < 15; i++) {
//            System.out.println();
//        }
    }


    public static void printInputMessage(String content) {
        for (int i = 0; i < 25; i++) {
            System.out.println();
        }
        String boxTop = "\u001B[33m"+"  ╔════════════════════━━━─── • ───━━━════════════════════╗"+ RESET_COLOR;

        String cinema =   "\u001B[38;5;208m"+ "       \t\t\t\t\t  Cinema " + RESET_COLOR +"\uD83C\uDFAC\n";

        String boxBottom = "\u001B[33m"+"  ╚════════════════════━━━─── • ───━━━════════════════════╝"+ RESET_COLOR;

        String exitMessage = "\u001B[31m"+"     \t\t\t\t  메뉴로 가기 -> exit "+ RESET_COLOR;

        String[] lines = content.split("\n");

        System.out.println(boxTop);
        System.out.println(cinema);
//        System.out.println("[ "+functionName+" ]");

        for (String line : lines) {
            // 중앙 정렬을 위해 상자의 가로 길이를 기준으로 문자열을 출력
            int paddingLength = (60 - line.length()) / 2; // 60자 가정
            String leftPadding = " ".repeat(paddingLength);
//            String rightPadding = " ".repeat(60 - line.length() - paddingLength);
            String rightPadding = " ".repeat(60 - line.length() - leftPadding.length());

            System.out.println(leftPadding + line + rightPadding);
        }
        System.out.println("\n"+exitMessage);
        System.out.println(boxBottom);
        for (int i = 0; i < 15; i++) {
            System.out.println();
        }

    }

    public static void printInputMessageNotJump(String content) {
        String boxTop = "\u001B[33m"+"  ╔════════════════════━━━─── • ───━━━════════════════════╗"+ RESET_COLOR;

        String cinema =   "\u001B[38;5;208m"+ "       \t\t\t\t\t  Cinema " + RESET_COLOR +"\uD83C\uDFAC\n";

        String boxBottom = "\u001B[33m"+"  ╚════════════════════━━━─── • ───━━━════════════════════╝"+ RESET_COLOR;

        String exitMessage = "\u001B[31m"+"     \t\t\t\t  메뉴로 가기 -> exit "+ RESET_COLOR;

        String[] lines = content.split("\n");

        System.out.println(boxTop);
        System.out.println(cinema);
//        System.out.println("[ "+functionName+" ]");

        for (String line : lines) {
            // 중앙 정렬을 위해 상자의 가로 길이를 기준으로 문자열을 출력
            int paddingLength = (60 - line.length()) / 2; // 60자 가정
            String leftPadding = " ".repeat(paddingLength);
//            String rightPadding = " ".repeat(60 - line.length() - paddingLength);
            String rightPadding = " ".repeat(60 - line.length() - leftPadding.length());

            System.out.println(leftPadding + line + rightPadding);
        }
        System.out.println("\n"+exitMessage);
        System.out.println(boxBottom);
    }

    public static void printMenuMessage(String content) {
        for (int i = 0; i < 25; i++) {
            System.out.println();
        }
        String boxTop = "\u001B[33m"+"  ╔════════════════════━━━─── • ───━━━════════════════════╗"+ RESET_COLOR;

        String cinema =   "\u001B[38;5;208m"+ "       \t\t\t\t\t  Cinema " + RESET_COLOR +"\uD83C\uDFAC\n";

        String boxBottom = "\u001B[33m"+"  ╚════════════════════━━━─── • ───━━━════════════════════╝"+ RESET_COLOR;

        String exitMessage = "\u001B[31m"+"     \t\t\t\t  시스템 종료 -> exit "+ RESET_COLOR;

        String[] lines = content.split("\n");

        System.out.println(boxTop);
        System.out.println(cinema);

        for (String line : lines) {
            // 중앙 정렬을 위해 상자의 가로 길이를 기준으로 문자열을 출력
            int paddingLength = (60 - line.length()) / 2; // 60자 가정
            String leftPadding = " ".repeat(paddingLength);
//            String rightPadding = " ".repeat(60 - line.length() - paddingLength);
            String rightPadding = " ".repeat(60 - line.length() - leftPadding.length());

            System.out.println(leftPadding + line + rightPadding);
        }
        System.out.println("\n"+exitMessage);
        System.out.println(boxBottom);
        for (int i = 0; i < 15; i++) {
            System.out.println();
        }

    }

    public static void printExceptionMessage(String content) {
        String[] lines = content.split("\n");
        String errorImage = "\t"+"⚠\uFE0F"+"\t";
        for (String line : lines) {
            // 빨간색 추가하여 출력
            System.out.println(errorImage+"\u001B[31m" + line+ RESET_COLOR + RESET_COLOR);
        }
    }


    public static void printScheduleBox(String selectedDate, AdminSchedule[][] adminSchedules) {
        StringBuilder boxContent = new StringBuilder();
        boxContent.append("선택하신 날짜 : ").append(selectedDate);
        boxContent.append("\n───────────────────────────────────────────────\n");

        for (int i = 0; i < THEATERS.length; i++) {
            boxContent.append(String.format("%13s)\n", "(" + THEATERS[i])); // 상영관 출력
            boxContent.append("(상영 시간)  :");
            for (ScreeningTime screen_time : TIMES) { // 상영시간 출력
                boxContent.append(String.format("  %-10s", screen_time.getTime()));
            }
            boxContent.append("\n(  영화  )  : ");
            for (int j = 0; j < TIMES.length; j++) {
                if (adminSchedules[i][j] == null) { // 빈 상영시간이라면 공백을 출력
                    boxContent.append("            ");
                    continue;
                }
                AdminSchedule adminSchedule = adminSchedules[i][j];
                boxContent.append(String.format(" %-10s", adminSchedule.getMovie().getTitle())); // 영화이름 출력
            }

            boxContent.append("\n(좌석 정보)  :");
            for (int j = 0; j < TIMES.length; j++) {
                if (adminSchedules[i][j] == null) { // 빈 상영시간이라면 공백을 출력
                    boxContent.append("             ");
                    continue;
                }
                Schedule schedule = adminSchedules[i][j].getSchedule();
                boxContent.append(String.format("  %d/%d       ", schedule.getEmpty(), TOTAL_SEAT_NUMBER)); // 채워진좌석/총좌석 을 출력
            }
            boxContent.append("\n  ( 입력번호 ) :");
            for (int j = 0; j < TIMES.length; j++) {
                boxContent.append(String.format("   [%d]       ", (i * TIMES.length) + j + 1)); // 1~9 까지의 입력번호를 가진다.
            }
            boxContent.append("\n───────────────────────────────────────────────\n");
        }

        printMovieScheduleForClient(boxContent.toString());
    }





    public static void movietiketPrint(Reservation reservation) {

        System.out.println("영화 예매 완료 ....");
        System.out.println("영화 티켓 출력중 ....");

        Integer[] theater = reservation.getTheater();
        User user = reservation.getUser();
        Seat seat = reservation.getSeat();
        String col = ((char) (seat.getRow() + 65)) + "열";
        String row = (seat.getCol() + 1) + "번";

        int timeIndex = theater[0];

        String[] rectangle = {
                "                                              ",
                "  _ ____   ____   ____   ____   ____   ____ _ ",
                " | [____] [____] [____] [____] [____] [____] |",
                " | |                                       | |",
                " |_|                                       |_|",
                " | |            \uD835\uDE62\uD835\uDE64\uD835\uDE6B\uD835\uDE5E\uD835\uDE5A \uD835\uDE69\uD835\uDE5E\uD835\uDE60\uD835\uDE5A\uD835\uDE69                 | |",
                " |_|            영 화 입 장 권               |_|",
                " | |                                       | |",
                String.format(" | |    예매번호 :    %s    | |",reservation.getReservationNumber()),
                " |_|                                       |_|",
                String.format(" | |       영화 :    %-10s           | |",reservation.getMovie().getTitle()),
                " |_|                                       |_|",
                String.format(" | |    러닝타임 :    %s분                  | |",reservation.getMovie().getRunningTime()),
                " |_|                                       |_|",
                String.format(" | |       가격 :    %s원                | |",reservation.getMovie().getPrice()),
                " |_|                                       |_|",
                String.format(" | |   상영 일자 :    %s    %s    | |",reservation.getMovieDate(), TIMES[theater[1]].getTime()),
                " |_|                                       |_|",
                String.format(" | |     상영관 :    %s                     | |", THEATERS[theater[0]]),
                " |_|                                       |_|",
                String.format(" | |       좌석 :    %s  %s               | |",col,row),
                " |_|                                       |_|",
                " | |                                       | |",
                " |_|_______________________________________|_|",
                " | |                                       | |",
                " |_|     ║▌│█║▌│ █║▌│█│║▌║║▌│█║▌│ █║       |_|",
                " | |     ║▌│█║▌│ █║▌│█│║▌║║▌│█║▌│ █║       | |",
                " |_|     ║▌│█║▌│ █║▌│█│║▌║║▌│█║▌│ █║       |_|",
                " | |      A250L1  20390A200B 3440P2        | |",
                " |_|                                       |_|",
                " | |____   ____   ____   ____   ____   ____| |",
                " |_[____] [____] [____] [____] [____] [____]_|"

        };

        for (String s : rectangle) {
            System.out.println("  "+s);
            try {
                Thread.sleep(200); // 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try { //결과를 볼 시간 1초 대기
            Thread.sleep(2900); // 대기
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void firstMenu() {
        String[] firstMenu = {
            "\t\t\t\t\t   \u001B[31m1. login\u001B[0m\n",
                "\n",
            "\t\t\t\t\t   \u001B[31m2. sign up\u001B[0m\n"
        };
        for (int i = 0; i < firstMenu.length; i++) { // 첫메뉴화면도 천천히 올라가게끔.
            try {
                Thread.sleep(150); // 0.005초
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print(firstMenu[i]);
        }
        for (int i = 0; i < 15; i++) { // 화면이 중간에 위치하게끔 공백추가.
            try {
                Thread.sleep(150); // 0.005초
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println();
        }
        String b = "\r         please input  ";
        String c = "\r                       ";

        for (int i = 0; i < 5; i++) { // 입력값을 지웠다 썼다하여 깜빡깜빡하게 만들게끔.
            System.out.print(b);
            try {
                Thread.sleep(350); // 0.005초
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print(c);
            try {
                Thread.sleep(350); // 0.005초
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    public static void loading() {
        String greenColor = RAINBOW_COLORS[3];
        String[] lines = {
                "██╗      ██████╗  █████╗ ██████╗ ██╗███╗   ██╗ ██████╗ ",
                "██║     ██╔═══██╗██╔══██╗██╔══██╗██║████╗  ██║██╔════╝ ",
                "██║     ██║   ██║███████║██║  ██║██║██╔██╗ ██║██║  ███╗",
                "██║     ██║   ██║██╔══██║██║  ██║██║██║╚██╗██║██║   ██║",
                "███████╗╚██████╔╝██║  ██║██████╔╝██║██║ ╚████║╚██████╔╝",
                "╚══════╝ ╚═════╝ ╚═╝  ╚═╝╚═════╝ ╚═╝╚═╝  ╚═══╝ ╚═════╝ "
        };
        // 초록색으로 각 줄을 출력
        for (String line : lines) {
            System.out.println(greenColor + line + RESET_COLOR);
        }
        System.out.println();
        //프로그레스바 100.0 까지  0.2씩 증가   -> 테스트 빨리하기위해 0.9로 증가시킴(임시)
        for (double progress = 0.0; progress < 100.2; progress += 0.2) {
            updateProgressBar(progress);
            try {
                Thread.sleep(5); // 0.005초 -> 프로그레스바 올라가는 속도조절
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
        TestDataGenerator.testDataGenerate(); // 프로그레스바 다되면 테스트 파일 쓰기

        for (int i = 0; i < 50; i++) {
            try {
                Thread.sleep(50); // 0.005초 -> 로딩로고 안보이게 올라가는 속도 조절
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println();
        }


    }

    private static void updateProgressBar(double progress) { // 콘솔지우고 다시쓰기를 반복
        int barLength = 50; // 프로그레스 바의 전체 길이
        int numChars = (int) (progress * barLength / 100.0); // 현재 진행된 비율에 따라 표시되어야 할 문자 수

        // 프로그레스 바 출력
        System.out.print("\r["); // 콘솔 지우기
        for (int i = 0; i < barLength; i++) {
            String color = RAINBOW_COLORS[i % RAINBOW_COLORS.length];
            if (i < numChars) {
                System.out.print(color + "=" + RESET_COLOR);
            } else {
                System.out.print(" ");
            }
        }
        System.out.printf("] %.2f%%", progress); // 현재 진행 상황 출력
    }


    public static void logoPrint() {
        String[] asciiArtLines = {
                " ██████╗   ██╗   ███╗   ██╗   ███████╗   ███╗   ███╗    █████╗ ",
                "██╔════╝   ██║   ████╗  ██║   ██╔════╝   ████╗ ████║   ██╔══██╗",
                "██║        ██║   ██╔██╗ ██║   █████╗     ██╔████╔██║   ███████║",
                "██║        ██║   ██║╚██╗██║   ██╔══╝     ██║╚██╔╝██║   ██╔══██║",
                "╚██████╗   ██║   ██║ ╚████║   ███████╗   ██║ ╚═╝ ██║   ██║  ██║",
                " ╚═════╝   ╚═╝   ╚═╝  ╚═══╝   ╚══════╝   ╚═╝     ╚═╝   ╚═╝  ╚═╝",
                "--------------------------start--------------------------------"
        };
        for (int i = 0; i < asciiArtLines.length; i++) {
            String color = RAINBOW_COLORS[i % RAINBOW_COLORS.length];
            System.out.println(color + asciiArtLines[i] + RESET_COLOR);
            try {
                Thread.sleep(200); // 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

    public static void firstMenu2() {
        String[] asciiArtLines = {
                " ██████╗   ██╗   ███╗   ██╗   ███████╗   ███╗   ███╗    █████╗ ",
                "██╔════╝   ██║   ████╗  ██║   ██╔════╝   ████╗ ████║   ██╔══██╗",
                "██║        ██║   ██╔██╗ ██║   █████╗     ██╔████╔██║   ███████║",
                "██║        ██║   ██║╚██╗██║   ██╔══╝     ██║╚██╔╝██║   ██╔══██║",
                "╚██████╗   ██║   ██║ ╚████║   ███████╗   ██║ ╚═╝ ██║   ██║  ██║",
                " ╚═════╝   ╚═╝   ╚═╝  ╚═══╝   ╚══════╝   ╚═╝     ╚═╝   ╚═╝  ╚═╝",
                "--------------------------start--------------------------------"
        };
        for (int i = 0; i < asciiArtLines.length; i++) {
            String color = RAINBOW_COLORS[i % RAINBOW_COLORS.length];
            System.out.println(color + asciiArtLines[i] + RESET_COLOR);
        }
        String[] firstMenu2 = {
                "\t\t\t\t\t   \u001B[31m1. login\u001B[0m\n",
                "\n",
                "\t\t\t\t\t   \u001B[31m2. sign up\u001B[0m\n"
        };
        for (String menu : firstMenu2) {
            System.out.print(menu);
        }

        for (int i = 0; i < 15; i++) {
            System.out.println();
        }
    }

}
