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
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{5,}$";; // 특수 문자 1개 이상 포함, 영어 대소문 1개씩 무조건, 숫자도 1개 이상 무조건 최소 5자 이상
        String phonePattern = "010-\\d{4}-\\d{4}";

        while(true) {

            System.out.println("회원가입을 시작합니다.");
            System.out.print("아이디를 입력하세요 : " );
            // 이메일 입력
            String emailId = sc.nextLine();

            if(!validateEmailFormat(emailId ,emailPattern)) {
                System.out.println("이메일 형식에 맞지 않습니다. 다시 입력해주세요.");
                continue;
            }

            System.out.println();
            System.out.println("비밀번호는 숫자와 영어 그리고 특수 문자가 들어가며 5자 이상이어야합니다.");
            System.out.print("비밀번호를 입력하세요 : " );
            // 비밀번호 입력
            String password = sc.nextLine();

            if(!validatePasswordFormat(password,passwordPattern)) {
                System.out.println("비밀번호 형식에 맞지 않습니다. 이메일부터 다시 입력해주세요.");
                continue;
            }

            System.out.println();
            System.out.println("이름을 입력해주세요.");

            String name = sc.nextLine();

            System.out.println();
            System.out.println("전화번호는 010-xxxx-xxxx 형식으로 입력해주세요.");
            System.out.println("전화번호를 입력해주세요. : ");


            String phoneNumber = sc.nextLine();

            if(!validatePhoneFormat(phoneNumber,phonePattern)){
                System.out.println("전화번호 형식에 맞지 않습니다. 이메일부터 다시 입력해주세요.");
                continue;

            }

            // 빌더패턴이 안먹히네..??
            //User client = Client.builder().

            // 파일쓰기 ㅇ--> 위에서 생성한 객체의 유저 정보를 넣는다.
            // 유저 정보 저장 로직이 어떻게 될지 모르겠는데. 유저 정보를 파라미터로 받아야하지 않나?

            writeUserInfoToFile();

        }

    }


    // 전화번호 형식에 맞춰서 입력 패턴 확인.
    private static boolean validatePhoneFormat(String phoneNumber, String phonePattern) {
        Pattern pattern = Pattern.compile(phonePattern);
        Matcher matcher = pattern.matcher(phonePattern);
        return matcher.matches();
    }

    // 비밀번호 형식에 맞춰서 입력 패턴 확인.
    private static boolean validatePasswordFormat(String password, String passwordPattern) {
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    // 이메일 형식에 맞춰서 입력 패턴 확인.
    public static boolean validateEmailFormat(String emailId, String emailPattern) {
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(emailId);
        return matcher.matches();
    }
}

