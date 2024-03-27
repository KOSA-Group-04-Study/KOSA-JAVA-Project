package Project;

import Project.FilesIO.FileDataManager;
import Project.FilesIO.TestDataGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application {
    public static void main(String[] args) {

        TestDataGenerator.testDataGenerate();
        Cinema cinema = new Cinema();
        cinema.run();
    }

}