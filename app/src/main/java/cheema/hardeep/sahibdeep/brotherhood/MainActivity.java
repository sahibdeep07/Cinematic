package cheema.hardeep.sahibdeep.brotherhood;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String GOOGLE_SEARCH_URL = "https://www.google.com/search?num=1&q=";
    public static final String BASE_BOOK_MY_SHOW_URL = "https://in.bookmyshow.com/";
    public static final String HREF_BASE = "/url?q=";
    public static final String AMPERSAND = "&";
    public static final String CHARSET = "UTF-8";
    public static final String USER_AGENT = "Chrome";

    EditText query;
    Button submit;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
//                Uri.parse("http://maps.google.com/?ll=31.3260,75.5762&q=movie+theaters"));
//        startActivity(intent);

        query = findViewById(R.id.query);
        submit = findViewById(R.id.submitButton);
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeASearchRequest(query.getText().toString());
            }
        });
    }

    private void makeASearchRequest(final String query) {
        if(TextUtils.isEmpty(query)) return;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Document document = null;
                try {
                    document = Jsoup.connect(GOOGLE_SEARCH_URL + URLEncoder.encode(query, CHARSET)).userAgent(USER_AGENT).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final Document finalDocument = document;
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String bookMyShowUrl = getBookMyShowUrl(finalDocument);
                        webView.loadUrl(bookMyShowUrl.substring(0, bookMyShowUrl.indexOf(AMPERSAND)));
                    }
                });
            }
        });
    }

    private String getBookMyShowUrl(Document document) {
        List<String> hrefs = document.select("a[href]").eachAttr("href");
        for (int i = 0; i < hrefs.size(); i++) {
            if(hrefs.get(i).contains(BASE_BOOK_MY_SHOW_URL)) {
                return hrefs.get(i).replace(HREF_BASE, "");
            }
        }
        return null;
    }
}
