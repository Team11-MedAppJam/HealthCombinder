package team11.healthcombinder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

public class annual_history_checklist extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annual_history_checklist);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
//        ImageButton b_allergies = (ImageButton) findViewById(R.id.edit_allergies);
//        ImageButton b_immunization = (ImageButton) findViewById(R.id.edit_immunization);
//        ImageButton b_chronic = (ImageButton) findViewById(R.id.edit_chr_med_allergies);
//        ImageButton b_treatment = (ImageButton) findViewById(R.id.edit_treatment);
//        ImageButton b_history = (ImageButton) findViewById(R.id.edit_fam_history);

        final EditText e_allergies = (EditText) findViewById(R.id.allergies_text);
        final EditText e_immunization = (EditText) findViewById(R.id.immunizations_text);
        final EditText e_chronic = (EditText) findViewById(R.id.chron_med_all_text);
        final EditText e_treatment = (EditText) findViewById(R.id.treat_med_text);
        final EditText e_history = (EditText) findViewById(R.id.fam_his_text);

        final ToggleButton toggleButton=(ToggleButton)findViewById(R.id.toggleEdit);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                e_allergies.setFocusableInTouchMode(isChecked);
                e_immunization.setFocusableInTouchMode(isChecked);
                e_chronic.setFocusableInTouchMode(isChecked);
                e_treatment.setFocusableInTouchMode(isChecked);
                e_history.setFocusableInTouchMode(isChecked);
            }
        });
}
}

