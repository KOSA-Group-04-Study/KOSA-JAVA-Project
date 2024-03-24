package Project;

import lombok.AllArgsConstructor;
import lombok.ToString;


@ToString
public class Movie {
    String title;
    Integer runningTime;
    Integer price;

    public Movie(String title, Integer runningTime, Integer price) {
        this.title = title;
        this.runningTime = runningTime;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }
}

