package Project.FilesIO;

import Project.Movie;
import Project.Reservation.Reservation;
import Project.Schedule;
import Project.User.User;

import java.io.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class FileDataManager {

    // 파일 경로 상수 정의
    private static final String MOVIE_FILE_PATH = "src/Project/Files/Movies.txt";
    private static final String RESERVATION_FILE_PATH = "src/Project/Files/Reservations.txt";
    private static final String USER_INFO_FILE_PATH = "src/Project/Files/Users.txt";
    private static final String MOVIE_SCHEDULE_FILE_PATH = "src/Project/Files/MovieSchedule.txt";


    // 공통 파일 쓰기 메소드
    private static void writeToFile(String filePath, Object data, String successMessage) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(filePath))) {
            outputStream.writeObject(data);
            System.out.println(successMessage);
        } catch (IOException e) {
            System.err.println("파일 쓰기 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 공통 파일 읽기 메소드
    private static Object readFromFile(String filePath, String errorMessage) {
        try (FileInputStream fis = new FileInputStream(filePath);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(errorMessage + ": " + e.getMessage());
            return null;
        }
    }

    // 영화 정보 파일 쓰기
    public static void writeMoviesToFile(List<Movie> movies) {
        writeToFile(MOVIE_FILE_PATH, movies, "영화 정보가 파일에 저장되었습니다.");
    }

    // 예약 내역 파일 쓰기
    public static void writeReservationToFile(List<Reservation> reservations) {
        writeToFile(RESERVATION_FILE_PATH, reservations, "예매내역이 파일에 저장되었습니다.");
    }

    // 사용자 정보 파일 쓰기
    public static void writeUserInfoToFile(List<User> users) {
        writeToFile(USER_INFO_FILE_PATH, users, "사용자 정보가 성공적으로 저장되었습니다.");
    }

    // 무비 스케줄 파일 쓰기
    public static void writeMovieScheduleToFile(Map<String, Map<Movie, Schedule[][]>> movieSchedule) {
        writeToFile(MOVIE_SCHEDULE_FILE_PATH, movieSchedule, "무비 스케줄이 성공적으로 저장되었습니다.");
    }

    // 파일 읽기 -> 영화 정보를 파일에서 읽기
    public static List<Movie> readMoviesFromFile() {
        return (List<Movie>) readFromFile(MOVIE_FILE_PATH, "파일 읽기 오류가 발생했습니다");
    }

    //파일읽기 -> 예매내역 읽기
    public static List<Reservation> readReservationsFromFile() {
        return (List<Reservation>) readFromFile(RESERVATION_FILE_PATH, "파일 읽기 오류가 발생했습니다");
    }

    //파일읽기 -> (전체)유저정보 읽기
    public static List<User> readUserInfoFromFile() {
        return (List<User>) readFromFile(USER_INFO_FILE_PATH, "파일 읽기 오류가 발생했습니다");
    }

    public static Map<String, Map<Movie, Schedule[][]>> readMovieScheduleFromFile() {
        return (Map<String, Map<Movie, Schedule[][]>>) readFromFile(MOVIE_SCHEDULE_FILE_PATH, "파일 읽기 오류가 발생했습니다");
    }

}
