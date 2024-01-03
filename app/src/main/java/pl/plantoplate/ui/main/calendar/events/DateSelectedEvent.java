package pl.plantoplate.ui.main.calendar.events;

import java.time.LocalDate;

public class DateSelectedEvent {

    private final LocalDate date;

    public DateSelectedEvent(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }
}
