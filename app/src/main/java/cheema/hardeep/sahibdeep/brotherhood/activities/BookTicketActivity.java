package cheema.hardeep.sahibdeep.brotherhood.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheema.hardeep.sahibdeep.brotherhood.Brotherhood;
import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.api.LocationService;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.AMPERSAND;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.A_HREF;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.BASE_BOOK_MY_SHOW_URL;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.BOOK_MY_SHOW_QUERY;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.CHARSET;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.EMPTY;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.ERROR_JSOUP;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.ERROR_URL;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.ERROR_WEBPAGE;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.GOOGLE_SEARCH_URL;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.HREF;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.HREF_BASE;
import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.USER_AGENT;

public class BookTicketActivity extends AppCompatActivity {

    private static final String KEY_MOVIE_NAME = "movie-name";

    @BindView(R.id.bookingWebView)
    WebView bookingWebView;

    @BindView(R.id.webViewProgressBar)
    ProgressBar webViewProgressBar;

    @Inject
    CompositeDisposable compositeDisposable;

    @Inject
    LocationService locationService;

    public static Intent createIntent(Context context, String movieName) {
        Intent intent = new Intent(context, BookTicketActivity.class);
        intent.putExtra(KEY_MOVIE_NAME, movieName);
        return intent;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ticket);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        ((Brotherhood) getApplication()).getBrotherhoodComponent().inject(this);

        bookingWebView.getSettings().setJavaScriptEnabled(true);
        loadBookMyShowUrl();
    }

    private void loadBookMyShowUrl() {
        compositeDisposable.add(
                Observable
                        .defer(() -> Observable.just(Jsoup.connect(createBookMyShowUrl()).userAgent(USER_AGENT).get()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(disposable -> webViewProgressBar.setVisibility(View.VISIBLE))
                        .doOnTerminate(() -> webViewProgressBar.setVisibility(View.GONE))
                        .subscribe(document -> {
                            String bookMyShowUrl = getBookMyShowUrl(document);
                            if (bookMyShowUrl != null && !BASE_BOOK_MY_SHOW_URL.equals(bookMyShowUrl) && bookMyShowUrl.contains(AMPERSAND)) {
                                bookingWebView.loadUrl(bookMyShowUrl.substring(0, bookMyShowUrl.indexOf(AMPERSAND)));
                            } else {
                                bookingWebView.loadUrl(bookMyShowUrl);
                            }
                        }, throwable -> {
                            Toast.makeText(this, ERROR_WEBPAGE, Toast.LENGTH_SHORT).show();
                            finish();
                        })
        );
    }

    private String createBookMyShowUrl() {
        String movieName = getIntent().getStringExtra(KEY_MOVIE_NAME);
        try {
            if(!locationService.getCityName().isEmpty()) {
                return GOOGLE_SEARCH_URL + URLEncoder.encode(String.format(BOOK_MY_SHOW_QUERY, movieName, locationService.getCityName()), CHARSET);
            }
        } catch (Exception e) {
            Log.e(BookTicketActivity.class.getSimpleName(), ERROR_URL + e.getMessage());
        }
        return BASE_BOOK_MY_SHOW_URL;
    }

    private String getBookMyShowUrl(Document document) {
        for (String href : document.select(A_HREF).eachAttr(HREF)) {
            if (href.contains(BASE_BOOK_MY_SHOW_URL)) return href.replace(HREF_BASE, EMPTY);
        }
        return null;
    }
}
