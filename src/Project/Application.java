package Project;

import Project.FilesIO.FileDataManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application {
    public static void main(String[] args) {


        Cinema cinema = new Cinema();

        //cinema.setMovieSchedule(data);

        cinema.run();
    }


    // 영화 및 스케줄 데이터를 생성하는 메소드
    private static Map<Movie, Schedule[][]> createMovieSchedule() {
        Map<Movie, Schedule[][]> movieSchedule = new HashMap<>();
        List<Movie> movieList = FileDataManager.readMoviesFromFile();



        return movieSchedule;
    }


}