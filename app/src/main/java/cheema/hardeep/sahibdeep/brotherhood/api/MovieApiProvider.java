package cheema.hardeep.sahibdeep.brotherhood.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieApiProvider {


    /**
     * Use Retrofit to create MovieApi interface
     */
    public static MovieApi getMovieApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(MovieApi.class);
    }


}
