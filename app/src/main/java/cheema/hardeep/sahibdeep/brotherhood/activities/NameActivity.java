package cheema.hardeep.sahibdeep.brotherhood.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.database.SharedPreferenceProvider;

public class NameActivity extends AppCompatActivity {

    View exit, genre;
    EditText name;
    TextView genreText;
    ImageView nextImage;
    String userName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_name);
        exit = findViewById(R.id.exitView);
        genre = findViewById(R.id.genreView);
        name = findViewById(R.id.nameBox);
        genreText = findViewById(R.id.genreText);
        nextImage = findViewById(R.id.nextImage);
        name.addTextChangedListener(userNameWatcher);

        Toast.makeText(this, SharedPreferenceProvider.getUserName(this), Toast.LENGTH_SHORT).show();

        //click listener

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (userName.isEmpty()) {
            genre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(NameActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    TextWatcher userNameWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            userName = name.getText().toString().trim();
            if (!userName.isEmpty()) {
                genre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(NameActivity.this, GenreActivity.class);
                        startActivity(intent);
                        SharedPreferenceProvider.saveUserName(NameActivity.this, userName);
                        Toast.makeText(NameActivity.this, "Saved", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                genre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(NameActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };
}
