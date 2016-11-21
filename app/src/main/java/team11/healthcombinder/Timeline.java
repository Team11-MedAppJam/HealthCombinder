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
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

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

//        TextView descriptionView = (TextView) findViewById(R.id.info_text);
//        descriptionView.setText("test");
//        TextView symptomView = (TextView) findViewById(R.id.textView5);
//        symptomView.setText("Testtitle");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendAddCard(view);
            }
        });


        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_timeline);

        new loadNotecardsTask().execute();
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

    private class loadNotecardsTask extends AsyncTask<String, Void, String> {
        private ArrayList<String> notecardXmlList;
        private ArrayList<String> notecardContentList;

        protected String doInBackground(String... params) {
            notecardXmlList = new ArrayList<String>();
            notecardContentList = new ArrayList<String>();
            try {
                URL notecardsURL = new URL(Config.API_ROOT + "/Notecards");
                HttpURLConnection notecardsCon = (HttpURLConnection) notecardsURL.openConnection();
                notecardsCon.setDoOutput(true);
                StringBuffer message = new StringBuffer();
                BufferedReader in = new BufferedReader(new InputStreamReader(notecardsCon.getInputStream()));
                String inputline;
                while ((inputline = in.readLine()) != null)
                    message.append(inputline);

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document notecardsDoc = builder.parse(new InputSource(new StringReader(message.toString())));
                Element notecardsRoot = notecardsDoc.getDocumentElement();
                NodeList notecards = notecardsRoot.getElementsByTagName("notecard");
                for(int i = 0; notecards.item(i)!=null; ++i)
                {
                    Element notecard = (Element) notecards.item(i);
                    String notecardID = notecard.getElementsByTagName("notecard_id").item(0).getTextContent();
                    URL notecardURL = new URL(Config.API_ROOT + "/Notecard");
                    HttpURLConnection notecardCon = (HttpURLConnection) notecardURL.openConnection();
                    notecardCon.setRequestMethod("POST");
                    notecardCon.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(notecardCon.getOutputStream());
                    wr.write("id="+notecardID);
                    wr.flush();
                    wr.close();
                    in = new BufferedReader(new InputStreamReader(notecardCon.getInputStream()));
                    message = new StringBuffer();
                    while ((inputline = in.readLine()) != null)
                        message.append(inputline);
                    notecardXmlList.add(message.toString());
                    URL descriptionURL = new URL(Config.API_ROOT + "/ShowNotecard");
                    HttpURLConnection descriptionCon = (HttpURLConnection) descriptionURL.openConnection();
                    descriptionCon.setRequestMethod("POST");
                    descriptionCon.setDoOutput(true);
                    wr = new OutputStreamWriter(descriptionCon.getOutputStream());
                    wr.write("id="+notecardID);
                    wr.flush();
                    wr.close();
                    in = new BufferedReader(new InputStreamReader(descriptionCon.getInputStream()));
                    message = new StringBuffer();
                    while ((inputline = in.readLine()) != null)
                        message.append(inputline);
                    notecardContentList.add(message.toString());
                }
                //return message.toString();
            } catch (Exception e) {
                //return e.toString();
            }
            return "";
        }

        protected void onPostExecute(String param) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                for(int i = 0; i < notecardXmlList.size(); ++i) {
                    Document doc = builder.parse(new InputSource(new StringReader(notecardXmlList.get(i))));
                    Element notecard = doc.getDocumentElement();
                    String id = notecard.getElementsByTagName("notecard_id").item(0).getTextContent();
                    TextView symptomView = (TextView) findViewById(R.id.textView5);
                    symptomView.setText(notecard.getElementsByTagName("title").item(0).getTextContent());
                    //TextView timeView = (TextView) findViewById(...);
                    //timeView.setText(notecard.getElementsByTagName("time").item(0).getTextContent());
                    TextView descriptionView = (TextView) findViewById(R.id.info_text);
                    descriptionView.setText(notecardContentList.get(i));
                    //timeView.setText

                    break; //remove this line when multiple notecards
                }
//                TextView descriptionView = (TextView) findViewById(R.id.info_text);
//                descriptionView.setText("test");
//                TextView symptomView = (TextView) findViewById(R.id.textView5);
//                symptomView.setText("Testtitle");
            } catch (Exception e) {
            }
        }
    }
}
