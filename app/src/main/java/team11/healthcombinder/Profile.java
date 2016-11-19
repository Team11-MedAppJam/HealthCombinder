package team11.healthcombinder;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_profile);

        Button b = (Button) findViewById(R.id.change_password);
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(Profile.this, popup.class));
            }
        });

        ImageButton b_email = (ImageButton) findViewById(R.id.edit_email);
        b_email.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(Profile.this, change_email.class));
            }
        });
    }
    public void openHistoryChecklist(View view) {
        Intent intent = new Intent(this, annual_history_checklist.class);
        startActivity(intent);
    }

    public void toHomePage(View view){
        Intent intent = new Intent(this, Timeline.class);
        startActivity(intent);
    }
}
