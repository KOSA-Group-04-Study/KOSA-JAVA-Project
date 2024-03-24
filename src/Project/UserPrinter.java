package Project;

import Project.Manager.FileDataManager;
import Project.User.User;

import java.util.ArrayList;
import java.util.List;

public class UserPrinter {
    public static void main(String[] args) {
        // 임시 유저 정보 생성
        List<User> users = new ArrayList<>();
        //users.add(new User("user1@naver.com", "password1", "User 1", "01012345678", false));
        //users.add(new User("user2@gmail.com", "password2", "User 2", "01012345679", false));
        //users.add(new User("admin@gmail.com", "adminpassword", "Admin", "01012345670", true));

        // 파일에 유저 정보 저장
        FileDataManager.writeUserInfoToFile(users);

        // 파일에서 유저 정보 읽기
        List<User> readUsers = FileDataManager.readUserInfoFromFile();

        // 읽어온 유저 정보 출력
        if (readUsers != null) {
            System.out.println("전체 유저 정보:");
            for (User user : readUsers) {
                System.out.println("Email: " + user.getEmail());
                System.out.println("Password: " + user.getPassword());
                System.out.println("Name: " + user.getName());
                System.out.println("Phone Number: " + user.getPhoneNumber());
                System.out.println("Is Admin: " + user.isAdmin());
                System.out.println("-----------------------");
            }
        } else {
            System.out.println("파일에서 읽어오는 것을 실패했습니다.");
        }
    }
}
