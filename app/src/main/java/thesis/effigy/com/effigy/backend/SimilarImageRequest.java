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
import java.util.ArrayList;
import java.util.List;

import thesis.effigy.com.effigy.data.SimilarImage;
import thesis.effigy.com.effigy.interfaces.ParentImageReceiver;

import static thesis.effigy.com.effigy.helpers.SimilarImagesParser.parseJSON;

/**
 * Created by Borys on 12/22/16.
 */

public class SimilarImageRequest extends AsyncTask<Long, Void, List<SimilarImage>>{

    public ParentImageReceiver connector = null;
    private String basicParentURL = "http://192.168.0.11:8080/images?parentId=", extra = "&quantity=";

    @Override
    protected List<SimilarImage> doInBackground(Long... longs) {
        URL url = null;
        JSONObject similarImages = null;
        List<SimilarImage> images = new ArrayList<>();
        long parentImageId = longs[0];
        long quantity = longs[1];
        String imageURL = "";
        try {
            url = new URL(basicParentURL+parentImageId+extra+quantity);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String responseString = "";
        HttpURLConnection urlConnection=null;
        try {
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                responseString = total.toString();
                images = parseJSON(new JSONObject(responseString));
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        } finally {
            urlConnection.disconnect();
        }
        return images;
    }

    @Override
    protected void onPostExecute(List<SimilarImage> similarImages) {
        super.onPostExecute(similarImages);
        connector.setSimilarImages(similarImages);
    }

}
