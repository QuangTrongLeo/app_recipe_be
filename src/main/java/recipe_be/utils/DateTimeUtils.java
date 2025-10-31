package recipe_be.utils;

import java.time.*;
import java.util.Date;

public class DateTimeUtils {

    private static final ZoneId VIETNAM_ZONE = ZoneId.of("Asia/Ho_Chi_Minh");

    private DateTimeUtils() {
        // Ngăn không cho khởi tạo class tiện ích
    }

    /**
     * Lấy thời gian hiện tại theo múi giờ Việt Nam, dạng java.util.Date.
     */
    public static Date nowVietnamTime() {
        return Date.from(LocalDateTime.now().atZone(VIETNAM_ZONE).toInstant());
    }

    /**
     * Lấy thời gian hiện tại theo múi giờ Việt Nam, dạng LocalDateTime.
     */
    public static LocalDateTime nowLocalDateTime() {
        return LocalDateTime.now(VIETNAM_ZONE);
    }

    /**
     * Chuyển từ Instant sang Date theo múi giờ Việt Nam.
     */
    public static Date toVietnamDate(Instant instant) {
        return Date.from(instant.atZone(VIETNAM_ZONE).toInstant());
    }
}
