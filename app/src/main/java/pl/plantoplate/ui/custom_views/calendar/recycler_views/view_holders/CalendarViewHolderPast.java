/*
 * Copyright 2023 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
