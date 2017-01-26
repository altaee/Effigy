package thesis.effigy.com.effigy.backend.scores;

import android.os.AsyncTask;

import org.json.JSONArray;
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

import thesis.effigy.com.effigy.interfaces.score_interfaces.BestScoresProcessor;

import static thesis.effigy.com.effigy.config.ConfigConstants.REQUEST_BEST_SCORE;

/**
 * Created by Borys on 1/19/17.
 */

public class GetBestScores extends AsyncTask<Void, Void, String[]> {

    private static final String UPDATE_SCORE_URL = REQUEST_BEST_SCORE;
    private BestScoresProcessor connector;
    private String userName;

    public GetBestScores(BestScoresProcessor connector, String userName){
        this.connector = connector;
        this.userName = userName;
    }

    @Override
    protected String[] doInBackground(Void... voids) {
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

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } finally {
            urlConnection.disconnect();
        }
        return generateStringsFromJSON(response);
    }

    private String[] generateStringsFromJSON(JSONObject response) {
        String[] result = null; //"1. Anna: 26 points"
        String tmp1 = ". ";
        String tmp2 = ": ";
        String tmp3 = " points";
        try {
            JSONArray bestScores = response.getJSONArray("topScores");
            int userPosition = response.getInt("userPosition");
            result = new String[bestScores.length()+1];
            result[bestScores.length()] = userPosition + tmp1 + userName + tmp2 + response.getInt("userScore") + tmp3;

            for(int i = 1; i <= bestScores.length() ;i++){
                JSONObject obj = bestScores.getJSONObject(i-1);
                result[i - 1] = i + tmp1 + obj.getString("userName") + tmp2 + obj.getInt("scoreCount") + tmp3;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String[] result) {
        super.onPostExecute(result);
        connector.bestScoresReceived(result);
    }
}
