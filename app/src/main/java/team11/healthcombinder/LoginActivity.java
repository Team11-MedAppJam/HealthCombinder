package team11.healthcombinder;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import android.content.Intent;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import javax.xml.parsers.*;
import org.w3c.dom.*;
import java.io.StringReader;
import org.xml.sax.InputSource;

public class LoginActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private TextView textview;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        textview = new TextView(this);
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_login);
        layout.addView(textview);
        intent = new Intent(this, Timeline.class);
    }

    public void loginButton(final View view) throws Exception {
        String urlString = "http://35.162.96.241:8080/healthcombinder/Login";
        EditText editEmail = (EditText) findViewById(R.id.editEmail);
        String email = editEmail.getText().toString();
        EditText editPassword= (EditText) findViewById(R.id.editPassword);
        String password = editPassword.getText().toString();
        new LoginTask().execute(email,password);

    }

    private class LoginTask extends AsyncTask<String, Void, String>{
        protected String doInBackground(String... urls) {
            try {
                String urlString = "http://35.162.96.241:8080/healthcombinder/Login";
                URL url = new URL(urlString);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                String message = "";
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write("email="+urls[0]+"&password="+urls[1]);
                wr.flush();
                wr.close();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputline;
                while((inputline = in.readLine()) != null)
                    message += inputline;
                return message;
            }
            catch(Exception e)
            {
                return e.toString();
            }
        }

        protected void onPostExecute(String result){
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(result)));
                Element element = doc.getDocumentElement();
                int id = Integer.parseInt(element.getTextContent());
//                textview.setText(element.getTextContent());
                if(id!=0)
                {
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
