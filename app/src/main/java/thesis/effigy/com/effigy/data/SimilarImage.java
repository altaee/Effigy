package thesis.effigy.com.effigy.data;

import android.graphics.Bitmap;

import thesis.effigy.com.effigy.adapters.SimilarImagesAdapter;
import thesis.effigy.com.effigy.interfaces.ImagesDownloader;

/**
 * Created by Borys on 1/4/17.
 */

public class SimilarImage implements ImagesDownloader{
    private final long imageId;
    private final long parentImageId;
    private final String imageUrl;
    private Integer ranking;

    private Bitmap image;
    public SimilarImagesAdapter adapter;

    public SimilarImage(long imageId,
                        long parentImageId,
                        String imageUrl,
                        Integer ranking) {

        this.imageId = imageId;
        this.parentImageId = parentImageId;
        this.imageUrl = imageUrl;
        this.ranking = ranking;
    }

    public Bitmap getImage(){
        return this.image;
    }
    public void setImage(Bitmap img){
        this.image = img;
    }
    public long getImageId() {
        return imageId;
    }
    public long getParentImageId(){
        return parentImageId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getRanking() {
        return ranking;
    }
    public void setRanking(int r){
        this.ranking = r;
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
        this.image = image;
        this.adapter.notifyDataSetChanged();
    }
}
