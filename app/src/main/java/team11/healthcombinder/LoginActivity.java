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
import java.net.CookieManager;
import java.net.CookieHandler;
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
import java.net.URLEncoder;

import org.xml.sax.InputSource;

public class LoginActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CookieManager cookiemanager = new CookieManager();
        CookieHandler.setDefault(cookiemanager);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        intent = new Intent(this, Timeline.class);
        new NextPageTask().execute(); //Remove this line in app
    }

    public void loginButton(final View view) throws Exception {
        String urlString = "http://35.162.96.241:8080/healthcombinder/Login";
        EditText editEmail = (EditText) findViewById(R.id.editEmail);
        String email = editEmail.getText().toString();
        EditText editPassword= (EditText) findViewById(R.id.editPassword);
        String password = editPassword.getText().toString();
        new LoginTask().execute(email,password);

    }

    public void createAccountButton(final View view) {
        Intent createAccountIntent = new Intent(this, CreateAccount.class);
        startActivity(createAccountIntent);
    }

    private class LoginTask extends AsyncTask<String, Void, String>{
        protected String doInBackground(String... urls) {
            try {
//                String urlString = "http://35.162.96.241:8080/healthcombinder/Login";
                URL url = new URL(Config.API_ROOT+"/Login");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                String message = "";
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write("email="+ URLEncoder.encode(urls[0],"utf-8")+"&password="+URLEncoder.encode(urls[1],"utf-8"));
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

        protected void onPostExecute(String result) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(result)));
                Element element = doc.getDocumentElement();
                int id = Integer.parseInt(element.getTextContent());
//                textview.setText(result);
                if (id != 0) {
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private class NextPageTask extends AsyncTask<String, Void, String>{
        private int id;
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(Config.API_ROOT+"/Session");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                String message = "";
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputline;
                while((inputline = in.readLine()) != null)
                    message += inputline;
                return message;
            } catch (Exception e) {
                return e.toString();
            }
        }
        protected void onPostExecute(String result) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(result)));
                Element element = doc.getDocumentElement();
                int id = Integer.parseInt(element.getTextContent());
                if (id != 0) {
                    startActivity(intent);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
