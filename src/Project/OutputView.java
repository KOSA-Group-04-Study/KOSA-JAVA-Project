package Project;

import Project.Reservation.Reservation;
import Project.Reservation.ScreeningTime;
import Project.User.User;

public class OutputView {

    static final String[] theaters = {"1관", "2관", "3관"};
    static final ScreeningTime[] times = {ScreeningTime.time_09, ScreeningTime.time_12, ScreeningTime.time_18};

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
//        loading();
//        logoPrint();
//        firstMenu();
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
