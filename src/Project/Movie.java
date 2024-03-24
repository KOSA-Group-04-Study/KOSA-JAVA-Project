package Project;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
@Getter

public class Movie implements Serializable {
    String title;
    Integer runningTime;
    Integer price;

    public Movie(String title, Integer runningTime, Integer price) {
        this.title = title;
        this.runningTime = runningTime;
        this.price = price;
    }
}

