package cheema.hardeep.sahibdeep.brotherhood.api;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieApiProvider {

    private static final String API_KEY_PARAMETER_NAME = "api_key";
    private static final String API_KEY = "5812e4b63553d1273a420416fddeed72";

    /**
     * This creates an OkHttpClient which get attached to Retrofit Builder
     * OkHttpClient automatically attach API key to every request so we don't have to do it
     * manually for each GET or POST call
     */
    private static OkHttpClient createOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    //Intercept the outgoing request
                    Request original = chain.request();
                    HttpUrl originalUrl = original.url();

                    //Create new url from outgoing request url and attach API_KEY
                    HttpUrl newUrlWithApiKey = originalUrl
                            .newBuilder().addQueryParameter(API_KEY_PARAMETER_NAME, API_KEY).build();

                    //Create a new outgoing request with new API_KEY url
                    Request newRequestWithApiKey = original.newBuilder().url(newUrlWithApiKey).build();

                    //Send the request
                    return chain.proceed(newRequestWithApiKey);
                })
                .build();
        return okHttpClient;
    }

    /**
     * Use Retrofit to create MovieApi interface
     */
    public static MovieApi getMovieApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .client(createOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(MovieApi.class);
    }
}
