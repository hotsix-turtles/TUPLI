package hotsixturtles.tupli.exception;

public class TokenValidFailedException extends RuntimeException {

    public TokenValidFailedException() {
        super("토큰 생성 실패");
    }

    private TokenValidFailedException(String message) {
        super(message);
    }
}
