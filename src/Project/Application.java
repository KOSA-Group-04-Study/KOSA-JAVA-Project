package Project;

import Project.Manager.MovieScheduleManager;

import java.util.HashMap;
import java.util.Map;

public class Application {
    public static void main(String[] args) {

        Map<String, Map<Movie,Schedule[][]>> data = new HashMap<>();

        Map<Movie, Schedule[][]> data1 = new HashMap<>();

        Map<String, AdminSchedule[][]> data3 = new HashMap<>();

        AdminSchedule[][] adminSchedules = new AdminSchedule[3][3];

        Movie 파묘 = new Movie("파묘", 60, 15000);
        Movie 엘리멘탈 = new Movie("엘리멘탈", 60, 25000);
        Movie 윙카 = new Movie("윙카", 60, 35000);
        Movie 서울의봄 = new Movie("서울의봄", 60, 45000);


        Schedule[][] schedules = new Schedule[3][3]; //엘리멘탈
        for (int i = 0; i < 3; i++) {
            schedules[i] = new Schedule[3];
        }
        /*
        더미데이터 -> 엘리멘탈은 1,3관에서 9시에 밖에안함
         */
        schedules[0][0] = new Schedule(new Seat[5][5]); // 1관 9시
//        adminSchedules[0][0] = new AdminSchedule(엘리멘탈, "09시",schedules[0][0]);
        schedules[2][0] = new Schedule(new Seat[5][5]); // 3관 9시
//        adminSchedules[2][0] = new AdminSchedule(엘리멘탈, "09시",schedules[2][0]);


        Schedule[][] schedules2 = new Schedule[3][3]; // 윙카
        for (int i = 0; i < 3; i++) {
            schedules2[i] = new Schedule[3];
        }
        /*
        윙카는 1,3관에서 12시에 밖에안함
         */
        schedules2[0][1] = new Schedule(new Seat[5][5]); // 1관 12시
//        adminSchedules[0][1] = new AdminSchedule(윙카, "12시",schedules2[0][1]);
        schedules2[2][1] = new Schedule(new Seat[5][5]); // 3관 12시
//        adminSchedules[2][1] = new AdminSchedule(윙카, "12시",schedules2[2][1]);

        Schedule[][] schedules3 = new Schedule[3][3]; // 파묘
        for (int i = 0; i < 3; i++) {
            schedules3[i] = new Schedule[3];
        }
        /*
        파묘는 1관 18시 , 2관 9,12시에 함
         */
        schedules3[0][2] = new Schedule(new Seat[5][5]); // 1관 18시
//        adminSchedules[0][2] = new AdminSchedule(파묘, "18시",schedules3[0][2]);
        schedules3[1][0] = new Schedule(new Seat[5][5]); // 2관 9시
//        adminSchedules[1][0] = new AdminSchedule(파묘, "9시",schedules3[1][0]);
//        schedules3[1][1] = new Schedule(new Seat[5][5]); // 2관 12시
//        scheduleTemps[1][1] = new ScheduleTemp(파묘, "12시");

        Schedule[][] schedules4 = new Schedule[3][3]; //서울의봄
        for (int i = 0; i < 3; i++) {
            schedules4[i] = new Schedule[3];
        }
        /*
        서울의 봄은 2관 18시 , 3관 18시에 함
         */
        schedules4[1][2] = new Schedule(new Seat[5][5]); // 2관 18시
//        adminSchedules[1][2] = new AdminSchedule(서울의봄, "18시",schedules4[1][2]);
        schedules4[2][2] = new Schedule(new Seat[5][5]); // 3관 18시
//        adminSchedules[2][2] = new AdminSchedule(서울의봄, "18시",schedules4[2][2]);


        data1.put(엘리멘탈,schedules);
        data1.put(윙카,schedules2);
        data1.put(파묘,schedules3);
        data1.put(서울의봄,schedules4);

        data.put("2024-03-22", data1);
        data3.put("2024-03-22", adminSchedules);

        Cinema cinema = new Cinema();
        cinema.setMovieSchedule(data);
        cinema.setMovieScheduleTemp(data3);

//        MovieScheduleManager.test("2024-03-22",cinema);
//        MovieScheduleManager.test2("2024-03-22",cinema);
//        MovieScheduleManager.test3("2024-03-22",cinema);


    }
}