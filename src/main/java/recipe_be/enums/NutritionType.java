package recipe_be.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NutritionType {
    // === Macronutrients ===
    CALORIES,
    PROTEIN,
    FAT,
    CARBOHYDRATES,
    FIBER,
    SUGAR,
    WATER,
    ASH,

    // === Fat types ===
    SATURATED_FAT,
    MONOUNSATURATED_FAT,
    POLYUNSATURATED_FAT,
    TRANS_FAT,
    OMEGA_3_FATTY_ACIDS,
    OMEGA_6_FATTY_ACIDS,

    // === Cholesterol & Sterols ===
    CHOLESTEROL,
    PHYTOSTEROLS,

    // === Vitamins ===
    VITAMIN_A,
    VITAMIN_B1,
    VITAMIN_B2,
    VITAMIN_B3,
    VITAMIN_B5,
    VITAMIN_B6,
    VITAMIN_B7,
    VITAMIN_B9,
    VITAMIN_B12,
    VITAMIN_C,
    VITAMIN_D,
    VITAMIN_E,
    VITAMIN_K,

    // === Minerals ===
    CALCIUM,
    IRON,
    MAGNESIUM,
    PHOSPHORUS,
    POTASSIUM,
    SODIUM,
    ZINC,
    COPPER,
    MANGANESE,
    SELENIUM,
    IODINE,
    CHLORIDE,
    FLUORIDE,
    CHROMIUM,
    MOLYBDENUM,

    // === Amino acids ===
    ALANINE,
    ARGININE,
    ASPARAGINE,
    ASPARTIC_ACID,
    CYSTEINE,
    GLUTAMIC_ACID,
    GLUTAMINE,
    GLYCINE,
    HISTIDINE,
    ISOLEUCINE,
    LEUCINE,
    LYSINE,
    METHIONINE,
    PHENYLALANINE,
    PROLINE,
    SERINE,
    THREONINE,
    TRYPTOPHAN,
    TYROSINE,
    VALINE,

    // === Sugars ===
    FRUCTOSE,
    GLUCOSE,
    SUCROSE,
    LACTOSE,
    MALTOSE,
    GALACTOSE,

    // === Organic acids ===
    CITRIC_ACID,
    LACTIC_ACID,
    MALIC_ACID,
    OXALIC_ACID,
    TARTARIC_ACID,

    // === Phytochemicals ===
    BETA_CAROTENE,
    ALPHA_CAROTENE,
    LYCOPENE,
    LUTEIN,
    ZEAXANTHIN,
    CHOLINE,
    INOSITOL,
    TAURINE,
    CREATINE,
    COENZYME_Q10,

    // === Other Compounds ===
    CAFFEINE,
    ALCOHOL,
    THEOBROMINE,
    POLYPHENOLS,
    FLAVONOIDS,
    ANTHOCYANINS,
    TANNINS,
    LIGNANS,

    // === Trace elements & extras ===
    NITRATE,
    SULFUR,
    VANADIUM,
    COBALT,
    NICKEL;

    // --- JSON serialization (gửi ra client REST API) ---
    @JsonValue
    public String toValue() {
        return this.name(); // giữ nguyên tên enum (VD: "PROTEIN")
    }

    // --- JSON & Mongo deserialization (đọc từ client hoặc DB) ---
    @JsonCreator
    public static NutritionType from(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return NutritionType.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // hoặc có thể return NutritionType.CALORIES làm default
        }
    }
}
