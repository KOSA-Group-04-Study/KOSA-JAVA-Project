package Project.User;

import Project.Exception.ExitException;
import Project.FilesIO.FileDataManager;
import Project.OutputView;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static Project.FilesIO.FileDataManager.readUserInfoFromFile;

public final class AuthenticationManager {

    static Scanner sc = new Scanner(System.in);

    static String emailPattern =  "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    static String passwordPattern = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).{5,}$"; // 특수 문자 1개 이상 포함, 영어 대소문 1개씩 무조건, 숫자도 1개 이상 무조건 최소 5자 이상
    private static final String EXIT_COMMAND = "exit";

    //로그인
    public static User login() {

        // 여기서 아이디, 비밀번호 입력받고  여기서 while -> 파일에서 읽어서 검증
        try {
            String id = getEmailInput(emailPattern);
            String password = getPasswordInput(passwordPattern);
            //파일에서 사용자 정보 읽어오기 임시 확인용 코드
            List<User> users = readUserInfoFromFile();
            // 파일에서 읽어온 사용자 정보 확인
            if (users != null) {
                for (User user : users) {
                    if (user.getEmail().equals(id) && user.getPassword().equals(password)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("로그인 성공!").append("\n");
                        sb.append("사용자 정보:").append("\n");
                        sb.append("아이디: " + user.getEmail()).append("\n");
                        sb.append("이름: " + user.getName()).append("\n");
                        sb.append("전화번호: " + user.getPhoneNumber()).append("\n");
                        sb.append("관리자 여부: " + (user.isAdmin() ? "O" : "X")).append("\n");
                        OutputView.printBox(sb.toString());

                        return user;

                    }
                }
                String failLoginIntro = """
                  로그인 실패 : 해당 계정이 존재하지 않습니다.
                  메뉴로 돌아갑니다.
                   """;
                OutputView.printBox(failLoginIntro);
                return null;
            }
        } catch (ExitException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return null;
    }

    //회원가입
    public static void register() {
        // 여기서 아이디, 비밀번호 입력받고 정규표현식으로 체크  여기서 while , 파일쓰기도 해야함.
        // 유저 어떤식 저장될지 형식
        try {
            String phonePattern = "010-\\d{4}-\\d{4}";
            String emailId = getEmailInput(emailPattern);
            checkDuplicationEmail(emailId);
            String password = getPasswordInput(passwordPattern);
            String name = getNameInput();
            String phoneNumber = getPhoneNumberInput(phonePattern);

            String ment = name+"님\n"+  """
                    회원가입 완료되었습니다.
                    환영합니다!
                    """;
            OutputView.printBox(ment);

            // 파일에 사용자 정보 저장 임시코드
            Client newUser = new Client(emailId, password, name, phoneNumber, false,0, new LinkedList<>()); //일단 isAdmin 기본 false(사용자)
            List<User> usersList;
            List<User> existingUsers = ((usersList = FileDataManager.readUserInfoFromFile()) != null) ? usersList : new ArrayList<>();
            // 기존 사용자 정보 읽어오기
//        List<Client> existingUsers = new ArrayList<>(FileDataManager.readUserInfoFromFile());
            // 기존 사용자 정보와 새로운 사용자 정보를 합쳐야함
            existingUsers.add(newUser);
            // 파일에 추가된 사용자 정보를 덮어쓴다.
            FileDataManager.writeUserInfoToFile(existingUsers);

        } catch (ExitException e) {
            System.out.println(e.getMessage());
        }

    }

    //아이디가 중복체크
    private static void checkDuplicationEmail(String emailId) {
        List<User> existingUsers = FileDataManager.readUserInfoFromFile();
        for (User existingUser : existingUsers) {
            if(existingUser.getEmail().equals(emailId)){
                OutputView.printExceptionMessage("""
                        존재하는 계정입니다.
                        가입된 계정으로 로그인하세요. 메뉴로 이동합니다.
                        """);
                throw new ExitException();
            }
        }
    }

    // 이메일 입력을 받는 메소드
    private static String getEmailInput(String emailPattern) {
        OutputView.printInputMessage("""
            안녕하세요 고객님!
            이메일을 입력해주세요.
            """);
        while (true) {
            String emailId = sc.nextLine();
            if(emailId.equals(EXIT_COMMAND)) throw new ExitException();
            if (validateEmailFormat(emailId, emailPattern)) {
                return emailId;
            } else {
                OutputView.printExceptionMessage("이메일 형식에 맞지 않습니다. ");
            }
        }
    }


    // 비밀번호 입력을 받는 메소드
    private static String getPasswordInput(String passwordPattern) {
        OutputView.printInputMessage("""
                    비밀번호는 숫자와 영어 그리고 특수 문자가 들어가며 5자 이상이어야합니다.
                    비밀번호를 입력해주세요.
                    """);
        while (true) {
            String password = sc.nextLine();
            if(password.equals(EXIT_COMMAND)) throw new ExitException();
            if (validatePasswordFormat(password, passwordPattern)) {
                return password;
            } else {
                OutputView.printExceptionMessage("비밀번호 형식에 맞지 않습니다. ");

            }
        }
    }

    // 이름 입력을 받는 메소드
    private static String getNameInput() {

        OutputView.printInputMessage("이름을 입력해주세요.");
        String name = sc.nextLine();
        if(name.equals(EXIT_COMMAND)) throw new ExitException();
        return name;
    }


    // 전화번호 입력을 받는 메소드
    private static String getPhoneNumberInput(String phonePattern) {
        OutputView.printInputMessage("""
                    전화번호는 010-xxxx-xxxx 형식으로 입력해주세요.
                    전화번호를 입력해주세요.
                    """);
        while (true) {
            String phoneNumber = sc.nextLine();
            if(phoneNumber.equals(EXIT_COMMAND)) throw new ExitException();
            if (validatePhoneFormat(phoneNumber, phonePattern)) {
                return phoneNumber;
            } else {

                OutputView.printExceptionMessage("전화번호를 형식에 맞지 않습니다. ");
            }
        }
    }

    // 전화번호 형식에 맞춰서 입력 패턴 확인
    private static boolean validatePhoneFormat(String phoneNumber, String phonePattern) {
        Pattern pattern = Pattern.compile(phonePattern);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    // 비밀번호 형식에 맞춰서 입력 패턴 확인
    private static boolean validatePasswordFormat(String password, String passwordPattern) {
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    // 이메일 형식에 맞춰서 입력 패턴 확인
    public static boolean validateEmailFormat(String emailId, String emailPattern) {
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(emailId);
        return matcher.matches();
    }
}

