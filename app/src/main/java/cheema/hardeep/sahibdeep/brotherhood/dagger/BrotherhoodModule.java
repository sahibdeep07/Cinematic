package cheema.hardeep.sahibdeep.brotherhood.dagger;

import cheema.hardeep.sahibdeep.brotherhood.api.MovieApi;
import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class BrotherhoodModule {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY_PARAMETER_NAME = "api_key";
    private static final String API_KEY = "5812e4b63553d1273a420416fddeed72";

    @Provides
    OkHttpClient provideOkHttpClient() {
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

    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    MovieApi provideMovieApi(Retrofit retrofit) {
        return retrofit.create(MovieApi.class);
    }
}
