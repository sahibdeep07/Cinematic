
package cheema.hardeep.sahibdeep.brotherhood.models;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;

public class GenreResponse {

    @Expose
    private List<Genre> genres = new ArrayList<>();

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

}
