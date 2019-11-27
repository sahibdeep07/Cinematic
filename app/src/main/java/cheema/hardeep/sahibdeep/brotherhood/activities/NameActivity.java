package cheema.hardeep.sahibdeep.brotherhood.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cheema.hardeep.sahibdeep.brotherhood.Brotherhood;
import cheema.hardeep.sahibdeep.brotherhood.R;
import cheema.hardeep.sahibdeep.brotherhood.database.UserInfoManager;

public class NameActivity extends AppCompatActivity {

    @BindView(R.id.exitBackground)
    View exit;

    @BindView(R.id.moveToNameBackground)
    View genre;

    @BindView(R.id.nameBox)
    EditText name;

    @Inject
    UserInfoManager userInfoManager;

    public static Intent createIntent(Context context) {
        return new Intent(context, NameActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        getSupportActionBar().hide();
        ((Brotherhood) getApplication()).getBrotherhoodComponent().inject(this);
        ButterKnife.bind(this);

        exit.setOnClickListener(v -> finish());
        genre.setOnClickListener(v -> handleGenreClick());
    }

    private void handleGenreClick() {
        String input = name.getText().toString().trim();
        if (!input.isEmpty()) {
            userInfoManager.saveUserName(input);
            startActivity(GenreActivity.createIntent(this));
        } else {
            Toast.makeText(NameActivity.this, "Please enter your name.", Toast.LENGTH_SHORT).show();
        }
    }
}
