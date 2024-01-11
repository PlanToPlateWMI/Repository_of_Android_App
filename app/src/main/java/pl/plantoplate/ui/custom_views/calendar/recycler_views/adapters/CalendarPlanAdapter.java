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
package pl.plantoplate.ui.custom_views.calendar.recycler_views.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import pl.plantoplate.R;
import pl.plantoplate.ui.custom_views.calendar.recycler_views.view_holders.CalendarViewHolder;
import pl.plantoplate.ui.custom_views.calendar.recycler_views.view_holders.CalendarViewHolderFuture;
import pl.plantoplate.ui.custom_views.calendar.recycler_views.view_holders.CalendarViewHolderToday;
import pl.plantoplate.ui.custom_views.calendar.recycler_views.view_holders.CalendarViewHolderPast;
import pl.plantoplate.ui.main.recycler_views.listeners.SetupItemButtons;
import timber.log.Timber;

/**
 * Adapter for the RecyclerView displaying a calendar plan.
 */
public class CalendarPlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private SetupItemButtons listener;
    private List<LocalDate> dates;
    private int selectedPosition = 3;

    public CalendarPlanAdapter() {
        dates = new ArrayList<>();
    }

    public void setUpItemButtons(SetupItemButtons listener) {
        this.listener = listener;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDateList(List<LocalDate> dates) {
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
        throw new IllegalArgumentException("There is no type that matches the type "
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