package Project.User;


import lombok.Getter;

import java.io.Serializable;


@Getter
public class User implements Serializable {
    private String email; // 로그인할떄 아이디
    private String password;
    private String name;
    private String phoneNumber;
    private boolean isAdmin;

    public User(String email, String password, String name, String phoneNumber, boolean isAdmin) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isAdmin = isAdmin;
    }

}


/*
객체는 저장해서 Users.txt 에 한줄씩 저장
 */