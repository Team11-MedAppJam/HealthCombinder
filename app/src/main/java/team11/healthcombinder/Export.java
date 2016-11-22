package team11.healthcombinder;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class Export extends Timeline {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Export");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // BOTTOM-BAR STUFFS:
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.profile_item:
                                startActivity(new Intent(getApplicationContext(), Profile.class));
                                break;

                            case R.id.timeline_item:
                                startActivity(new Intent(getApplicationContext(), Timeline.class));
                                break;

                            case R.id.reminder_item:
                                startActivity(new Intent(getApplicationContext(), Reminder.class));
                                break;
                        }
                        return false;
                    }
                });
    }
}
