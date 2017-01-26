package thesis.effigy.com.effigy.interfaces.images;

import java.util.List;

import thesis.effigy.com.effigy.data.SimilarImage;

/**
 * Created by Borys on 1/6/17.
 */

public interface FileUploader {
    public void imageWasUploaded(List<SimilarImage> result);
}
