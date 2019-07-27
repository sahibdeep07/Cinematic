package cheema.hardeep.sahibdeep.brotherhood.models;

import com.google.gson.annotations.Expose;

public class Dates {

    @Expose
    private String maximum;

    @Expose
    private String minimum;

    public String getMaximum() {
        return maximum;
    }

    public String getMinimum() {
        return minimum;
    }
}
