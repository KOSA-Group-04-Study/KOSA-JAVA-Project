package Project;

import Project.User.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Getter
@AllArgsConstructor
public class Reservation {
    private LocalDateTime reservationDate;  //영화 예매 날짜
    private String reservationNumber;       //예매번호 -> [(날짜16진수)-(상영관, 좌석정보)-(랜덤문자)-(랜덤문자)]
    private User user;                      //고객
    private Movie movie;                    //영화
    private LocalDate movieDate;            //영화 날짜
    private Integer[] theater;              //예매한 영화의 상영관
    private Seat seat ;                     //예매 좌석
    private Integer moviePrice;             //예매 가격 추가 필요 (이후 예매 취소시 필요)


    @Override
    public String toString() {
        String[] theaters = {"1관", "2관", "3관"};
        ScreeningTime[] times = {ScreeningTime.time_09, ScreeningTime.time_12, ScreeningTime.time_18};
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return String.format("%s\t\t%s\t%s\t%s\t%s %s %s\t%s",
                reservationDate.format(formatter),
                "예매번호: [" + reservationNumber + "]",
                "이름: [" + user.getName() + "]",
                "영화상영정보: [" + movieDate +" "+ times[this.theater[1]],
                theaters[this.theater[0]],
                ((char) (seat.row + 65)) + "열",
                (seat.col + 1) + "번" + "]",
                "영화제목: [" + movie.getTitle() + "]");
    }
}