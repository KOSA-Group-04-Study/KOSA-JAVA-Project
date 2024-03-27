package Project.Reservation;

public enum ScreeningTime {
    time_09("9시"),time_12("12시"),time_18("18시");

    String time;
    ScreeningTime(String time) {
        this.time =time;
    }
    public String getTime() {
        return this.time;
    }

}
