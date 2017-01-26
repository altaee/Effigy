package thesis.effigy.com.effigy.backend.scores;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import thesis.effigy.com.effigy.interfaces.score_interfaces.ScoreUpdate;

import static thesis.effigy.com.effigy.config.ConfigConstants.REQUEST_TOTAL_SCORE;

/**
 * Created by Borys on 1/16/17.
 */

public class GetTotalScore extends AsyncTask<Void, Void, Integer> {

    private static final String UPDATE_SCORE_URL = REQUEST_TOTAL_SCORE;
    private ScoreUpdate connector;
    private String userName;

    public GetTotalScore(ScoreUpdate connector, String userName){
        this.connector = connector;
        this.userName = userName;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        URL url = null;
        JSONObject response = null;
        try {
            url = new URL(UPDATE_SCORE_URL + userName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String responseString = "";
        HttpURLConnection urlConnection=null;
        try {
            try {
                assert url != null;
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                responseString = total.toString();
                response = new JSONObject(responseString);
                return response.getInt("scoreCount");

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } finally {
            urlConnection.disconnect();
        }
        return -1;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        connector.updateTotalScore(result);
    }
}
