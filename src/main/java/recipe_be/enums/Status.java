package recipe_be.enums;

import lombok.Getter;

@Getter
public enum Status {
    ACTIVE, INACTIVE,
    
    PENDING, SUCCESS, FAILED, CANCELED
}
