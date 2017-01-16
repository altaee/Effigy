package thesis.effigy.com.effigy.backend;

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

import thesis.effigy.com.effigy.data.SimilarImage;
import thesis.effigy.com.effigy.interfaces.ScoreUpdate;

/**
 * Created by Borys on 1/16/17.
 */

public class SetScore extends AsyncTask<SimilarImage, Void, Boolean>{

    private static final String UPDATE_SCORE_URL = "http://192.168.0.11:8080/scores";
    private ScoreUpdate connector;
    private int position;
    private String userName;

    public SetScore(ScoreUpdate connector, String userName, int position){
        this.connector = connector;
        this.position = position;
        this.userName = userName;
    }

    @Override
    protected Boolean doInBackground(SimilarImage... similarImages) {
        SimilarImage image = similarImages[0];
        URL url = null;
        JSONObject request = generateJSON(image);

        try {
            url = new URL(UPDATE_SCORE_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String responseString = "";
        HttpURLConnection urlConnection=null;
        try {
            try {
                assert url != null;
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());

                wr.writeBytes(request.toString());
                wr.flush();
                wr.close();

                InputStream in = new BufferedInputStream(urlConnection.getErrorStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                responseString = total.toString();
                r.close();

                if(!responseString.isEmpty()) return false;


            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            urlConnection.disconnect();
        }

        return true;
    }

    private JSONObject generateJSON(SimilarImage image) {
        JSONObject result = new JSONObject();
        try {
            result.put("parentId", image.getParentImageId());
            result.put("scoredImageId", image.getImageId());
            result.put("score", image.getRanking());
            result.put("username", userName);
            result.put("ranking", position);
            result.put("features", "PALETTE_POWER");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        connector.scoreWasUpdated(result);
    }
}
