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
package pl.plantoplate.ui.main.calendar.recyclerViews.viewHolders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.time.LocalDate;
import pl.plantoplate.R;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;

public class CalendarViewHolderFuture extends CalendarViewHolder{

    private final TextView dateDayName;
    private final TextView dateNumber;
    private final LinearLayout layoutFutureDate;

    public CalendarViewHolderFuture(View itemView) {
        super(itemView);
        dateDayName = itemView.findViewById(R.id.dwieLiteryDniaTygodniaFutNH);
        dateNumber = itemView.findViewById(R.id.liczbaDatyFutNH);
        layoutFutureDate = itemView.findViewById(R.id.layoutCalendarFutureNoHighlighting);
    }

    public void bind(LocalDate date, SetupItemButtons listener) {
        String dayName = getDayName(date.getDayOfWeek().getValue());
        int dayOfMonth = date.getDayOfMonth();
        dateDayName.setText(dayName);
        dateNumber.setText(String.valueOf(dayOfMonth));
        layoutFutureDate.setOnClickListener(v -> listener.setupDateItemClick(v, date));
    }
}