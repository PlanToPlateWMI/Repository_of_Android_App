package pl.plantoplate.ui.custom_views.calendar;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import pl.plantoplate.ui.custom_views.calendar.recycler_views.adapters.CalendarPlanAdapter;
import pl.plantoplate.ui.custom_views.calendar.recycler_views.adapters.CalendarPlanningAdapter;
import pl.plantoplate.ui.main.recycler_views.listeners.SetupItemButtons;
import pl.plantoplate.utils.DateUtils;

/**
 * Class for the calendar.
 */
public class ShortCalendar{

    private final CalendarStyle calendarStyle;
    private final RecyclerView calendarRecyclerView;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> calendarAdapter;

    /**
     * Constructor
     */
    public ShortCalendar(@NonNull Context context,
                         RecyclerView recyclerView,
                         CalendarStyle calendarStyle) {
        calendarRecyclerView = recyclerView;
        this.calendarStyle = calendarStyle;
        setupRecyclerView(context);
    }

    /**
     * Function to set up recycler view
     */
    public void setupRecyclerView(Context context){
        calendarRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        setCalendarAdapter();
    }

    /**
     * Function to set calendar adapter
     */
    public void setCalendarAdapter(){
        if (calendarStyle.equals(CalendarStyle.PURPLE)){
            calendarAdapter = new CalendarPlanAdapter();
            ((CalendarPlanAdapter) calendarAdapter).setDateList(DateUtils.generateDates(true));
        } else if (calendarStyle.equals(CalendarStyle.BLUE)){
            calendarAdapter = new CalendarPlanningAdapter();
            ((CalendarPlanningAdapter) calendarAdapter).setDateList(DateUtils.generateDates(false));
        }
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    /**
     * Function to set up item buttons
     */
    public void setUpItemButtons(SetupItemButtons setupItemButtons){
        if(calendarStyle.equals(CalendarStyle.PURPLE)){
            ((CalendarPlanAdapter) calendarAdapter).setUpItemButtons(setupItemButtons);
        } else if (calendarStyle.equals(CalendarStyle.BLUE)){
            ((CalendarPlanningAdapter) calendarAdapter).setUpItemButtons(setupItemButtons);
        }
    }
}