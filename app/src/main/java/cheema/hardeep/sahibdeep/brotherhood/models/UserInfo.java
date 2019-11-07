package cheema.hardeep.sahibdeep.brotherhood.models;

import java.util.ArrayList;
import java.util.List;

public class UserInfo {

    private String name = "";
    private List<Genre> genres = new ArrayList<>();
    private List<Actor> actors = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Actor> getActors() {
        return actors;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }
}
