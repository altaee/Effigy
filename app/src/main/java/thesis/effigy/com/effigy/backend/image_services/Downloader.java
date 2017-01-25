package thesis.effigy.com.effigy.backend.image_services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;

import thesis.effigy.com.effigy.interfaces.image_interfaces.ImagesDownloader;

/**
 * Created by Borys on 1/4/17.
 */

public class Downloader extends AsyncTask<URL, Void, Bitmap>{

    public ImagesDownloader connector;

    @Override
    protected Bitmap doInBackground(URL... urls) {
        Bitmap bmImage = null;
        try {
            InputStream in = urls[0].openStream();
            bmImage = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return bmImage;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        connector.imageWasDownloaded(bitmap);
    }

}
