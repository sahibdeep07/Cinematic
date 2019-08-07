
package cheema.hardeep.sahibdeep.brotherhood.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class Actor {
    @Expose
    private Boolean adult;
    @Expose
    private Long id;
    @Expose
    private String name;
    @Expose
    private Double popularity;
    @SerializedName("profile_path")
    private String profilePath;

    private boolean selected = false;

    public Boolean getAdult() {
        return adult;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }

    public boolean isSelected(){return selected;}

}
