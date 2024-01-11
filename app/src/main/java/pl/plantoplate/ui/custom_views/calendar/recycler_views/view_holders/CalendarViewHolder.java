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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;

import pl.plantoplate.ui.main.recycler_views.listeners.SetupItemButtons;

/**
 * Abstract class for the view holders used in the calendar plan RecyclerView.
 */
public abstract class CalendarViewHolder extends RecyclerView.ViewHolder{

    protected CalendarViewHolder(@NonNull View itemView) {
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
