package thesis.effigy.com.effigy.interfaces.images;

import java.util.List;

import thesis.effigy.com.effigy.data.ParentImage;
import thesis.effigy.com.effigy.data.SimilarImage;

public interface ParentImageReceiver {
    void setParentImage(ParentImage parentImage);
    void setSimilarImages(List<SimilarImage> images);
}
