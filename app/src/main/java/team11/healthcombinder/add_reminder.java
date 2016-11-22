package team11.healthcombinder;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import java.util.Calendar;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import static android.R.attr.value;

public class add_reminder extends AppCompatActivity {
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    public String s_appointment, s_comment, s_date, s_time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminder);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        dateView = (TextView) findViewById(R.id.dateDisplay);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);
    }


    //SET DATE
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Calender",
                Toast.LENGTH_SHORT)
                .show();
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }


    //SET TIME
    public void setTime(View v){
        DialogFragment newFragment = new timepicker();
        newFragment.show(getFragmentManager(),"TimePicker");
    }


    //Set SharedPreference
    public void CreatePreference(){

        SharedPreferences setting;
        SharedPreferences.Editor editor;
        setting = getSharedPreferences(getString(R.string.ReminderPref), Context.MODE_PRIVATE);
        editor = setting.edit();
        editor.putString(getString(R.string.Appointment), s_appointment);
        editor.putString(getString(R.string.Comment), s_comment);
        editor.putString(getString(R.string.Date), s_date);
        editor.putString(getString(R.string.Time), s_time);
        editor.apply();

    }

    public void save_reminder(View v){
        EditText appt = (EditText) findViewById(R.id.et_appointment);
        EditText cmmt = (EditText) findViewById(R.id.et_comment);

        TextView d = (TextView) findViewById(R.id.dateDisplay);
        TextView t = (TextView) findViewById(R.id.timeDisplay);

        s_appointment = appt.getText().toString();
        s_comment = cmmt.getText().toString();
        s_date = d.getText().toString();
        s_time = t.getText().toString();
        String[] array = {s_appointment, s_comment, s_date,s_time};

        Intent intent = new Intent();
        intent.putExtra("result",array);
        setResult(RESULT_OK,intent);

        CreatePreference();
        this.finish();
    }





}
