package cheema.hardeep.sahibdeep.brotherhood.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Upcoming {

    @Expose
    private Dates dates;

    @Expose
    private Long page;

    @Expose
    private List<Movie> results;

    @SerializedName("total_pages")
    private Long totalPages;

    @SerializedName("total_results")
    private Long totalResults;

    public Dates getDates() {
        return dates;
    }

    public Long getPage() {
        return page;
    }

    public List<Movie> getResults() {
        return results;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public Long getTotalResults() {
        return totalResults;
    }
}
