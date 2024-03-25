package Project.User;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
@AllArgsConstructor
public class User implements Serializable {
    protected String email; // 로그인할떄 아이디
    protected String password;
    protected String name;
    protected String phoneNumber;
    protected boolean isAdmin;

//    public User(String email, String password, String name, String phoneNumber, boolean isAdmin) {
//        this.email = email;
//        this.password = password;
//        this.name = name;
//        this.phoneNumber = phoneNumber;
//        this.isAdmin = isAdmin;
//    }

}


/*
객체는 저장해서 Users.txt 에 한줄씩 저장
 */