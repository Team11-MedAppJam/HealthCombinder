package team11.healthcombinder;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
        new GetUserInfoTask().execute();
    }
    public void openHistoryChecklist(View view) {
        Intent intent = new Intent(this, annual_history_checklist.class);
        startActivity(intent);
    }

    public void toHomePage(View view){
        Intent intent = new Intent(this, Timeline.class);
        startActivity(intent);
    }


    private class GetUserInfoTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(Config.API_ROOT+"/User");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                StringBuffer message = new StringBuffer();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputline;
                while((inputline = in.readLine()) != null)
                    message.append(inputline);
                return message.toString();
            }
            catch(Exception e)
            {
                return e.toString();
            }
        }

        protected void onPostExecute(String result) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(result)));
                Element root = doc.getDocumentElement();
                String email = root.getElementsByTagName("email").item(0).getTextContent();
                String name = root.getElementsByTagName("first_name").item(0).getTextContent() + " " +
                        root.getElementsByTagName("last_name").item(0).getTextContent();
                String dob = root.getElementsByTagName("birth_date").item(0).getTextContent();
                TextView nameText = (TextView) findViewById(R.id.textView);
                TextView emailText = (TextView) findViewById(R.id.textView5);
                TextView dateText = (TextView) findViewById(R.id.textView2);
                nameText.setText(name);
                emailText.setText(email);
                dateText.setText("Birth date: "+dob);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
