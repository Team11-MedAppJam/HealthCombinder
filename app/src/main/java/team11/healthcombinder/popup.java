package team11.healthcombinder;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

public class popup extends AppCompatActivity {
    private static final double HEIGHT = .7;
    private TextView textview;
    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

        setContentView(R.layout.activity_popup);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout(width,(int)(height*HEIGHT));
        textview = new TextView(this);
        ViewGroup layout = (ViewGroup) findViewById(R.id.popup);
        layout.addView(textview);

    }
    public void submit(final View view) {
        EditText currentPassword = (EditText) findViewById(R.id.current_password);
        EditText newPassword = (EditText) findViewById(R.id.new_password);
        EditText verifiedPassword = (EditText) findViewById(R.id.verified_password);
        if(newPassword.getText().toString().compareTo(verifiedPassword.getText().toString())==0)
        {
            new changePasswordTask().execute(currentPassword.getText().toString(),
                    newPassword.getText().toString());
        }
        else
            textview.setText("New passwords must match");
    }

    private class changePasswordTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(Config.API_ROOT+"/ChangeProfile");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                String message = "";
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write("oldpass=" + URLEncoder.encode(params[0],"utf-8") +
                        "&newpass=" + URLEncoder.encode(params[1],"utf-8"));
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
                if(id==0)
                {
                    textview.setText("Password change failure");
                }
                else
                {

                    textview.setText("Password successfully changed");
                    popup.this.finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
