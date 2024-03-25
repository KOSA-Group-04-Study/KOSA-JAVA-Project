package Project.User;

import Project.Reservation;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;


//@Builder
@Getter
@Setter
//@AllArgsConstructor
public class Client extends User implements Serializable{

    private Integer point; // 포인트
    private List<Reservation> reservationList;

    public Client(String email, String password, String name, String phoneNumber, boolean isAdmin, Integer point, List<Reservation> reservationList) {
        super(email, password, name, phoneNumber, isAdmin);
        this.point = point;
        this.reservationList = new LinkedList<>();
    }

    /*
    public Client(String email, String password, String name, String phoneNumber, boolean isAdmin, Integer point) {
        super.email = email;
        super.password = password;
        super.name = name;
        super.phoneNumber = phoneNumber;
        super.isAdmin = isAdmin;
        this.point = point;
        this.reservationList = new LinkedList<>();
    }

     */
}
