package recipe_be.mb_gr03.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum EnumCategory {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner");

    private final String name;

    EnumCategory(String name) {
        this.name = name;
    }

    @JsonValue
    public String toValue() {
        return name;
    }
}
