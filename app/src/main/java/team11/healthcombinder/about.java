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
import android.widget.RelativeLayout;

public class about extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

//        RelativeLayout b = (RelativeLayout) findViewById(R.id.activity_about);
//        b.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                startActivity(new Intent(about.this, popup.class));
//            }
//        });
    }

    public void toHomePage(View view){
        Intent intent = new Intent(this, Timeline.class);
        startActivity(intent);
    }

    public void dismiss(View view){
        this.finish();
    }
}
