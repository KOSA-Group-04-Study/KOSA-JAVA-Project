package Project.User;

import Project.Manager.FileDataManager;
import Project.Reservation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


import java.util.ArrayList;
import java.util.List;


@Getter
//@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Client extends User  {
    private Integer point; // 포인트
    private List<Reservation> reservationList;

    public Client(String email, String password, String name, String phoneNumber, boolean isAdmin, Integer point, List<Reservation> reservationList) {
        super(email, password, name, phoneNumber, isAdmin);
        this.point = point;
        this.reservationList = reservationList;
    }



    public void setPoint(Integer point) {
        this.point = point;
    }
}
