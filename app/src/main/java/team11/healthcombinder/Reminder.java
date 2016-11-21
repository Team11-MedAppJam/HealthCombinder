package team11.healthcombinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Reminder extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void addReminder (View view){
        Intent intent = new Intent(this, add_reminder.class);
        startActivity(intent);
    }

}
