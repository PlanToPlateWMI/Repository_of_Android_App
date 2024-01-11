package pl.plantoplate.ui.custom_views.calendar.recycler_views.view_holders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.time.LocalDate;

import pl.plantoplate.R;
import pl.plantoplate.ui.main.recycler_views.listeners.SetupItemButtons;

/**
 * View holder for the past dates in the calendar plan RecyclerView.
 */
public class CalendarViewHolderPast extends CalendarViewHolder{

    private final TextView dateDayName;
    private final TextView dateNumber;
    private final LinearLayout layoutPastDate;

    public CalendarViewHolderPast(View itemView) {
        super(itemView);
        dateDayName = itemView.findViewById(R.id.dwieLiteryDniaTygodniaPastNH);
        dateNumber = itemView.findViewById(R.id.liczbaDatyPastNH);
        layoutPastDate = itemView.findViewById(R.id.layoutCalendarPastNoHighlighting);
    }

    public void bind(LocalDate date, SetupItemButtons listener) {
        String dayName = getDayName(date.getDayOfWeek().getValue());
        int dayOfMonth = date.getDayOfMonth();
        dateDayName.setText(dayName);
        dateNumber.setText(String.valueOf(dayOfMonth));
        layoutPastDate.setOnClickListener(v -> listener.setupDateItemClick(v, date));
    }
}
