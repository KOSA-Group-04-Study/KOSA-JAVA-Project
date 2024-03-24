package Project.User;

import Project.Manager.FileDataManager;
import Project.Manager.MovieScheduleManager;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Admin extends User {


    public Admin(String email, String password, String name, String phoneNumber, boolean isAdmin) {
        super(email, password, name, phoneNumber, isAdmin);
    }


    // 영화를 상영스케줄에 등록
    public void addMovieToSchedule() {
        MovieScheduleManager.registerMovieToSchedule();
    }

    // 영화 상영스케줄 조회
    public void getMovieToSchedule() {
        MovieScheduleManager.getMovieSchedule();
    }

    //영화상영종료 (스케쥴표에서)
    public void deleteMovieFromSchedule() {
        MovieScheduleManager.deleteMovieFromSchedule();
    }


    //회원 정보 조회
    public void getUserInfo() {
        List<User> users = FileDataManager.readUserInfoFromFile();
        // 뿌리기
    }

    //예매 내역
}
