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

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.time.LocalDate;
import java.util.ArrayList;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarViewHolder>{
    private final int categoryItemType;
    private SetupItemButtons listener;
    private ArrayList<LocalDate> dates;

    public CalendarAdapter(ArrayList<LocalDate> dates, int calendarItemType) {
        this.dates = dates;
        this.categoryItemType = calendarItemType;
    }

    public void setUpItemButtons(SetupItemButtons listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDateList(ArrayList<LocalDate> dates) {

        this.dates = dates;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(this.categoryItemType, parent, false);
        return new CalendarViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
        holder.bind(dates.get(position));
    }

    @Override
    public int getItemCount() {
        if (dates == null) {
            return 0;
        }
        return dates.size();
    }
}