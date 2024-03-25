package Project.Manager;

import Project.Movie;
import Project.Reservation;
import Project.Schedule;
import Project.User.Client;
import Project.User.User;
import Project.User.Client;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class FileDataManager {
    /*
    매번하자. 화이팅!
     */

    // 파일 경로 상수 정의
    private static final String MOVIE_FILE_PATH = "src/Project/Files/Movies.txt";
    private static final String RESERVATION_FILE_PATH = "src/Project/Files/Reservations.txt";
    private static final String USER_INFO_FILE_PATH = "src/Project/Files/Users.txt";
    private static final String MOVIE_SCHEDULE_FILE_PATH = "src/Project/Files/MovieSchedule.txt";


    //메소드는 스태틱

    // 초기에 Movies.txt와 Movies.Schedule.txt에 정보 저장하기 위해
    // 파일쓰기 -> 영화 정보를 파일에 쓰기
    public static void writeMoviesToFile(List<Movie> movies) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(MOVIE_FILE_PATH))) {
            outputStream.writeObject(movies); //직렬화
            System.out.println("영화 정보가 파일에 저장되었습니다.");
        } catch (IOException e) {
            System.err.println("영화 정보를 파일에 쓰는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 파일 읽기 -> 영화 정보를 파일에서 읽기
    public static List<Movie> readMoviesFromFile() {
        try (FileInputStream fis = new FileInputStream(MOVIE_FILE_PATH);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (List<Movie>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("파일 읽기 오류가 발생했습니다: " + e.getMessage());
            return null;
        }

    }

    //파일쓰기 -> 예매내역 저장
    public static void writeReservationToFile(List<Reservation> reservations) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(RESERVATION_FILE_PATH))) {
            outputStream.writeObject(reservations);
            System.out.println("예매내역이 파일에 저장되었습니다.");
        } catch (IOException e) {
            System.err.println("예매내역을 파일에 쓰는 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    //파일읽기 -> 예매내역 읽기
    public static List<Reservation> readReservationsFromFile() {
        try (FileInputStream fis = new FileInputStream(RESERVATION_FILE_PATH);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (List<Reservation>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("파일 읽기 오류가 발생했습니다: " + e.getMessage());
            return null;
        }

    }


    //파일쓰기 -> 유저정보 저장
    public static void writeUserInfoToFile(List<User> users) {
        try (FileOutputStream fos = new FileOutputStream(USER_INFO_FILE_PATH);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(users);
            System.out.println("사용자 정보가 성공적으로 저장되었습니다.");
        } catch (IOException e) {
            System.err.println("파일에 쓰기 오류가 발생했습니다: " + e.getMessage());
        }
    }




    //파일읽기 -> (전체)유저정보 읽기
    public static List<User> readUserInfoFromFile() {
        try (FileInputStream fis = new FileInputStream(USER_INFO_FILE_PATH);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (List<User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("파일 읽기 오류가 발생했습니다: " + e.getMessage());
            return null;
        }

    }


    //파일쓰기 -> 무비스케줄 저장
    public static void writeMovieScheduleToFile(Map<String, Map<Movie, Schedule[][]>> movieSchedule) {
        try (FileOutputStream fos = new FileOutputStream(MOVIE_SCHEDULE_FILE_PATH);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(movieSchedule);
            System.out.println("무비 스케줄이 성공적으로 저장되었습니다.");
        } catch (IOException e) {
            System.err.println("파일에 쓰기 오류가 발생했습니다: " + e.getMessage());
        }
    }

    //파일읽기 -> (전체)무비스케줄 읽기
    public static Map<String, Map<Movie, Schedule[][]>> readMovieScheduleFromFile() {
        try (FileInputStream fis = new FileInputStream(MOVIE_SCHEDULE_FILE_PATH);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (Map<String, Map<Movie, Schedule[][]>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("파일 읽기 오류가 발생했습니다: " + e.getMessage());
            return null;
        }

    }


    //삭제 -> 사용자가 (해당 예약번호)예매내역 삭제

    public static void deleteReservationFromFile(String reservationNumber) {
        // 파일에서 예약 목록을 읽어온다
        List<Reservation> reservations = readReservationsFromFile();

        // 예약 목록을 순회하면서 해당 예약 번호에 해당하는 예약을 찾아 삭제한다
        Iterator<Reservation> iterator = reservations.iterator();//다음 예약이 있는지 확인
        boolean deleted = false;
        while (iterator.hasNext()) {
            Reservation reservation = iterator.next(); //다음 예약 객체

            //주어진 예약 번호와 일치하는 예약을 찾았다면, 해당 예약을 삭제
            if (reservation.getReservationNumber().equals(reservationNumber)) {
                iterator.remove();
                deleted = true; //예약이 성공적으로 삭제
                break; // 해당 예약을 찾았으므로 반복문 종료
            }
        }

        // 예약이 삭제되었으면 새로운 예약 목록을 파일에 쓴다
        if (deleted) {
            writeReservationToFile(reservations);
            System.out.println("예매 번호 " + reservationNumber + "의 예매 내역이 성공적으로 삭제되었습니다.");
        } else {
            System.out.println("예매 번호 " + reservationNumber + "에 해당하는 예매 내역이 없습니다.");
        }
    }

    //삭제 -> 무비스케줄 삭제
    // 주어진 날짜, 영화 제목, 상영관 인덱스, 시간 인덱스에 해당하는 무비 스케줄을 파일에서 삭제
    public static void deleteMovieScheduleFromFile(String date, String title, int theaterIndex, int timeIndex) {
        // 파일에서 무비 스케줄을 읽어온다
        Map<String, Map<Movie, Schedule[][]>> movieSchedule = FileDataManager.readMovieScheduleFromFile();

        // 해당하는 날짜의 스케줄을 가져온다
        Map<Movie, Schedule[][]> scheduleByDate = movieSchedule.get(date);
        if (scheduleByDate != null) {
            // 해당하는 영화의 스케줄을 가져온다
            Schedule[][] schedules = scheduleByDate.get(new Movie(title, null, null));
            if (schedules != null && schedules.length > theaterIndex && schedules[theaterIndex].length > timeIndex) {
                // 선택한 배열의 무비 스케줄을 삭제한다
                schedules[theaterIndex][timeIndex] = null;
                System.out.println("무비 스케줄이 성공적으로 삭제되었습니다.");

                // 삭제된 무비 스케줄을 다시 파일에 쓴다
                FileDataManager.writeMovieScheduleToFile(movieSchedule);
                return;
            }
        }

        System.out.println("해당 날짜, 영화, 상영관, 상영 시간에 해당하는 무비 스케줄이 없습니다.");
    }

}
