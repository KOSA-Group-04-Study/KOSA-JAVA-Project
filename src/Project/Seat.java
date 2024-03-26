package Project;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class Seat implements Serializable {
    Integer row;
    Integer col;
}
