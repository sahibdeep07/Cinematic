package cheema.hardeep.sahibdeep.brotherhood.models;

import java.util.ArrayList;
import java.util.List;

public class UserInfo {

    private String name = "";
    private List<String> genres = new ArrayList<>();
    private List<String> actors = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setActors(List<String> actors) {
        this.actors = actors;
    }
}
