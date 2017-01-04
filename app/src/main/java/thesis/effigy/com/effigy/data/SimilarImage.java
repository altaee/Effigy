package thesis.effigy.com.effigy.data;

import android.graphics.Bitmap;
import android.widget.ImageView;

import thesis.effigy.com.effigy.interfaces.ImagesDownloader;

/**
 * Created by Borys on 1/4/17.
 */

public class SimilarImage implements ImagesDownloader{
    private final long imageId;
    private final String imageUrl;
    private final Integer ranking;

    public ImageView view;

    public SimilarImage(long imageId,
                        String imageUrl,
                        Integer ranking) {

        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.ranking = ranking;
    }

    public long getImageId() {
        return imageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getRanking() {
        return ranking;
    }

    @Override
    public String toString() {
        return "SimilarImage{" +
                "imageId='" + imageId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", ranking=" + ranking +
                '}';
    }

    @Override
    public void imageWasDownloaded(Bitmap image) {
        this.view.setImageBitmap(image);
    }
}
