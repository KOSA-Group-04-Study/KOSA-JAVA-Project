package Project;

import Project.Manager.AuthenticationManager;
import Project.Manager.PaymentManager;
import Project.Manager.ReservationManager;
import Project.User.Admin;
import Project.User.Client;
import Project.User.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Getter
@Setter
public class Cinema { // 영화관

    Map<String, Map<Movie, Schedule[][]>> movieSchedule;
    List<Movie> movieList;
    List<User> userList;
    List<Reservation> reservationList;

    static Scanner sc = new Scanner(System.in);


    public void run() { // 시스템 시작


        String inputData = "";
        User user = null;
        boolean isAdmin = false; //  사용자인지 관리자인지 체크

        // 초기 메뉴화면 -> 로그인, 회원가입  2가지 기능
        loop:
        do {
            System.out.println("메뉴를 입력하세요. 1-> 로그인 2-> 회원가입 exit-> 종료 ");
            inputData = sc.nextLine();

            switch (inputData) {
                case "1": {
                    //로그인
                    if ((user = AuthenticationManager.Login()) != null) { //user = ~ 유저객체 채우기 (로그인 성공시)}
                        isAdmin = user.isAdmin();
                        break loop;
                    } else {
                        continue;
                    }
                }
                case "2": {
                    //회원가입
                    AuthenticationManager.Register();
                    break ;
                }
                case "exit": {
                    System.exit(0);
                }

                default:
                    System.out.println("입력 잘못되었습니다.");
            }

        }

        while (true) ;


        // 사용자에 따라 다른 메뉴 실행
        if(isAdmin) {
            adminMenu(user);}
        else {
            clientMenu((Client) user);}
    }

    private static void clientMenu(Client client) {

        String inputData = "";
//        Client client = null;
//        if(user != null && user instanceof Client){
//            client = (Client) user;
//        }


        //사용자 메인메뉴
        do {

            System.out.println("메뉴를 입력하세요. 1-> 예매하기 2-> 예매조회 3-> 예매취소 4-> 포인트 관리 exit-> 종료 ");
            inputData = sc.nextLine();

            switch (inputData) {
                case "1": {
                    //영화 예매하기
                    ReservationManager.makeMovieReservation(client);
                    break;
                }
                case "2": {
                    //예매조회
                    ReservationManager.getReservation(client);
                    break;
                }
                case "3": {
                    //예매취소
                    ReservationManager.deleteReservation(client);
                    break;
                }
                case "4": {
                    //포인트 관리
                    PaymentManager.pointManage(client);
                    break;
                }
                case "exit": {
                    System.exit(0);
                }

                default: {
                    System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
                }
            }

        }

        while (true) ;
    }

    private static void adminMenu(User user) {
        //관리자 메인메뉴

        String inputData = "";
        Admin admin = null;
        if(user != null && user instanceof Admin){
            admin = (Admin) user;
        }

        do {
            System.out.println("메뉴를 입력하세요. 1-> 영화상영등록 2-> 영화상영종료 3-> 회원정보조회 4-> 상영목록조회 exit-> 종료 ");
            // 추가작업 ?! -> 5. 새로운"영화"등록
            inputData = sc.nextLine();

            switch (inputData) {
                case "1": {
                    //영화상영등록
                    admin.addMovieToSchedule();
                    break;
                }
                case "2": {
                    //영화상영종료
                    admin.deleteMovieFromSchedule();
                    break;
                }
                case "3": {
                    //회원정보조회
                    admin.getUserInfo();
                    break;
                }
                case "4": {
                    //상영목록조회
                    admin.getMovieToSchedule();
                    break;
                }
                case "exit": {
                    //포인트 관리
                    System.exit(0);
                }
                default:
                    System.out.println("입력 잘못되었습니다.");
            }

        }
        while (true) ;
    }




    public Map<String, Map<Movie, Schedule[][]>> getMovieSchedule() {
        return movieSchedule;
    }

    public void setMovieSchedule(Map<String, Map<Movie, Schedule[][]>> movieSchedule) {
        this.movieSchedule = movieSchedule;
    }
}



