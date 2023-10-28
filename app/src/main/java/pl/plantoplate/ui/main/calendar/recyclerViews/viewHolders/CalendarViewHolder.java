package pl.plantoplate.ui.main.calendar.recyclerViews.viewHolders;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;

import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;

public abstract class CalendarViewHolder extends RecyclerView.ViewHolder{

    public CalendarViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bind(LocalDate date, SetupItemButtons listener);

    /**
     * Returns the day name for the provided day of the week.
     * @param dayOfWeek The day of the week (1-7)
     * @return The short name of the day of the week
     */
    public String getDayName(int dayOfWeek) {
        String[] dayNames = new String[]{"Pn", "Wt", "Śr", "Cz", "Pt", "Sb", "Nd"};
        int adjustedIndex = (dayOfWeek + 6) % 7;

        return dayNames[adjustedIndex];
    }
}
