package Project.Manager;

import Project.Movie;
import Project.Reservation;
import Project.Schedule;
import Project.User.User;

import java.util.List;
import java.util.Map;

public final class FileDataManager {
    /*
    매번하자. 화이팅!
     */


    //메소드는 스태틱

    //파일쓰기 -> 예매내역 저장
    public static void writeReservationToFile() {

    }

    //파일읽기 -> 예매내역 읽기
    public static List<Reservation> readReservationFromFile() {
        return null;
    }

    //파일쓰기 -> 유저정보 저장
    public static void writeUserInfoToFile() {

    }


    //파일읽기 -> 유저정보 읽기
    public static List<User> readUserInfoFromFile() {
        return null;
    }

    //파일쓰기 -> 무비스케줄 저장
    public static void writeMovieScheduleToFile() {

    }

    //파일읽기 -> 무비스케줄 읽기
    public static Map<String, Map<Movie, Schedule[][]>> readMovieScheduleFromFile() {
        return null;
    }


    //삭제 -> 예매내역 삭제

    public static void deleteReservationFromFile() {

    }

    //삭제 -> 무비스케줄 삭제

    public static void deleteMovieScheduleFromFile() {

    }

}
