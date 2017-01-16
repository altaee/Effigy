package thesis.effigy.com.effigy.adapters;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import thesis.effigy.com.effigy.R;
import thesis.effigy.com.effigy.data.SimilarImage;

public class SimilarImagesAdapter extends PagerAdapter{

    public List<SimilarImage> imageResources = new ArrayList<SimilarImage>();
    private Context context;
    private LayoutInflater layoutInflater;

    public SimilarImagesAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageResources.size();
    }

    @Override
    public int getItemPosition(Object object) {
//        return super.getItemPosition(object);
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view== object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.swipe_similar_images,container,false);
        ImageView imageView = (ImageView)item_view.findViewById(R.id.similar_image);
        TextView textView = (TextView)item_view.findViewById(R.id.image_count);
        RatingBar ratingBar = (RatingBar)item_view.findViewById(R.id.rating_bar);
       // TabLayout tabLayout = (TabLayout) item_view.findViewById(R.id.tabDots);


        imageView.setImageBitmap(imageResources.get(position).getImage());
        textView.setText("Similar Image:");
        ratingBar.getRating();
        //tabLayout.getTabCount();
        container.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);

    }
}
