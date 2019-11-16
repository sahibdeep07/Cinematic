package cheema.hardeep.sahibdeep.brotherhood.api;

import cheema.hardeep.sahibdeep.brotherhood.models.ActorResponse;
import cheema.hardeep.sahibdeep.brotherhood.models.CastDetail;
import cheema.hardeep.sahibdeep.brotherhood.models.GenreResponse;
import cheema.hardeep.sahibdeep.brotherhood.models.MovieDetail;
import cheema.hardeep.sahibdeep.brotherhood.models.Upcoming;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("genre/movie/list")
    Observable<GenreResponse> getGenre(@Query("language") String language);

    @GET("person/popular")
    Observable<ActorResponse> getActors(@Query("language") String language);

    @GET("movie/upcoming")
    Observable<Upcoming> getUpcomingMovies(@Query("language") String language, @Query("page") int page);

    @GET("movie/{id}")
    Observable<MovieDetail> getMovieDetails(@Path("id") long movieId);

    @GET("movie/{id}/credits")
    Observable<CastDetail> getMovieCastDetails(@Path("id") long movieId);
}
