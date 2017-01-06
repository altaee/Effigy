package thesis.effigy.com.effigy.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import thesis.effigy.com.effigy.data.SimilarImage;

/**
 * Created by Borys on 1/6/17.
 */

public class SimilarImagesJSONParser {
    public static List<SimilarImage> parseJSON(JSONObject similarImages) throws JSONException {
        List<SimilarImage> images = new ArrayList<>();
        JSONArray simImgs = similarImages.getJSONArray("similarImages");

        for(int i=0;i<simImgs.length();i++){
            JSONObject current = simImgs.getJSONObject(i);
            images.add(new SimilarImage(current.getLong("imageId"),current.getString("imageUrl"),0));
        }
        return images;
    }
}
