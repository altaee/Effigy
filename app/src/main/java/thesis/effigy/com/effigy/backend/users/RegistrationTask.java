package thesis.effigy.com.effigy.backend.users;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import thesis.effigy.com.effigy.interfaces.user_interfaces.RegistrationInterface;

import static thesis.effigy.com.effigy.config.ConfigConstants.REQUEST_REGISTER;

/**
 * Created by Borys on 1/16/17.
 */

public class RegistrationTask extends AsyncTask<JSONObject, Void, JSONObject> {

    private static final String AUTHORISATION_URL = REQUEST_REGISTER;

    public RegistrationInterface connector;

    public RegistrationTask(RegistrationInterface connector) {
        this.connector = connector;
    }

    @Override
    protected JSONObject doInBackground(JSONObject... jsonObjects) {
        URL url = null;
        JSONObject toSend = jsonObjects[0];
        try {
            url = new URL(AUTHORISATION_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String responseString = "";
        HttpURLConnection urlConnection=null;
        JSONObject images = null;
        try {
            try {
                assert url != null;
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());

                wr.writeBytes(toSend.toString());
                wr.flush();
                wr.close();

//                    String msg = urlConnection.getResponseMessage();
//                    if(msg.isEmpty()){
//                        InputStream in = new BufferedInputStream(urlConnection.getErrorStream());
//                        BufferedReader r = new BufferedReader(new InputStreamReader(in));
//                        StringBuilder total = new StringBuilder();
//                        String line;
//                        while ((line = r.readLine()) != null) {
//                            total.append(line);
//                        }
//                        responseString = total.toString();
//                    }
//                    else{
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                responseString = total.toString();
//                    }

                images = new JSONObject(responseString);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } finally {
            urlConnection.disconnect();
        }

        return images;
    }

    @Override
    protected void onPostExecute(final JSONObject success) {
        String userName = "";
        String token = "";

        if (success != null) {

            try {
                userName = success.getString("username");
                token = success.getString("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(userName.isEmpty() || token.isEmpty()){
                connector.registrationResult("");
            }else{
                connector.registrationResult(success);
            }
        } else {
            connector.registrationResult("");
        }
    }
}
