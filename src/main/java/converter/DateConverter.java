package converter;

import com.google.common.collect.ImmutableMap;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

/**
 * Class DateConverter.
 * @author v.ivanov
 * @version 1.0
 * @since 09.09.2020
 */
public class DateConverter {
    /** Map with months names and corresponding numbers.*/
    private static final Map<String, Integer> MONTHS = ImmutableMap.<String, Integer>builder()
            .put("янв", 1)
            .put("фев", 2)
            .put("мар", 3)
            .put("апр", 4)
            .put("май", 5)
            .put("июн", 6)
            .put("июл", 7)
            .put("авг", 8)
            .put("сен", 9)
            .put("окт", 10)
            .put("ноя", 11)
            .put("дек", 12)
            .build();

    /**
     * Method to convert String sql.ru date value into Date format.
     * @param val -  value to convert
     * @return result - converted date
     * @throws ParseException possible exception
     */
    public static Date convertStringToDate(String val) throws ParseException {
        String day = "";
        String month = "";
        String year = "";
        String[] arr = val.split(",");
        if (arr[0].split(" ").length > 1) {
            String[] date = arr[0].trim().split(" ");
            day = date[0].trim();
            month = MONTHS.get(date[1].trim()).toString();
            year = date[2].trim();
        } else {
            LocalDate ld = new Date().toInstant().atZone(ZoneId.of("Europe/Moscow")).toLocalDate();
            month = String.valueOf(ld.getDayOfMonth() + 1);
            year = String.valueOf(ld.getYear());
            if (arr[0].trim().startsWith("сегодня")) {
                day = String.valueOf(ld.getDayOfMonth());
            } else if (arr[0].trim().startsWith("вчера")) {
                day = String.valueOf(ld.minusDays(1).getDayOfMonth());
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd M yy, HH:mm");
        return formatter.parse(
                String.format("%s %s %s, %s", day, month, year, arr[1].trim())
        );
    }
}
