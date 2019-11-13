package cheema.hardeep.sahibdeep.brotherhood.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.models.Movie;
import cheema.hardeep.sahibdeep.brotherhood.models.TopRated;
import cheema.hardeep.sahibdeep.brotherhood.models.UpcomingData;
import cheema.hardeep.sahibdeep.brotherhood.utils.Utilities;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.ROUNDED_CORNER_UPCOMING;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.SIZE_342;

public class YourGenresAdapter extends RecyclerView.Adapter<YourGenresAdapter.YourGenreViewHolder> {

    private List<Movie> movies = new ArrayList<>();

    public void updateDataSet(List<Movie> adapterData) {
        movies.clear();
        movies.addAll(adapterData);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public YourGenreViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.upcoming_item, viewGroup, false);
        return new YourGenreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YourGenreViewHolder yourGenreViewHolder, int i) {
        Movie movie = movies.get(i);
        yourGenreViewHolder.movieName.setText(movie.getTitle());
        yourGenreViewHolder.movieRating.setText(String.valueOf(movie.getVoteAverage()));
        loadImageWithGlide(movie.getBackdropPath(), yourGenreViewHolder.movieImage);
    }

    private void loadImageWithGlide(String url, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterCrop(), new RoundedCorners(ROUNDED_CORNER_UPCOMING));
        Glide.with(imageView.getContext())
                .load(Utilities.createImageUrl(url, SIZE_342))
                .apply(requestOptions)
                .placeholder(R.drawable.curved_bottom_right)
                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class YourGenreViewHolder extends RecyclerView.ViewHolder {
        ImageView movieImage;
        TextView movieRating;
        TextView movieName;

        public YourGenreViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.upcomingMovieImage);
            movieRating = itemView.findViewById(R.id.upcomingMovieRating);
            movieName = itemView.findViewById(R.id.upcomingMovieName);
        }
    }
}
