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
import java.util.Timer;

import pl.plantoplate.R;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;
import timber.log.Timber;

public class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    //private final int categoryItemType;
    private SetupItemButtons listener;
    private ArrayList<LocalDate> dates;

    public CalendarAdapter(ArrayList<LocalDate> dates) {
        this.dates = dates;
        //this.categoryItemType = calendarItemType;
    }

    public void setUpItemButtons(SetupItemButtons listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDateList(ArrayList<LocalDate> dates) {

        this.dates = dates;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Timber.e("position: %s", position);
        if (position >= 0 && position <= 2) {
            return 0;
        } else if (position == 3) {
            return 1;
        } else if (position >= 4) {
            return 2;
        }
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = null;
        if (viewType == 0) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_calendar_past_no_highlighting, parent, false);
            return new CalendarViewHolderPast(itemView);
        }
        else if (viewType == 1) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_calendar_today_no_highlighting, parent, false);
            return new CalendarViewHolderToday(itemView);
        }
        else if (viewType == 2) {
            itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendar_future_no_highlighting, parent, false);
            return new CalendarViewHolderFuture(itemView);
        }
        throw new RuntimeException("There is no type that matches the type "
                + viewType +
                " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CalendarViewHolderPast) {
            ((CalendarViewHolderPast) holder).bind(dates.get(position));
        }
        else if (holder instanceof CalendarViewHolderToday) {
            ((CalendarViewHolderToday) holder).bind(dates.get(position));
        }
        else if (holder instanceof CalendarViewHolderFuture) {
            ((CalendarViewHolderFuture) holder).bind(dates.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (dates == null) {
            return 0;
        }
        return dates.size();
    }
}