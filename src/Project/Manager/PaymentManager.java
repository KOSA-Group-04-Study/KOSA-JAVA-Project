package Project.Manager;

import Project.User.Client;
import Project.User.User;

import java.util.Scanner;

public final class PaymentManager {
    //메소드는 스태틱
    static Scanner sc = new Scanner(System.in);



    public static boolean payPoint(User user,Integer price) {
        // 포인트 결제 -> 포인트 감소



        // 취소시 포인트 반환 -> 포인트 증가

        //try catch finally
        return false;
    }


    public static void pointManage(User user) {  //포인트 조회 + 충전선택


        user = (Client) user;
        System.out.println("안녕하세요. " + user.getName() + " 님의 포인트 정보를 알려드리겠습니다.");

        // 지금 포인트는 -> ? 입니다. 충전하려면 1을 누르세요 , 나가려면 ~  do while, while
        System.out.println(user.getName() + " 님의 포인트는 " +  ((Client) user).getPoint() + " 입니다" );

        while(true){
            boolean check = true;
            System.out.println();
            System.out.println("충전을 원하시면 1을 입력해주시고, 0을 입력하면 메뉴로 돌아갑니다.");

            String inputData = sc.nextLine().trim();

            if(inputData.equals("1")){
                insertPoint((Client) user);
            }

            if(inputData.equals("0")) {
                return;
            }

            else{
                System.out.println("잘못된 입력입니다. 다시 입력해주세요.");
            }
        }



        //유저 파일 쓰기
    }


    // 포인트 충전
    private static void insertPoint(Client user) {

        System.out.println("원하시는 충전 금액을 입력해주세요. ");
        System.out.println("금액은 10000원 단위로 충전 가능합니다. ");

        int quantity = Integer.parseInt(sc.nextLine());
        if (!ValidationQuantity(quantity)) {
            System.out.print("죄송합니다. 금액은 10000원 단위로 충전가능합니다.");
        }
        // 유저 금액에 대한 setter가 필요할듯?
        user.

    }

    private static boolean ValidationQuantity(int quantity) {
        if (quantity % 10000 != 0) {
            return false;
        }
        return true;
    }
}


