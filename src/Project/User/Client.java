package Project.User;

import Project.Reservation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;


@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Client extends User  {
    private Integer point; // 포인트
    private List<Reservation> reservationList;

    public Client(String email, String password, String name, String phoneNumber, boolean isAdmin, Integer point) {
        super.email = email;
        super.password = password;
        super.name = name;
        super.phoneNumber = phoneNumber;
        super.isAdmin = isAdmin;
        this.point = point;
        this.reservationList = new ArrayList<>();
    }
}
