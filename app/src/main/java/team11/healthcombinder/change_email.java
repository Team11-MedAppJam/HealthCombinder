package team11.healthcombinder;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

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

public class change_email extends AppCompatActivity {

    private static final double HEIGHT = .7;
    private TextView textview;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        setContentView(R.layout.activity_change_email);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout(width,(int)(height*HEIGHT));
        textview = new TextView(this);
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_change_email);
        layout.addView(textview);


    }
    public void submit(final View view) {
        EditText editEmail2 = (EditText) findViewById(R.id.editText2);
        EditText editEmail3 = (EditText) findViewById(R.id.editText3);
        if(editEmail2.getText().toString().compareTo(editEmail3.getText().toString())==0)
        {
            new changeEmailTask().execute(editEmail2.getText().toString());
        }
        else
            textview.setText("Emails must match");
    }

    private class changeEmailTask extends AsyncTask<String, Void, String>{
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(Config.API_ROOT+"/ChangeProfile");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                String message = "";
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write("email="+params[0]);
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
                EditText editEmail = (EditText) findViewById(R.id.editText3);
                TextView newText = (TextView) findViewById(R.id.textView8);
                if(id==0)
                {
                    newText.setText("Can't change email to " + editEmail.getText().toString());
                }
                else
                {

                    newText.setText("Email successfully changed to " + editEmail.getText().toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
