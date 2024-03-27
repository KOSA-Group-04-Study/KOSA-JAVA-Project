package Project.Exception;

public class ExitException extends RuntimeException {
    public ExitException() {
        super("탈출메시지가 입력되었습니다.");
    }
}
