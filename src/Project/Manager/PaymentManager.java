package Project.Manager;

import Project.User.Client;
import Project.User.User;
import org.w3c.dom.ls.LSOutput;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class PaymentManager {
    //메소드는 스태틱
    static Scanner sc = new Scanner(System.in);

    public static void payBack(User user, Integer price) {
        // 결제 취소 -> 포인트 반환
        Integer retrunPoint = ((Client) user).getPoint() + price;
        ((Client) user).setPoint(retrunPoint);
    }

    public static void payPoint(User user,Integer price) {
        // 포인트 결제 -> 포인트 감소
        // 취소시 포인트 반환 -> 포인트 증가

        try {
           Integer userPoint = ((Client) user).getPoint();
           // 구매 가능 검증
           validatePaymentCapability(userPoint, price);

           userPoint -= price;
           ((Client) user).setPoint(userPoint);
            System.out.println();
            System.out.println(" 결제완료 ");
            System.out.println(user.getName() + "님의 포인트 잔액은 " + ((Client) user).getPoint() + " 입니다.");
            // 파일 쓰기



        }catch(IllegalArgumentException e) {
            System.out.println(e.getMessage());
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


        user = (Client) user;

        System.out.println("안녕하세요. " + user.getName() + " 님의 포인트 정보를 알려드리겠습니다.");

        // 지금 포인트는 -> ? 입니다. 충전하려면 1을 누르세요 , 나가려면 ~  do while, while
        System.out.println(user.getName() + " 님의 포인트는 " +  ((Client) user).getPoint() + " 입니다" );

        while(true){
            System.out.println();
            System.out.println("충전을 원하시면 1을 입력해주시고, 0을 입력하면 메뉴로 돌아갑니다.");

            String inputData = sc.nextLine().trim();

            if(inputData.equals("1")){
                insertPoint((Client) user);
            }

            else if(inputData.equals("0")) {
                return;
            }

            else {
                System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
            }
        }



        //유저 파일 쓰기
    }


    // 포인트 충전
    private static void insertPoint(Client user) {

        System.out.println("원하시는 충전 금액을 입력해주세요. ");
        System.out.println("금액은 10000원 단위로 충전 가능합니다. ");
        int quantity =0;
        while (true) {
            try {
                System.out.print("금액을 입력해주세요 : ");
                quantity = Integer.parseInt(sc.nextLine());
                if (!ValidationQuantity(quantity)) {
                    System.out.println("죄송합니다. 금액은 10000원 단위로 충전가능합니다.");
                    System.out.println("충전을 원하시면 1을 입력해주시고, 0을 입력하면 메뉴로 돌아갑니다.");
                    String choice = sc.nextLine().trim();

                    if (choice.equals("0")){
                        return;
                    }
                    continue;
                }
                break;
            } catch (Exception e) {
                System.out.println("숫자를 입력해주세요");
            }
        }



        int changePoint = user.getPoint() + quantity;

        user.setPoint(changePoint);
        System.out.println("현재 사용자 포인트 : "+changePoint);

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
        if (quantity % 10000 != 0) {
            return false;
        }
        return true;
    }
}


