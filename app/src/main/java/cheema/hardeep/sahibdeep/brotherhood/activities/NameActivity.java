package cheema.hardeep.sahibdeep.brotherhood.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.database.SharedPreferenceProvider;

public class NameActivity extends AppCompatActivity {

    View exit, genre;
    EditText name;

    public static Intent createIntent(Context context) {
        return new Intent(context, NameActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        exit = findViewById(R.id.exitBackground);
        genre = findViewById(R.id.genreBackground);
        name = findViewById(R.id.nameBox);
        exit.setOnClickListener(v -> finish());
        genre.setOnClickListener(v -> handleGenreClick());
    }

    private void handleGenreClick() {
        String input = name.getText().toString().trim();
        if (!input.isEmpty()) {
            SharedPreferenceProvider.saveUserName(NameActivity.this, input);
            startActivity(GenreActivity.createIntent(this));
        } else {
            Toast.makeText(NameActivity.this, "Please enter your name.", Toast.LENGTH_SHORT).show();
        }
    }
}
