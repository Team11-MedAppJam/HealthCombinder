package team11.healthcombinder;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class ViewCard extends AppCompatActivity {
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = 1; // HARDCODED; PLEASE CHANGE
        setContentView(R.layout.activity_view_card);
        new loadNotecardTask().execute();

    }

    private class loadNotecardTask extends AsyncTask<String, Void, String> {
        private String notecardInfo;
        private String notecardContent;
        private String notecardComment;
        protected String doInBackground(String... params) {
            try {
//                String urlString = "http://35.162.96.241:8080/healthcombinder/Login";
                URL url = new URL(Config.API_ROOT+"/Notecard");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write("id="+id);
                wr.flush();
                wr.close();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuffer message = new StringBuffer();
                String inputline;
                while((inputline = in.readLine()) != null)
                    message.append(inputline);
                notecardInfo = message.toString();
                url = new URL(Config.API_ROOT+"/ShowNotecard");
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                wr = new OutputStreamWriter(con.getOutputStream());
                wr.write("id="+id);
                wr.flush();
                wr.close();
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                message = new StringBuffer();
                while((inputline = in.readLine()) != null)
                    message.append(inputline+'\n');
                notecardContent = message.toString();

                url = new URL(Config.API_ROOT+"/ShowNotecard");
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                wr = new OutputStreamWriter(con.getOutputStream());
                wr.write("id="+id+"&comment=true");
                wr.flush();
                wr.close();
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                message = new StringBuffer();
                while((inputline = in.readLine()) != null)
                    message.append(inputline+'\n');
                notecardComment = message.toString();

            } catch (Exception e) {
                return e.toString();
            }
            return "";
        }
        protected void onPostExecute(String result) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(new InputSource(new StringReader(notecardInfo)));
                Element root = doc.getDocumentElement();
                String title = root.getElementsByTagName("title").item(0).getTextContent();
                String time = root.getElementsByTagName("time").item(0).getTextContent();
                TextView descriptionText = (TextView) findViewById(R.id.descriptionForm);
                TextView commentsText = (TextView) findViewById(R.id.commentsForm);
                TextView dateText = (TextView) findViewById(R.id.date);
                TextView titleText = (TextView) findViewById(R.id.pageHeader);
                titleText.setText(title);
                dateText.setText(time);
                descriptionText.setText(notecardContent);
                commentsText.setText(notecardComment);
            } catch (Exception e) {
                TextView descriptionText = (TextView) findViewById(R.id.descriptionForm);
                descriptionText.setText(e.toString());
            }
        }
    }
}
