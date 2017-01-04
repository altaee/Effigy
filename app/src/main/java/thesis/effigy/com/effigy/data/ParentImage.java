package thesis.effigy.com.effigy.data;

/**
 * Created by Borys on 1/4/17.
 */

public class ParentImage {
    private final long parentId;
    private final String imageUrl;

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
}
