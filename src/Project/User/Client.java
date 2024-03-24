package Project.User;

import Project.Reservation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


import java.util.List;


@Builder
@Getter

public class Client extends User  {
    private Integer point; // 포인트
    private List<Reservation> reservationList;

    public void setPoint(Integer point) {
        this.point = point;
    }
}
