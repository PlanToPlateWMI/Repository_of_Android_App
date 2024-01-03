package pl.plantoplate.ui.custom_views.calendar.recycler_views.view_holders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.time.LocalDate;
import pl.plantoplate.R;
import pl.plantoplate.ui.main.recycler_views.listeners.SetupItemButtons;

public class CalendarViewHolderToday extends CalendarViewHolder{

    private final TextView dateDayName;
    private final TextView dateNumber;
    private final LinearLayout layoutTodayDate;

    public CalendarViewHolderToday(View itemView) {
        super(itemView);

        dateDayName = itemView.findViewById(R.id.dwieLiteryDniaTygodniaTodNH);
        dateNumber = itemView.findViewById(R.id.liczbaDatyTodNH);
        layoutTodayDate = itemView.findViewById(R.id.layoutCalendarTodayNoHighlighting);
    }

    public void bind(LocalDate date, SetupItemButtons listener) {
        String dayName = getDayName(date.getDayOfWeek().getValue());
        int dayOfMonth = date.getDayOfMonth();
        dateDayName.setText(dayName);
        dateNumber.setText(String.valueOf(dayOfMonth));
        layoutTodayDate.setOnClickListener(v -> listener.setupDateItemClick(v, date));
        layoutTodayDate.setSelected(true);
    }
}