package thesis.effigy.com.effigy;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class SimilarImagesAdapter extends PagerAdapter{

    private int[] imageResources = {R.drawable.reddress1, R.drawable.reddress3, R.drawable.reddress4,
            R.drawable.reddress3};
    private Context context;
    private LayoutInflater layoutInflater;

    public SimilarImagesAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageResources.length;
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
        imageView.setImageResource(imageResources[position]);
        textView.setText("Similar Image: "+position);
        ratingBar.getRating();
        container.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);

    }
}
