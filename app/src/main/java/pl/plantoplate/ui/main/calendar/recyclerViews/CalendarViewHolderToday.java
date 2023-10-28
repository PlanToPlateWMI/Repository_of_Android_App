package pl.plantoplate.ui.main.calendar.recyclerViews;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;

import pl.plantoplate.R;

public class CalendarViewHolderToday extends RecyclerView.ViewHolder{

    private final TextView dateDayName;
    private final TextView dateNumber;
    private final LinearLayout layout_today;

    public CalendarViewHolderToday(View itemView) {
        super(itemView);

        dateDayName = itemView.findViewById(R.id.dwieLiteryDniaTygodniaTodNH);
        dateNumber = itemView.findViewById(R.id.liczbaDatyTodNH);
        layout_today = itemView.findViewById(R.id.layoutCalendarTodayNoHighlighting);
    }

    public void bind(LocalDate date) {
        String dayName = getDayName(date.getDayOfWeek().getValue());
        int dayOfMonth = date.getDayOfMonth();
        dateDayName.setText(dayName);
        dateNumber.setText(String.valueOf(dayOfMonth));
    }

    /**
     * Returns the day name for the provided day of the week.
     * @param dayOfWeek The day of the week (1-7)
     * @return The short name of the day of the week
     */
    private String getDayName(int dayOfWeek) {
        String[] dayNames = new String[]{"Pn", "Wt", "Śr", "Cz", "Pt", "Sb", "Nd"};
        int adjustedIndex = (dayOfWeek + 6) % 7;

        return dayNames[adjustedIndex];
    }
}
