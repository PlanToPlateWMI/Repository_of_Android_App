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
package pl.plantoplate.ui.main.calendar.recyclerViews.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.time.LocalDate;
import java.util.ArrayList;
import pl.plantoplate.R;
import pl.plantoplate.ui.main.calendar.recyclerViews.viewHolders.CalendarViewHolder;
import pl.plantoplate.ui.main.calendar.recyclerViews.viewHolders.CalendarViewHolderFuture;
import pl.plantoplate.ui.main.calendar.recyclerViews.viewHolders.CalendarViewHolderPast;
import pl.plantoplate.ui.main.calendar.recyclerViews.viewHolders.CalendarViewHolderToday;
import pl.plantoplate.ui.main.recyclerViews.listeners.SetupItemButtons;
import timber.log.Timber;

public class CalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private SetupItemButtons listener;
    private ArrayList<LocalDate> dates;
    private int selectedPosition = 3;

    public CalendarAdapter(ArrayList<LocalDate> dates) {
        this.dates = dates;
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
        View itemView;
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
        int adapterPosition = holder.getBindingAdapterPosition();
        ((CalendarViewHolder) holder).bind(dates.get(adapterPosition), listener);
        boolean isSelected = adapterPosition == selectedPosition;
        holder.itemView.setSelected(isSelected);

        holder.itemView.setOnClickListener(v -> {
            if (adapterPosition != selectedPosition) {
                int previousSelectedPosition = selectedPosition;
                selectedPosition = adapterPosition;
                notifyItemChanged(previousSelectedPosition);
                notifyItemChanged(selectedPosition);
                listener.setupDateItemClick(v, dates.get(adapterPosition));
            }
        });

        Timber.e("Binded position: %s", position);
    }

    @Override
    public int getItemCount() {
        return dates == null ? 0 : dates.size();
    }
}