package cheema.hardeep.sahibdeep.brotherhood.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheema.hardeep.sahibdeep.brotherhood.Brotherhood;
import cheema.hardeep.sahibdeep.brotherhood.R;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import static cheema.hardeep.sahibdeep.brotherhood.utils.Constants.EMPTY;

public class BookTicketActivity extends AppCompatActivity {

    private static final String KEY_MOVIE_NAME = "movie-name";
    private static final String GOOGLE_SEARCH_URL = "https://www.google.com/search?num=1&q=";
    private static final String BASE_BOOK_MY_SHOW_URL = "https://in.bookmyshow.com/";
    private static final String HREF_BASE = "/url?q=";
    private static final String AMPERSAND = "&";
    private static final String CHARSET = "UTF-8";
    private static final String USER_AGENT = "Chrome";
    private static final String A_HREF = "a[href]";
    private static final String HREF = "href";
    private static final String QUERY = "bookmyshow %s %s";


    @BindView(R.id.bookingWebView)
    WebView bookingWebView;

    @Inject
    CompositeDisposable compositeDisposable;

    public static Intent createIntent(Context context, String movieName) {
        Intent intent = new Intent(context, BookTicketActivity.class);
        intent.putExtra(KEY_MOVIE_NAME, movieName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_ticket);
        getSupportActionBar().hide();
        ButterKnife.bind(this);
        ((Brotherhood) getApplication()).getBrotherhoodComponent().inject(this);

        loadBookMyShowUrl();
    }

    private void loadBookMyShowUrl() {
        compositeDisposable.add(
                Observable
                        .just(Objects.requireNonNull(createJsoupConnection()))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(document -> {
                            String bookMyShowUrl = getBookMyShowUrl(document);
                            if(!BASE_BOOK_MY_SHOW_URL.equals(bookMyShowUrl) && bookMyShowUrl.contains(AMPERSAND)) {
                                bookingWebView.loadUrl(bookMyShowUrl.substring(0, bookMyShowUrl.indexOf(AMPERSAND)));
                            } else {
                                bookingWebView.loadUrl(bookMyShowUrl);
                            }
                        }, throwable -> {
                            Toast.makeText(this, "Error loading the WebPage", Toast.LENGTH_SHORT).show();
                            finish();
                        })
        );
    }

    private Document createJsoupConnection() {
        try {
            return Jsoup.connect(createBookMyShowUrl()).userAgent(USER_AGENT).get();
        } catch (IOException e) {
            Log.e(BookTicketActivity.class.getSimpleName(), "Error creating Jsoup connection: " + e.getMessage());
            return null;
        }
    }

    private String createBookMyShowUrl() {
        String movieName = getIntent().getStringExtra(KEY_MOVIE_NAME);
        try {
            return GOOGLE_SEARCH_URL + URLEncoder.encode(String.format(QUERY, movieName, "jalandhar"), CHARSET);
        } catch (Exception e) {
            Log.e(BookTicketActivity.class.getSimpleName(), "Error creating book my show url: " + e.getMessage());
            return BASE_BOOK_MY_SHOW_URL;
        }
    }

    private String getBookMyShowUrl(Document document) {
        for (String href : document.select(A_HREF).eachAttr(HREF)) {
            if (href.contains(BASE_BOOK_MY_SHOW_URL)) return href.replace(HREF_BASE, EMPTY);
        }
        return null;
    }
}
