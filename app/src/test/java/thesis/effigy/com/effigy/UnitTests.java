package thesis.effigy.com.effigy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import thesis.effigy.com.effigy.data.ParentImage;
import thesis.effigy.com.effigy.data.SimilarImage;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * Created by Tomek on 24.01.2017.
 */

@RunWith(MockitoJUnitRunner.class)
public class UnitTests {

    @Test
    public void parentImageTests() {
        ParentImage parentImage = new ParentImage(965478952,"http://adress.com/parent.png");

        assertThat(parentImage.toString(), is("ParentImage{parentId='965478952', imageUrl='http://adress.com/parent.png'}"));
        assertThat(parentImage.getImageUrl(), is("http://adress.com/parent.png"));
        assertEquals(parentImage.getParentId(), 965478952);
    }

    @Test
    public void similarImageTests() {
        SimilarImage similarImage = new SimilarImage(123456789,965478952,"http://adress.com/img.png",1);

        assertThat(similarImage.toString(), is("SimilarImage{imageId='123456789', imageUrl='http://adress.com/img.png', ranking=1}"));

        similarImage.setRanking(2);
        assertThat(similarImage.toString(), is("SimilarImage{imageId='123456789', imageUrl='http://adress.com/img.png', ranking=2}"));
        assertThat(similarImage.getImageUrl(), is("http://adress.com/img.png"));
        assertEquals(similarImage.getRanking(), (Integer)2);
        assertEquals(similarImage.getImageId(), 123456789);
    }

}
