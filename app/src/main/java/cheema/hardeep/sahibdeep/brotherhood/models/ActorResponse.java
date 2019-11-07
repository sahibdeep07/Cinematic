
package cheema.hardeep.sahibdeep.brotherhood.models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ActorResponse {

    @Expose
    private Long page;

    @Expose
    private List<Actor> results = new ArrayList<>();

    @SerializedName("total_pages")
    private Long totalPages;

    @SerializedName("total_results")
    private Long totalResults;

    public Long getPage() {
        return page;
    }

    public void setPage(Long page) {
        this.page = page;
    }

    public List<Actor> getActors() {
        return results;
    }

    public void setActors(List<Actor> actors) {
        this.results = actors;
    }

    public Long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Long totalPages) {
        this.totalPages = totalPages;
    }

    public Long getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Long totalResults) {
        this.totalResults = totalResults;
    }

}
