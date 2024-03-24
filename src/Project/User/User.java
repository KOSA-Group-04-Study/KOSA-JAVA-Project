package Project.User;


import lombok.Getter;

import java.io.Serializable;


@Getter
public abstract class User implements Serializable {
    private String email; // 로그인할떄 아이디
    private String password;
    private String name;
    private String phoneNumber;
    private boolean isAdmin;
}


/*
객체는 저장해서 Users.txt 에 한줄씩 저장
 */