package cheema.hardeep.sahibdeep.brotherhood.api;


import java.util.List;

import cheema.hardeep.sahibdeep.brotherhood.models.GenreResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("genre/movie/list")
    Call<GenreResponse> getGenre(@Query("language") String language);
}
