package team11.healthcombinder;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
import java.net.URLEncoder;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class addcard extends AppCompatActivity {
    private TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcard);
        textview = new TextView(this);
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_addcard);
        layout.addView(textview);
    }

    public void saveButton(final View view) {
        EditText editTitle = (EditText) findViewById(R.id.pageHeader);
        EditText editDescription = (EditText) findViewById(R.id.descriptionForm);
        EditText editComment = (EditText) findViewById(R.id.commentsForm);
        new addCardTask().execute(editTitle.getText().toString(),editDescription.getText().toString(),editComment.getText().toString());
    }

    private class addCardTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(Config.API_ROOT+"/EditNotecard");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                String message = "";
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write("id=0&title="+URLEncoder.encode(params[0],"utf-8")+
                        "&description="+URLEncoder.encode(params[1],"utf-8")+
                        "&comments="+URLEncoder.encode(params[2],"utf-8"));
                wr.flush();
                wr.close();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputline;
                while((inputline = in.readLine()) != null)
                    message += inputline;
                return message;
            } catch (Exception e) {
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
//                textview.setText(result);
                if (id != 0) {
                    textview.setText("Successfully added card");
                }
            } catch (Exception e) {
               textview.setText(e.toString());
            }
        }
    }
}
