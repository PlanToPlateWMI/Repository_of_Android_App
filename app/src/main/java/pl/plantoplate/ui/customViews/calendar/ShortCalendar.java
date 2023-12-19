package pl.plantoplate.ui.customViews.calendar;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.plantoplate.ui.customViews.calendar.recyclerViews.adapters.CalendarPlanAdapter;
import pl.plantoplate.ui.customViews.calendar.recyclerViews.adapters.CalendarPlanningAdapter;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;
import pl.plantoplate.utils.DateUtils;

public class ShortCalendar{

    private CalendarStyle calendarStyle;
    private RecyclerView calendarRecyclerView;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> calendarAdapter;

    /**
     * @param context
     * @param recyclerView
     * @param calendarStyle
     */
    public ShortCalendar(@NonNull Context context,
                         RecyclerView recyclerView,
                         CalendarStyle calendarStyle) {
        calendarRecyclerView = recyclerView;
        this.calendarStyle = calendarStyle;
        setupRecyclerView(context);
    }

    /**
     * @param context
     */
    public void setupRecyclerView(Context context){
        calendarRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        setCalendarAdapter();
    }

    /**
     * @param calendarStyle
     */
    public void setCalendarAdapter(){
        switch (calendarStyle){
            case PURPLE:
                calendarAdapter = new CalendarPlanAdapter();
                ((CalendarPlanAdapter) calendarAdapter).setDateList(DateUtils.generateDates(true));
                break;
            case BLUE:
                calendarAdapter = new CalendarPlanningAdapter();
                ((CalendarPlanningAdapter) calendarAdapter).setDateList(DateUtils.generateDates(false));
                break;
        }
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    /**
     * @param setupItemButtons
     */
    public void setUpItemButtons(SetupItemButtons setupItemButtons){
        switch (calendarStyle){
            case PURPLE:
                ((CalendarPlanAdapter) calendarAdapter).setUpItemButtons(setupItemButtons);
                break;
            case BLUE:
                ((CalendarPlanningAdapter) calendarAdapter).setUpItemButtons(setupItemButtons);
                break;
        }
    }
}