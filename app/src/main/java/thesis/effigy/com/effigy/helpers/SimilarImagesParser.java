package thesis.effigy.com.effigy.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import thesis.effigy.com.effigy.adapters.SimilarImagesAdapter;
import thesis.effigy.com.effigy.backend.Downloader;
import thesis.effigy.com.effigy.data.SimilarImage;

/**
 * Created by Borys on 1/6/17.
 */

public class SimilarImagesParser {
    public static List<SimilarImage> parseJSON(JSONObject similarImages, long parentId) throws JSONException {
        List<SimilarImage> images = new ArrayList<>();
        JSONArray simImgs = similarImages.getJSONArray("similarImages");

        for(int i=0;i<simImgs.length();i++){
            JSONObject current = simImgs.getJSONObject(i);
            images.add(new SimilarImage(current.getLong("imageId"),parentId,current.getString("imageUrl"),0));
        }
        return images;
    }

    public static void updateSingleImages(List<SimilarImage> similarImages, SimilarImagesAdapter adapter){
        for(SimilarImage img : similarImages){
            img.adapter = adapter;
            URL[] link = new URL[1];
            try {
                link[0] = new URL(img.getImageUrl());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Downloader tmp = new Downloader();
            tmp.connector = img;
            tmp.execute(link);
            adapter.imageResources = similarImages;
            adapter.notifyDataSetChanged();
        }
    }
}
