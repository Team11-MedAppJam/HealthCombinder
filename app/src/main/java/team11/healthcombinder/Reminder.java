package team11.healthcombinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Reminder extends Timeline {

    ArrayList <String[]>list_of_Reminder = new ArrayList<String[]>();
    Integer id = 5000; //id of Reminder
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
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
                            case R.id.export_item:
                                startActivity(new Intent(getApplicationContext(), Export.class));
                                break;
                        }
                        return false;
                    }
                });

    }

    public void addReminder (View view){
        Intent intent = new Intent(this, add_reminder.class);
        startActivityForResult(intent,1);
    }

    public String display_reminder(String[] R){
        return (R[0] + "\n" + R[1] +"\n\n"
                + "On: " +R[2] + "\nAt:" +R[3]);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode ==1){
            if(resultCode == RESULT_OK){
                String[] strEditText = data.getStringArrayExtra("result");
                list_of_Reminder.add(strEditText);
                create_view(display_reminder(get_field_from_Pref()), id);
                count++;
            }
        }

    }

    public void create_view(String s, Integer id){
        LinearLayout layout = (LinearLayout) findViewById(R.id.reminder_layout);
        layout.setOrientation(LinearLayout.VERTICAL);

        int width = layout.getWidth();
        double d = width*0.8;
        width = ((int) d);

        TextView nView = new TextView(this);
        nView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        nView.setText(s);
        nView.setId(id);
        nView.setLayoutParams(new ActionBar.LayoutParams(width,
                RelativeLayout.LayoutParams.WRAP_CONTENT));
        nView.setBackgroundColor(getResources().getColor(R.color.colorCard));

        layout.addView(nView);

        Space n = new Space(this);
        n.setLayoutParams(new ActionBar.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                20));
        layout.addView(n);

        //((LinearLayout)linearLayout).addView(nView);
    }


    public String[] get_field_from_Pref( ) {
        String appt = "";
        String cmt = "";
        String d = "";
        String t = "";

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.ReminderPref), Context.MODE_PRIVATE);

        appt = sharedPreferences.getString(getString(R.string.Appointment), "");
        cmt = sharedPreferences.getString(getString(R.string.Comment), "");
        d = sharedPreferences.getString(getString(R.string.Date), "");
        t = sharedPreferences.getString(getString(R.string.Time), "");
        //}
        return new String[]{appt, cmt, d, t};
    }

}
