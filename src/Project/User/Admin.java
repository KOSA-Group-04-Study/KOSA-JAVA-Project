package Project.User;

import Project.FilesIO.FileDataManager;
import Project.MovieSchedule.MovieScheduleManager;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class Admin extends User implements Serializable{
    public Admin(String email, String password, String name, String phoneNumber, boolean isAdmin) {
        super(email, password, name, phoneNumber, isAdmin);
    }
}
