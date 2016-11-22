package team11.healthcombinder;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ToggleButton;

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

public class annual_history_checklist extends AppCompatActivity {

    private EditText e_allergies;
    private EditText e_immunization;
    private EditText e_chronic;
    private EditText e_treatment;
    private EditText e_history;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_annual_history_checklist);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        e_allergies = (EditText) findViewById(R.id.allergies_text);
        e_immunization = (EditText) findViewById(R.id.immunizations_text);
        e_chronic = (EditText) findViewById(R.id.chron_med_all_text);
        e_treatment = (EditText) findViewById(R.id.treat_med_text);
        e_history = (EditText) findViewById(R.id.fam_his_text);


        final ToggleButton toggleButton = (ToggleButton) findViewById(R.id.toggleEdit);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
                e_allergies.setFocusableInTouchMode(isChecked);
                e_immunization.setFocusableInTouchMode(isChecked);
                e_chronic.setFocusableInTouchMode(isChecked);
                e_treatment.setFocusableInTouchMode(isChecked);
                e_history.setFocusableInTouchMode(isChecked);
            }
        });
        new LoadInfoTask().execute();
    }

    public void saveButton(final View view) {
        new SetInfoTask().execute(
                e_allergies.getText().toString(),
                e_immunization.getText().toString(),
                e_chronic.getText().toString(),
                e_treatment.getText().toString(),
                e_history.getText().toString());
    }

    private class LoadInfoTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(Config.API_ROOT+"/User");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                StringBuffer message = new StringBuffer();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputline;
                while((inputline = in.readLine()) != null)
                    message.append(inputline);
                return message.toString();
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
                Element root = doc.getDocumentElement();
                e_allergies.setText(root.getElementsByTagName("allergies").item(0).getTextContent());
                e_immunization.setText(root.getElementsByTagName("immunizations").item(0).getTextContent());
                e_chronic.setText(root.getElementsByTagName("chronic_allergies").item(0).getTextContent());
                e_treatment.setText(root.getElementsByTagName("medication").item(0).getTextContent());
                e_history.setText(root.getElementsByTagName("family_history").item(0).getTextContent());


//                textview.setText(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private class SetInfoTask extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(Config.API_ROOT+"/SetHistory");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write("allergies="+ URLEncoder.encode(params[0],"utf-8")+
                        "&immunizations="+URLEncoder.encode(params[1],"utf-8")+
                        "&chronic_allergies="+URLEncoder.encode(params[2],"utf-8")+
                        "&medication="+URLEncoder.encode(params[3],"utf-8")+
                        "&family_history="+URLEncoder.encode(params[4],"utf-8"));
                wr.flush();
                wr.close();
                StringBuffer message = new StringBuffer();
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputline;
                while((inputline = in.readLine()) != null)
                    message.append(inputline);
                return message.toString();
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
                Element root = doc.getDocumentElement();
                if(root.getTextContent().compareTo("0")!=0)
                {
                    annual_history_checklist.this.finish();
                }


//                textview.setText(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

