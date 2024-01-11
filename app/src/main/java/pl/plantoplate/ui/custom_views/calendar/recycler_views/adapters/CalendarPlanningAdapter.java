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
import pl.plantoplate.ui.main.recycler_views.listeners.SetupItemButtons;
import timber.log.Timber;

/**
 * Adapter for the RecyclerView displaying a calendar plan.
 */
public class CalendarPlanningAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private SetupItemButtons listener;
    private List<LocalDate> dates;
    private int selectedPosition = 0;

    public CalendarPlanningAdapter() {
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
        if (position == 0) {
            return 0;
        } else if (position >= 1) {
            return 1;
        }
        return position;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == 0) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_calendar_today_no_highlighting_pop_up_version, parent, false);
            return new CalendarViewHolderToday(itemView);
        }
        else if (viewType == 1) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_calendar_future_no_highlighting_pop_up_version, parent, false);
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