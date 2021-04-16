package fabianpinzon23.example.flickrbrowser;

import android.util.Log;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    static final String FLICKR_QUERY = "FLICKR_QUERY";
    static final String PHOTO_TRANSFER = "PHOTO_TRANSFER";

    void activateToolbar(boolean enableHome) {
        Log.d(TAG, "activateToolbar: starts");
        ActionBar actionBar = getSupportActionBar();

        if (actionBar == null){
            Log.d(TAG, "activateToolbar is null");
            Toolbar toolbar = findViewById(R.id.toolbar);

            if (toolbar != null){
                Log.d(TAG, "activateToolbar: toolbar is not null");
                setSupportActionBar(toolbar);
                actionBar = getSupportActionBar();
            }
        }

        if (actionBar != null){
            Log.d(TAG, "activateToolbar: is not null");
            actionBar.setDisplayHomeAsUpEnabled(enableHome);
        }
    }
}
