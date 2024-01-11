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
