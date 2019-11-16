package cheema.hardeep.sahibdeep.brotherhood.models;

import android.util.Log;

import com.google.gson.annotations.Expose;

public class Genre {

    @Expose
    private Long id;
    @Expose
    private String name;

    private int icon;

    private boolean selected = false;

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

    public int getIcon() {
        return icon;
    }

    public void setIcon() {
        this.icon = GenreEnum.getEnum(name).icon;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}