package team11.healthcombinder;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.support.v7.widget.CardView;
import android.view.ViewGroup.LayoutParams;

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


        /////////////////////////////
        //Adding cards dynamically//
        ///////////////////////////

        //Converting dp to pixels
        Resources r = getResources();
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, r.getDisplayMetrics());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 275, r.getDisplayMetrics());
        int radius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, r.getDisplayMetrics());

        //Set ids, attach elements, then add to timeline
        for(int i = 0; i < 5; i++){
            //Find timeline list
            LinearLayout timelineCards = (LinearLayout) findViewById(R.id.timeline_notecards);

            //Declare objects to iterate on
            Context mContext = getApplicationContext();
            CardView cardView = new CardView(mContext);
            LinearLayout cardLinearLayout = new LinearLayout(mContext);
            TextView cardHeader = new TextView(mContext);
            TextView cardDescrip = new TextView(mContext);

            //STEPHEN LOOK HERE FOR NAMING CONVENTIONS
            //Whole card
            cardHeader.setId(2*i + 1);
            cardDescrip.setId(2*i + 2);

            //Set cardView styles
            LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(width, height);
            cardViewParams.gravity = Gravity.CENTER;
            cardViewParams.setMargins(0, 0, 0, 10);
            cardView.setRadius(radius);
            cardView.setLayoutParams(cardViewParams);
            cardView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(Timeline.this, about.class);
                    startActivity(intent);
                }
            });

            //Set cardLinearLayout styles
            LinearLayout.LayoutParams cardLinearLayoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            cardLinearLayout.setOrientation(LinearLayout.VERTICAL);

            //Set cardHeader styles
            FrameLayout.LayoutParams cardHeaderParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            cardHeader.setHint("Test Title Card");

            //Set cardDescrip styles
            FrameLayout.LayoutParams cardDescParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            cardDescrip.setLayoutParams(cardDescParams);
            cardDescrip.setHint("Testing symptoms descriptions");

            //Attach Elements in Hierachy
            cardLinearLayout.addView(cardHeader);
            cardLinearLayout.addView(cardDescrip);
            cardView.addView(cardLinearLayout);

            //Add cardView to timeline
            timelineCards.addView(cardView);
        }

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
