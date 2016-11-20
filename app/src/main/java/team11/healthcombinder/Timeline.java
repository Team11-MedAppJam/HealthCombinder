package team11.healthcombinder;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Timeline extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Timeline");
        setSupportActionBar(toolbar);
        getSupportActionBar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAddCard(view);
            }
        });

        new NextPageTask().execute();
        // Example of a call to a native method
//        TextView tv = (TextView) findViewById(R.id.sample_text);
        //       tv.setText(stringFromJNI());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();


    public void sendMessage(View view) {
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }

    public void sendIntentAbout(MenuItem view) {
        Intent intent = new Intent(this, about.class);
        startActivity(intent);
    }

    public void sendAddCard(View fab) {
        Intent intent = new Intent(this, addcard.class);
        startActivity(intent);
    }

    private class NextPageTask extends AsyncTask<String, Void, String> {
        private int id;

        protected String doInBackground(String... params) {
            try {
                URL url = new URL(Config.API_ROOT + "/Session");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                String message = "";
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputline;
                while ((inputline = in.readLine()) != null)
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
//                textview.setText(element.getTextContent());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
