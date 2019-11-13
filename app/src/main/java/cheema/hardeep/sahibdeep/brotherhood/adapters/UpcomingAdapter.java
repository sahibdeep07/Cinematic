package cheema.hardeep.sahibdeep.brotherhood.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.models.UpcomingData;
import cheema.hardeep.sahibdeep.brotherhood.utils.Utilities;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.TOMORROW;


public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.UpcomingViewHolder> {

    List<UpcomingData> dataSet = new ArrayList<>();

    public void updateDataSet(List<UpcomingData> adapterData) {
        dataSet.clear();
        dataSet.addAll(adapterData);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.upcoming_item, viewGroup, false);
        return new UpcomingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingViewHolder upcomingViewHolder, int i) {
        UpcomingData upcomingData = dataSet.get(i);
        upcomingViewHolder.date.setText(handleDate(upcomingData));
        upcomingViewHolder.upcomingMovieAdapter.updateDataSet(upcomingData.getMovies());
    }

    private String handleDate(UpcomingData upcomingData) {
        return Utilities.isDateTomorrow(upcomingData.getDate()) ? TOMORROW : Utilities.generateDisplayDate(upcomingData.getDate());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class UpcomingViewHolder extends RecyclerView.ViewHolder {

        RecyclerView horizontalRecyclerView;
        TextView date;
        UpcomingMovieAdapter upcomingMovieAdapter;

        public UpcomingViewHolder(View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.upcomingDate);
            horizontalRecyclerView = itemView.findViewById(R.id.upcomingHorizontalRecyclerView);
            horizontalRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            upcomingMovieAdapter = new UpcomingMovieAdapter();
            horizontalRecyclerView.setAdapter(upcomingMovieAdapter);
        }
    }
}
