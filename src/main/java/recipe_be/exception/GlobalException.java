package recipe_be.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import recipe_be.dto.response.ApiResponse;
import recipe_be.enums.ErrorCode;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> appException(AppException e) {
        ApiResponse apiResponse = new ApiResponse();
        ErrorCode errorCode = e.getErrorCode();
        apiResponse.setStatus(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());
        return ResponseEntity.status(errorCode.getCode()).body(apiResponse);
    }

    // Runtime Exception

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse> runtimeException(RuntimeException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(e.getMessage());
        apiResponse.setStatus(500);
        return ResponseEntity.status(500).body(apiResponse);
    }

}
