package thesis.effigy.com.effigy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import thesis.effigy.com.effigy.adapters.SimilarImagesAdapter;
import thesis.effigy.com.effigy.backend.Downloader;
import thesis.effigy.com.effigy.backend.GetTotalScore;
import thesis.effigy.com.effigy.backend.ParentImageRequest;
import thesis.effigy.com.effigy.backend.SimilarImageRequest;
import thesis.effigy.com.effigy.data.ParentImage;
import thesis.effigy.com.effigy.data.SimilarImage;
import thesis.effigy.com.effigy.interfaces.ParentImageReceiver;
import thesis.effigy.com.effigy.interfaces.ScoreUpdate;

import static thesis.effigy.com.effigy.helpers.SimilarImagesParser.updateSingleImages;


public class Tab1Main extends Fragment implements ParentImageReceiver, ScoreUpdate {

    private ParentImage parentImage;
    private List<SimilarImage> similarImages;

    private ViewPager viewPager;
    private SimilarImagesAdapter adapter;
    private RatingBar ratingBar;

    private Downloader downloader;
    private ParentImageRequest parentRequest;
    public String userName;
    private static final int QUANTITY = 5;

    TextView score;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_main, container, false);

        ratingBar = (RatingBar)rootView.findViewById(R.id.rating_bar);

//        if(similarImages==null){
//            similarImages = new ArrayList<>(QUANTITY);
//        }

        similarImages = new ArrayList<>(QUANTITY);
        //Check if logged in
        checkPrefs();
        score = (TextView) rootView.findViewById(R.id.totalNumberOfEvaluations);
        GetTotalScore total = new GetTotalScore(this, userName);
        total.execute();

        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        adapter = new SimilarImagesAdapter(this.getContext(), score);
        adapter.imageResources = similarImages;
        adapter.notifyDataSetChanged();
        System.out.println("test");
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager, true);

        if(parentImage==null){
            parentRequest = new ParentImageRequest();
            parentRequest.connector = Tab1Main.this;
            parentRequest.execute(userName);
        }
        Button finishButton = (Button) rootView.findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), FinalPageActivity.class));
            }
        });
        rootView.findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                parentRequest = new ParentImageRequest();
                parentRequest.connector = Tab1Main.this;
                parentRequest.execute(userName);
            }
        });


        return rootView;
    }

    private void checkPrefs() {
        SharedPreferences sharedPref = getContext().getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        String userName = sharedPref.getString("USER_NAME", "");
        if(userName.isEmpty()){
            startActivity(new Intent (getActivity(), LoginActivity.class));
        }
        else this.userName = userName;
    }

    @Override
    public void setParentImage(ParentImage parentImage) {
        if(parentImage!=null){
            this.parentImage = parentImage;
            this.parentImage.view = (ImageView) getView().findViewById(R.id.parentImage);

            downloader = new Downloader();
            downloader.connector = this.parentImage;

            SimilarImageRequest similarReqest = new SimilarImageRequest();
            Long[] arr = new Long[2];
            arr[0] = parentImage.getParentId();
            arr[1] = Long.parseLong(String.valueOf(QUANTITY));
            similarReqest.connector = Tab1Main.this;
            similarReqest.execute(arr);

            URL[] link = new URL[1];
            try {
                link[0] = new URL(parentImage.getImageUrl());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            downloader.execute(link);
        }
        else{
            Snackbar.make(getView(), "Something went wrong, try again later",
                    Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void setSimilarImages(List<SimilarImage> images) {
        similarImages = new ArrayList<>(QUANTITY);
        this.similarImages = images;
        adapter.notifyDataSetChanged();
        if(images.size()>0){
            updateSingleImages(similarImages, adapter);
        }
        else{
            Snackbar.make(getView(), "Something went wrong, try again later",
                    Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void scoreWasUpdated(boolean success) {
        return;
    }

    @Override
    public void updateTotalScore(int totalScore) {
        String display = this.score.getText().toString().split(" ")[0] + " " + totalScore;
        this.score.setText(display);
    }
}


