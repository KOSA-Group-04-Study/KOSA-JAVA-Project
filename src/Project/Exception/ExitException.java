package Project.Exception;

public class ExitException extends RuntimeException {
    public ExitException() {
        super("\t\t\t----- 메뉴로 이동합니다. -----");
    }
}
