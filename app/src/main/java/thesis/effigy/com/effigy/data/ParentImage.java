package thesis.effigy.com.effigy.data;

import android.graphics.Bitmap;
import android.widget.ImageView;

import thesis.effigy.com.effigy.interfaces.images.ImagesDownloader;

/**
 * Created by Borys on 1/4/17.
 */

public class ParentImage implements ImagesDownloader{
    private final long parentId;
    private final String imageUrl;

    public ImageView view;

    public ParentImage( long parentId, String imageUrl) {
        this.parentId = parentId;
        this.imageUrl = imageUrl;
    }

    public long getParentId() {
        return parentId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @Override
    public String toString() {
        return "ParentImage{" +
                "parentId='" + parentId + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    @Override
    public void imageWasDownloaded(Bitmap image) {
        this.view.setImageBitmap(image);
    }
}
