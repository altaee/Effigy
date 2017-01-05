package thesis.effigy.com.effigy.backend;

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

import thesis.effigy.com.effigy.data.ParentImage;
import thesis.effigy.com.effigy.interfaces.ParentImageReceiver;

/**
 * Created by Borys on 12/22/16.
 */

public class ParentImageRequest extends AsyncTask<String, Void, ParentImage>{

    public ParentImageReceiver connector = null;
    private String basicParentURL = "http://192.168.43.12:8080/images/parent/random?username=";

    @Override
    protected ParentImage doInBackground(String... strings) {
        URL url = null;
        JSONObject parentImage;
        int imageId = 0;
        String imageURL = "";
        try {
            url = new URL(basicParentURL+strings[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String responseString;
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
                parentImage = new JSONObject(responseString);
                imageId = parentImage.getInt("parentId");
                imageURL = parentImage.getString("imageUrl");
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } finally {
            assert urlConnection != null;
            urlConnection.disconnect();
        }

        return new ParentImage(imageId,imageURL);
    }

    @Override
    protected void onPostExecute(ParentImage parentImage) {
        super.onPostExecute(parentImage);
        connector.setParentImage(parentImage);
    }
}
