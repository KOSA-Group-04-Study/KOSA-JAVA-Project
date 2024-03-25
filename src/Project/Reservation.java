package Project;

import Project.User.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Reservation {
    String reservationNumber; // 예매번호 -> 중복x , 랜덤 -> 예외처리!? , UUId?

    User user; // 고객

    Movie movie;// 영화

    Seat seat ; // 예매한 좌석

    LocalDateTime date; // 날짜


}
