package Project.Manager;

import Project.Movie;
import Project.Reservation;
import Project.Schedule;
import Project.User.Client;
import Project.User.User;

import java.util.List;
import java.util.Map;

//파일 입출력시 오류
public class ReservationManager {


    public static void makeMovieReservation(User user) {
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
}
