package pl.plantoplate.utils;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import java.time.LocalDate;
import java.util.List;

public class DateUtilsTest {

    @Test
    public void testFormatPolishDate() {
        // Test formatting a specific LocalDate
        LocalDate testDate = LocalDate.of(2023, 5, 15);
        String formattedDate = DateUtils.formatPolishDate(testDate);

        assertEquals("Maj 15, 2023", formattedDate);
    }

    @Test
    public void testGenerateDates() {
        // Test generating dates without including past dates
        List<LocalDate> dateList = DateUtils.generateDates(false);

        assertEquals(8, dateList.size());

        // Test if the dates are consecutive
        for (int i = 0; i < dateList.size() - 1; i++) {
            LocalDate current = dateList.get(i);
            LocalDate next = dateList.get(i + 1);
            assertEquals(current.plusDays(1), next);
        }

        // Test generating dates including past dates
        List<LocalDate> dateListWithPast = DateUtils.generateDates(true);

        assertEquals(11, dateListWithPast.size());
        // Assuming today is the reference point for the test
        assertEquals(LocalDate.now().minusDays(3), dateListWithPast.get(0));
    }
}
