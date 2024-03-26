package Project.User;

import Project.FilesIO.FileDataManager;
import Project.MovieSchedule.MovieScheduleManager;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class Admin extends User implements Serializable{

    public Admin(String email, String password, String name, String phoneNumber, boolean isAdmin) {
        super(email, password, name, phoneNumber, isAdmin);
    }


    // 영화를 상영스케줄에 등록
    public void addMovieToSchedule() {
        MovieScheduleManager.registerMovieToSchedule();
    }

    // 영화 상영스케줄 조회
    public void getMovieToSchedule() {
        MovieScheduleManager.printScheduleForInputDate();
    }

    //영화상영종료 (스케쥴표에서)
    public void deleteMovieFromSchedule() {
        MovieScheduleManager.deleteMovieFromSchedule();
    }


    //회원 정보 조회
    public void getUserInfo() {
        List<User> users = FileDataManager.readUserInfoFromFile();
        for (User user : users) {
            System.out.println("-------------------");
            System.out.println("사용자 정보:");
            System.out.println("아이디: " + user.getEmail());
            System.out.println("이름: " + user.getName());
            System.out.println("전화번호: " + user.getPhoneNumber());
            System.out.println("관리자 여부: " + (user.isAdmin() ? "O" : "X"));
            if (user instanceof Client client) {
                System.out.println("포인트 :"+client.getPoint());
            }
            System.out.println("-------------------");
        }
        // 뿌리기
    }

    //예매 내역
}
