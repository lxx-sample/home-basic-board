package li.dongpo.home.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author dongpo.li
 * @date 2022/2/15
 */
public class DateTimeUtils {

    public static LocalDate parseLocalDate(String s) {
        return LocalDate.parse(s, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}
