package cheema.hardeep.sahibdeep.brotherhood.api;

import cheema.hardeep.sahibdeep.brotherhood.models.ActorResponse;
import cheema.hardeep.sahibdeep.brotherhood.models.GenreResponse;
import cheema.hardeep.sahibdeep.brotherhood.models.Upcoming;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("genre/movie/list")
    Call<GenreResponse> getGenre(@Query("language") String language);

    @GET("person/popular")
    Call<ActorResponse> getActors(@Query("language") String language);

    @GET("movie/upcoming")
    Call<Upcoming> getUpcomingMovies(@Query("language") String language, @Query("page") int page);
}
