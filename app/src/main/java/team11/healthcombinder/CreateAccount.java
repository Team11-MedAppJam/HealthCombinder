package team11.healthcombinder;

import android.content.Intent;
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
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class CreateAccount extends AppCompatActivity {
    private Intent intent;
    private TextView textview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = new Intent(this, Timeline.class);
        setContentView(R.layout.activity_create_account);
        textview = new TextView(this);
        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_create_account);
        layout.addView(textview);
    }


    public void createButton(final View view) {
        EditText first = (EditText) findViewById(R.id.editFirst);
        EditText last = (EditText) findViewById(R.id.editLast);
        EditText date = (EditText) findViewById(R.id.editDate);
        EditText phone = (EditText) findViewById(R.id.editPhone);
        EditText email = (EditText) findViewById(R.id.editEmail);
        EditText pass1 = (EditText) findViewById(R.id.password1);
        EditText pass2 = (EditText) findViewById(R.id.password2);
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat outputDate = new SimpleDateFormat("yyyy-MM-dd");
        formatDate.setLenient(false);
        Date dateObject;
        try {
            dateObject = formatDate.parse(date.getText().toString());
        }
        catch(ParseException e)
        {
            textview.setText("Bad date format");
            return;
        }

        if(pass1.getText().toString().compareTo(pass2.getText().toString())!=0)
        {
            textview.setText("Passwords must match");
            return;
        }

        new CreateAccountTask().execute(
                email.getText().toString(),
                pass1.getText().toString(),
                first.getText().toString(),
                last.getText().toString(),
                phone.getText().toString(),
                outputDate.format(dateObject));

    }
    private class CreateAccountTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(Config.API_ROOT+"/CreateAccount");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                String message = "";
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write("email="+URLEncoder.encode(params[0],"utf-8")+
                        "&password="+URLEncoder.encode(params[1],"utf-8")+
                        "&first="+URLEncoder.encode(params[2],"utf-8")+
                        "&last="+URLEncoder.encode(params[3],"utf-8")+
                        "&phone="+URLEncoder.encode(params[4],"utf-8")+
                        "&date="+URLEncoder.encode(params[5],"utf-8"));
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
//                EditText.setText(result);
                if (id != 0) {
                    startActivity(intent);
                }
                else
                    textview.setText("Cannot create account. Email may be taken.");
            } catch (Exception e) {

            }
        }
    }
}
