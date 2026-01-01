package recipe_be.exception;

import lombok.Data;
import recipe_be.enums.ErrorCode;

@Data
public class AppException extends RuntimeException {
    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
