package cheema.hardeep.sahibdeep.brotherhood.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cheema.hardeep.sahibdeep.brotherhood.R;

public class ActorActivity extends AppCompatActivity {

    public static Intent createIntent(Context context){
        return new Intent(context, ActorActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actor);
    }
}
