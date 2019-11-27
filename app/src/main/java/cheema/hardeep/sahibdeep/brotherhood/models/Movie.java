package cheema.hardeep.sahibdeep.brotherhood.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Movie implements Comparable<Movie> {

    @Expose
    private Boolean adult;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("genre_ids")
    private List<Long> genreIds;

    @Expose
    private Long id;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("original_title")
    private String originalTitle;

    @Expose
    private String overview;

    @Expose
    private Double popularity;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("release_date")
    private String releaseDate;

    @Expose
    private String title;

    @Expose
    private Boolean video;

    @SerializedName("vote_average")
    private Double voteAverage;

    @SerializedName("vote_count")
    private Long voteCount;

    private String genreNames;

    private CastDetail castDetails;

    public Boolean getAdult() {
        return adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public List<Long> getGenreIds() {
        return genreIds;
    }

    public Long getId() {
        return id;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public Double getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getVideo() {
        return video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Long getVoteCount() {
        return voteCount;
    }

    public String getGenreNames() {
        return genreNames;
    }

    public CastDetail getCastDetails() {
        return castDetails;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setGenreIds(List<Genre> genres) {
        this.genreIds = new ArrayList<>();
        for (Genre genre : genres) {
            this.genreIds.add(genre.getId());
        }
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setGenreNames(String genreNames) {
        this.genreNames = genreNames;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int compareTo(Movie movie) {
        if (getReleaseDate() == null || movie.getReleaseDate() == null) {
            return 0;
        }
        return getReleaseDate().compareTo(movie.getReleaseDate());
    }

    /**
     * Returning itself of RxJava Operation
     */
    public Movie setCastDetails(CastDetail castDetails) {
        this.castDetails = castDetails;
        return this;
    }
}
