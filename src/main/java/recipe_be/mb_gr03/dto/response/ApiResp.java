package recipe_be.mb_gr03.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResp<T> {
    private int statusCode;
    private T data;
    private String message;
}

