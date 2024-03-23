package Project.Manager;

import Project.User.Admin;
import Project.User.User;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Project.Manager.FileDataManager.writeUserInfoToFile;

public final class AuthenticationManager {


    public static final String PATH = "src/Project/Files/User.txt";
    Charset charset = StandardCharsets.UTF_8;
    static Scanner sc = new Scanner(System.in);

    //로그인
    public static User Login() {

        // 여기서 아이디, 비밀번호 입력받고  여기서 while -> 파일에서 읽어서 검증
        System.out.println("안녕하세요.");
        System.out.println("아이디를 입력해주세요.");
        String id = sc.nextLine();
        System.out.println("비밀번호를 입력해주세요");
        String password = sc.nextLine();

        // try - with - resource 적용
        try (BufferedReader bis = new BufferedReader(new FileReader(PATH))) {
            String line;
            while ((line = bis.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, ",");
                String storedId = tokenizer.nextToken().trim();
                String storedPassword = tokenizer.nextToken().trim();
                String storedName = tokenizer.nextToken().trim();
                String soredPhoneNumber = tokenizer.nextToken().trim();
                boolean isAdmin = Boolean.parseBoolean(tokenizer.nextToken().trim());

                if (id.equals(storedId) && password.equals(storedPassword)) {
                    // 빌더 패턴 적용해서 싹 넘기면 좋을텐데 왜 안되는거지
                    if (isAdmin == true) {
                        return Admin.builder().
                    }
                }

            }


        } catch (FileNotFoundException e) {
            System.out.println("파일이 없습니다.");
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //성공시 user객체 , 하기싫을때 null
        return null;
    }


    //회원가입
    public static void Register() {
        // 여기서 아이디, 비밀번호 입력받고 정규표현식으로 체크  여기서 while , 파일쓰기도 해야함.
        // 유저 어떤식 저장될지 형식

    }
}