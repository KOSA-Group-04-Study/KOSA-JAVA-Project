package Project;

import Project.Manager.FileDataManager;
import Project.User.Client;
import Project.User.User;

import java.util.ArrayList;
import java.util.List;

//전체 유저정보 확인 임시 클래스
//전체 유저정보를 출력
public class UserPrinter {
    public static void main(String[] args) {

        // 파일에서 유저 정보 읽기
        List<Client> readUsers = FileDataManager.readUserInfoFromFile();


        // 읽어온 유저 정보 출력
        if (readUsers != null) {
            System.out.println("전체 유저 정보:");
            for (User user : readUsers) {
                System.out.println("아이디: " + user.getEmail());
                System.out.println("비밀번호: " + user.getPassword());
                System.out.println("이름: " + user.getName());
                System.out.println("전화번호: " + user.getPhoneNumber());
                System.out.println("관리자 여부: " + user.isAdmin());
                System.out.println("-----------------------");
            }
        } else {
            System.out.println("파일에서 읽어오는 것을 실패했습니다.");
        }
    }
}
