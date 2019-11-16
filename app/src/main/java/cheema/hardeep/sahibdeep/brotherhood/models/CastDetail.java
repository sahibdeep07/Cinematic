
package cheema.hardeep.sahibdeep.brotherhood.models;

import java.util.List;
import com.google.gson.annotations.Expose;

public class CastDetail {

    @Expose
    private List<Cast> cast;

    @Expose
    private Long id;

    public List<Cast> getCast() {
        return cast;
    }

    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
