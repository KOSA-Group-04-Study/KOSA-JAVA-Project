package Project;

import Project.FilesIO.FileDataManager;
import Project.MovieSchedule.AdminSchedule;
import Project.Reservation.Reservation;
import Project.Reservation.ScreeningTime;
import Project.MovieSchedule.MovieScheduleManager;
import Project.User.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OutputView {

    static final String[] theaters = {"1관", "2관", "3관"};

    static final ScreeningTime[] times = {ScreeningTime.time_09, ScreeningTime.time_12, ScreeningTime.time_18};

    static final int TOTAL_SEAT_NUMBER = 25;

    static final String[] rainbowColors = {
            "\u001B[31m",   // Red
            "\u001B[33m",   // Yellow
            "\u001B[32m",   // Green
            "\u001B[36m",   // Cyan
            "\u001B[34m",   // Blue
            "\u001B[35m",   // Purple
            "\u001B[36m"    // Cyan
    };
    static final String resetColor = "\u001B[0m"; // 리셋 ANSI 이스케이프 시퀀스 (색상을 원래대로 되돌림)

    public static void main(String[] args) {

        printUserMenu2();
        printAdminMenu2();
        printBox("예외처리야 임마 \n 뭘봐");

    }

    // 사용자 메뉴
    public static void printUserMenu() {
        System.out.println("╔═════════━━━─── • ───━━━═════════╗");
        System.out.println("           메뉴를 입력하세요.           ");
        System.out.println(" 1-> 예매하기  2-> 예매조회  3-> 예매취소");
        System.out.println("    4-> 포인트 관리  exit-> 종료       ");
        System.out.println("╚═════════━━━─── • ───━━━═════════╝");
    }
    public static void printUserMenu2() {
        System.out.println("╔════════════════════━━━─── • ───━━━════════════════════╗");
        System.out.println("                      메뉴를 입력하세요.                      \n");
        System.out.println("    1-> 예매하기  2-> 예매조회  3-> 예매취소  4-> 포인트 관리     \n");
        System.out.println("                        exit-> 종료       ");
        System.out.println("╚════════════════════━━━─── • ───━━━════════════════════╝");
    }
    //관리자 메뉴
    public static void printAdminMenu() {
        System.out.println("╔═════════════━━━─── • ───━━━═════════════╗");
        System.out.println("               메뉴를 입력하세요.              ");
        System.out.println(" 1-> 영화상영등록  2-> 영화상영종료  3-> 회원정보조회");
        System.out.println("        4-> 상영목록조회  exit-> 종료          ");
        System.out.println("╚═════════════━━━─── • ───━━━═════════════╝");
    }
    public static void printAdminMenu2() {
        System.out.println("╔════════════════════━━━─── • ───━━━════════════════════╗");
        System.out.println("                      메뉴를 입력하세요.                      \n");
        System.out.println("    1-> 영화상영등록    2-> 영화상영종료     3-> 회원정보조회      \n");
        System.out.println("                        exit-> 종료       ");
        System.out.println("╚════════════════════━━━─── • ───━━━════════════════════╝");
    }

    // ASCII 아트를 이용한 상자 출력 메서드
    public static void printBox(String content) {
        String boxTop = "  ╔════════════════════━━━─── • ───━━━════════════════════╗";

        String boxBottom = "  ╚════════════════════━━━─── • ───━━━════════════════════╝";

        String[] lines = content.split("\n");

        System.out.println(boxTop);

        for (String line : lines) {
            // 중앙 정렬을 위해 상자의 가로 길이를 기준으로 문자열을 출력
            int paddingLength = (60 - line.length()) / 2; // 60자 가정
            String leftPadding = " ".repeat(paddingLength);
            String rightPadding = " ".repeat(60 - line.length() - paddingLength);
            System.out.println(leftPadding + line + rightPadding);
        }

        System.out.println(boxBottom);
    }

    public static void printScheduleBox(String selectedDate, AdminSchedule[][] adminSchedules) {
        StringBuilder boxContent = new StringBuilder();
        boxContent.append("선택하신 날짜 : ").append(selectedDate);
        boxContent.append("\n───────────────────────────────────────────────\n");

        for (int i = 0; i < theaters.length; i++) {
            boxContent.append(String.format("%13s)\n", "(" + theaters[i])); // 상영관 출력
            boxContent.append("(상영 시간)  :");
            for (ScreeningTime screen_time : times) { // 상영시간 출력
                boxContent.append(String.format("  %-10s", screen_time));
            }
            boxContent.append("\n(  영화  )  : ");
            for (int j = 0; j < times.length; j++) {
                if (adminSchedules[i][j] == null) { // 빈 상영시간이라면 공백을 출력
                    boxContent.append("            ");
                    continue;
                }
                AdminSchedule adminSchedule = adminSchedules[i][j];
                boxContent.append(String.format(" %-10s", adminSchedule.getMovie().getTitle())); // 영화이름 출력
            }

            boxContent.append("\n    (좌석 정보)  :");
            for (int j = 0; j < times.length; j++) {
                if (adminSchedules[i][j] == null) { // 빈 상영시간이라면 공백을 출력
                    boxContent.append("             ");
                    continue;
                }
                Schedule schedule = adminSchedules[i][j].getSchedule();
                boxContent.append(String.format("  %d/%d       ", schedule.getEmpty(), TOTAL_SEAT_NUMBER)); // 채워진좌석/총좌석 을 출력
            }
            boxContent.append("\n  ( 입력번호 ) :");
            for (int j = 0; j < times.length; j++) {
                boxContent.append(String.format("   [%d]       ", (i * times.length) + j + 1)); // 1~9 까지의 입력번호를 가진다.
            }
            boxContent.append("\n───────────────────────────────────────────────\n");
        }

        printBox(boxContent.toString());
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
                String.format(" | |   상영 일자 :    %s  %s    | |",reservation.getMovieDate(),times[theater[0]]),
                " |_|                                       |_|",
                String.format(" | |     상영관 :    %s                     | |",theaters[theater[1]]),
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
    }


    public static void firstMenu() {
        String[] firstMenu = {
            "\t\t\t\t\t   \u001B[31m1. login\u001B[0m\n",
                "\n",
            "\t\t\t\t\t   \u001B[31m2. sign up\u001B[0m\n"
        };
        for (int i = 0; i < firstMenu.length; i++) {
            try {
                Thread.sleep(150); // 0.005초
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.print(firstMenu[i]);
        }
        for (int i = 0; i < 3; i++) {
            try {
                Thread.sleep(150); // 0.005초
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println();
        }
        String b = "\r         please input :";
        String c = "\r                       ";

        for (int i = 0; i < 6; i++) {
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
        String greenColor = rainbowColors[2];
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
            System.out.println(greenColor + line + resetColor);
        }
        System.out.println();
        //프로그레스바 100.0 까지  0.2씩 증가
        for (double progress = 0.0; progress < 100.2; progress += 0.2) {
            updateProgressBar(progress);
            try {
                Thread.sleep(5); // 0.005초
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < 30; i++) {
            try {
                Thread.sleep(20); // 0.005초
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println();
        }
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(50); // 0.005초
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
            String color = rainbowColors[i % rainbowColors.length];
            if (i < numChars) {
                System.out.print(color + "=" + resetColor);
            } else {
                System.out.print(" ");
            }
        }
        System.out.printf("] %.2f%%", progress); // 현재 진행 상황 출력
    }


    static void logoPrint() {
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
            String color = rainbowColors[i % rainbowColors.length];
            System.out.println(color + asciiArtLines[i] + resetColor);
            try {
                Thread.sleep(200); // 대기
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println();
    }

}
