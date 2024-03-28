package Project.Payment;

import Project.Exception.ExitException;
import Project.FilesIO.FileDataManager;
import Project.OutputView;
import Project.User.Client;
import Project.User.User;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public final class PaymentManager {
    //메소드는 스태틱
    static Scanner sc = new Scanner(System.in);
    private static final String EXIT_COMMAND = "exit";

    public static void payBack(User user, Integer price) {
        // 결제 취소 -> 포인트 반환
        Integer retrunPoint = ((Client) user).getPoint() + price;
        ((Client) user).setPoint(retrunPoint);

    }

    public static boolean payPoint(User user,Integer price) {

        // 포인트 결제 -> 포인트 감소
        // 취소시 포인트 반환 -> 포인트 증가

        try {
           Integer userPoint = ((Client) user).getPoint();
           // 구매 가능 검증
           validatePaymentCapability(userPoint, price);

           userPoint -= price;
           ((Client) user).setPoint(userPoint);
            System.out.println();
           // System.out.println(" 결제완료 ");
           // System.out.println(user.getName() + "님의 포인트 잔액은 " + ((Client) user).getPoint() + " 입니다.");

            String payInfo = """
                 결제완료
                   """ +(user.getName() + "님의 포인트 잔액은 " + ((Client) user).getPoint() + " 입니다.") ;
            OutputView.printBox(payInfo);
            // 파일 쓰기

            return true;

        } catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // 금액 부족 검증
    private static void validatePaymentCapability(int userPoint, Integer price) throws IllegalArgumentException{
        if( userPoint - price < 0 ) {
            throw new IllegalArgumentException("결제가 불가능합니다. 포인트가 부족합니다.");
        }
    }



    // 포인트 관리
    public static void pointManage(User user) {  //포인트 조회 + 충전선택
        List<User> usersList = FileDataManager.readUserInfoFromFile();              // User.txt

        //현재 유저 정보 가져오기
        Optional<User> foundUserOptional  = usersList.stream()
                .filter(findUser -> findUser.getEmail().equals(user.getEmail()))
                .findFirst();

        Client client = (Client) foundUserOptional.orElse(null);



//        user = (Client) user;
        String PointManageIntro = """
                메뉴를 입력하세요.
                1-> 충전하기               
                """;
        OutputView.printInputMessage(PointManageIntro);

        try {
            String inputData = sc.nextLine().trim();

            if(inputData.equals(EXIT_COMMAND)){
                return;
            }

            if(inputData.equals("1")){
                insertPoint(client);
            } else {
                throw new IllegalArgumentException();
            }

        } catch (IllegalArgumentException e) {
            OutputView.printExceptionMessage("1 또는 exit만 입력하세요.");
        }

        //유저 파일 쓰기
    }


    // 포인트 충전
    private static void insertPoint(Client user) {

        String insertPointIntro= """
             충전 금액을 입력해주세요. 
           금액은 10000원 단위로 충전 가능합니다.
                """;
        insertPointIntro += "현재 잔여포인트는 " + user.getPoint() + "\n";
        OutputView.printInputMessage(insertPointIntro);
        int quantity =0;

        while (true) {
            String needQuantity = "";
            try {

                needQuantity = sc.nextLine();
                if (needQuantity.equals(EXIT_COMMAND)) throw new ExitException();
                if (!ValidationQuantity(Integer.parseInt(needQuantity))) {
                    OutputView.printExceptionMessage("\t죄송합니다. 금액은 10000원 단위로 충전가능합니다.");
                    continue;
                }
            } catch (NumberFormatException e) {
                OutputView.printExceptionMessage("\t숫자를 입력해주세요");
                continue;
            } catch (ExitException e) {
                return;
            }
            quantity += Integer.parseInt(needQuantity);
            break;
        }

        int changePoint = user.getPoint() + quantity;

        user.setPoint(changePoint);
        OutputView.printBox("충전 후 현재 포인트 : "+changePoint);

        // 파일 쓰기
        // 사용자 정보 읽어오기
        List<User> existingUsers = FileDataManager.readUserInfoFromFile();
        // 입력받은 데이터와 일치하는 파일 정보 찾아 포인트 변환 내용 적용
        for (User existingUser : existingUsers) {
            if(existingUser.getEmail().equals(user.getEmail())){
                Client client = (Client)existingUser;
                client.setPoint(changePoint);
                break;
            }
        }
        // 파일에 변경된 사용자 정보 덮어씌우기.
        FileDataManager.writeUserInfoToFile(existingUsers);

    }

    private static boolean ValidationQuantity(int quantity) {
        return quantity % 10000 == 0;
    }
}


