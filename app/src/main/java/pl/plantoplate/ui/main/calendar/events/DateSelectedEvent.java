package pl.plantoplate.ui.main.calendar.events;

import java.time.LocalDate;

/**
 * Event class representing the selection of a date.
 */
public class DateSelectedEvent {

    private final LocalDate date;

    public DateSelectedEvent(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }
}
