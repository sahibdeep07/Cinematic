package cheema.hardeep.sahibdeep.brotherhood.dagger;

import android.app.Application;

import javax.inject.Singleton;

import cheema.hardeep.sahibdeep.brotherhood.api.MovieApi;
import cheema.hardeep.sahibdeep.brotherhood.api.LocationService;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class BrotherhoodModule {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String API_KEY_PARAMETER_NAME = "api_key";
    private static final String API_KEY = "5812e4b63553d1273a420416fddeed72";
    private Application application;

    public BrotherhoodModule(Application application) {
        this.application = application;
    }

    @Provides
    CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }

    @Provides
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    HttpUrl originalUrl = original.url();
                    HttpUrl newUrlWithApiKey = originalUrl.newBuilder().addQueryParameter(API_KEY_PARAMETER_NAME, API_KEY).build();
                    Request newRequestWithApiKey = original.newBuilder().url(newUrlWithApiKey).build();
                    return chain.proceed(newRequestWithApiKey);
                })
                .build();
    }

    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    MovieApi provideMovieApi(Retrofit retrofit) {
        return retrofit.create(MovieApi.class);
    }

    @Provides
    @Singleton
    LocationService provideLocationService() {
        return new LocationService(application.getBaseContext());
    }
}
