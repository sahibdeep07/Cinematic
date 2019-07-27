package cheema.hardeep.sahibdeep.brotherhood.models;

import java.util.ArrayList;
import java.util.List;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.EMPTY;

public class UpcomingData {

    private String date = EMPTY;
    private List<Movie> movies = new ArrayList<>();

    public UpcomingData() {}

    public UpcomingData(String date, List<Movie> movies) {
        this.date = date;
        this.movies = movies;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
}
