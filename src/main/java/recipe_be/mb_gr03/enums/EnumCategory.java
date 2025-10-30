package recipe_be.mb_gr03.enums;

import lombok.Getter;

@Getter
public enum EnumCategory {
    BREAKFAST("Breakfast"),
    LUNCH("Lunch"),
    DINNER("Dinner");

    private final String description;

    EnumCategory(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
