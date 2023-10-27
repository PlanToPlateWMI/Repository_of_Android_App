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
package pl.plantoplate.ui.main.calendar.recyclerViews;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.time.LocalDate;
import pl.plantoplate.R;
public class CalendarViewHolder extends RecyclerView.ViewHolder{

    private final TextView dateDayName;
    private final TextView dateNumber;
    private final LinearLayout layout;

    public CalendarViewHolder(View itemView) {
        super(itemView);
        dateDayName = itemView.findViewById(R.id.dwieLiteryDniaTygodnia);
        dateNumber = itemView.findViewById(R.id.liczbaDaty);
        layout = itemView.findViewById(R.id.layoutCalendarFutureHighlighting);
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