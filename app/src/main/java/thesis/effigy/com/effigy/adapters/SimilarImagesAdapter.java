package thesis.effigy.com.effigy.adapters;

import android.content.Context;
import android.content.SharedPreferences;
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
import thesis.effigy.com.effigy.backend.scores.GetTotalScore;
import thesis.effigy.com.effigy.backend.scores.SetScore;
import thesis.effigy.com.effigy.data.SimilarImage;
import thesis.effigy.com.effigy.interfaces.score_interfaces.ScoreUpdate;

import static thesis.effigy.com.effigy.config.ConfigConstants.PREFS_NAME;

public class SimilarImagesAdapter extends PagerAdapter implements ScoreUpdate{

    public List<SimilarImage> imageResources = new ArrayList<SimilarImage>();
    private Context context;
    private TextView totalScore;
    private String userName;
    private LayoutInflater layoutInflater;

    public SimilarImagesAdapter(Context context, TextView totalScore)
    {
        this.context = context;
        this.totalScore = totalScore;
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userName = sharedPref.getString("USER_NAME", "");
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
    public Object instantiateItem(final ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.swipe_similar_images,container,false);
        ImageView imageView = (ImageView)item_view.findViewById(R.id.similar_image);
        TextView textView = (TextView)item_view.findViewById(R.id.image_count);
        RatingBar ratingBar = (RatingBar)item_view.findViewById(R.id.rating_bar);
       // TabLayout tabLayout = (TabLayout) item_view.findViewById(R.id.tabDots);


        imageView.setImageBitmap(imageResources.get(position).getImage());
        textView.setText("Similar Image:");
        ratingBar.setRating(imageResources.get(position).getRanking());

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                imageResources.get(position).setRanking(Math.round(v));
                //Call for score and update total score
                if(imageResources.get(position).getParentImageId() > -1){
                    SetScore scoreSetter = new SetScore(SimilarImagesAdapter.this, userName, position);
                    scoreSetter.execute(imageResources.get(position));
                }
            }
        });

        //tabLayout.getTabCount();
        container.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);

    }

    @Override
    public void scoreWasUpdated(boolean success) {
        if(success){
            GetTotalScore total = new GetTotalScore(this, userName);
            total.execute();
        }
    }

    @Override
    public void updateTotalScore(int totalScore) {
        int lastSpace = this.totalScore.getText().toString().lastIndexOf(" ");
        String display = this.totalScore.getText().toString().substring(0,lastSpace) + " " + totalScore;
        this.totalScore.setText(display);
    }
}
