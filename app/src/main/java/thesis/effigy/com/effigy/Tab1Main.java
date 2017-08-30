package thesis.effigy.com.effigy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import thesis.effigy.com.effigy.adapters.SimilarImagesAdapter;
import thesis.effigy.com.effigy.backend.images.Downloader;
import thesis.effigy.com.effigy.backend.scores.GetTotalScore;
import thesis.effigy.com.effigy.backend.images.ParentImageRequest;
import thesis.effigy.com.effigy.backend.images.SimilarImageRequest;
import thesis.effigy.com.effigy.data.ParentImage;
import thesis.effigy.com.effigy.data.SimilarImage;
import thesis.effigy.com.effigy.helpers.FileHelpers;
import thesis.effigy.com.effigy.interfaces.images.ParentImageReceiver;
import thesis.effigy.com.effigy.interfaces.scores.ScoreUpdate;

import static thesis.effigy.com.effigy.config.ConfigConstants.PREFS_NAME;
import static thesis.effigy.com.effigy.helpers.SimilarImagesParser.updateSingleImages;


public class Tab1Main extends Fragment implements ParentImageReceiver, ScoreUpdate {

    private ParentImage parentImage;
    private List<SimilarImage> similarImages;
    private ViewPager viewPager;
    private SimilarImagesAdapter adapter;
    private Downloader downloader;
    private ParentImageRequest parentRequest;
    private FileHelpers helper;
    public String userName;
    private static final int QUANTITY = 4;
    View v;

    TextView score;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_main, container, false);

        similarImages = new ArrayList<>(QUANTITY);
        //Check if logged in
        checkPrefs();
        score = (TextView) rootView.findViewById(R.id.totalNumberOfEvaluations);
        GetTotalScore total = new GetTotalScore(this, userName);
        total.execute();

        this.helper = new FileHelpers(userName, getContext());
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        adapter = new SimilarImagesAdapter(this.getContext(), score, helper, this);
        adapter.imageResources = similarImages;
        adapter.notifyDataSetChanged();
        System.out.println("test");
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabDots);
        tabLayout.setupWithViewPager(viewPager, true);

        v = rootView;
        if(parentImage==null){
            boolean result = helper.readFromFile(this);
            if(result){
                Log.d("FILE", "File was loaded properly!");
            }else {
                parentRequest = new ParentImageRequest();
                parentRequest.connector = Tab1Main.this;
                parentRequest.execute(userName);
            }
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


//        ScrollView scroll = (ScrollView) rootView.findViewById(R.id.scroll_view_tab_1);
//        scroll.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                return (motionEvent.getAction() == MotionEvent.ACTION_MOVE);
//            }
//        });
        return rootView;
    }

    public ParentImage getParentImage(){
        return parentImage;
    }
    public List<SimilarImage> getSimilarImages(){
        return similarImages;
    }
    public void setFromFileParentImage(ParentImage pi){
        this.parentImage = pi;
    }
    public void setFromFileSimilarImages(List<SimilarImage> similar){
        this.similarImages = similar;
    }

    private void checkPrefs() {
        SharedPreferences sharedPref = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String userName = sharedPref.getString("USER_NAME", "");
        if(userName.isEmpty()){
            startActivity(new Intent (getActivity(), LoginActivity.class));
        }
        else this.userName = userName;
    }

    @Override
    public void setParentImage(ParentImage parentImage, boolean shortRequest) {
        if(parentImage!=null){
            this.parentImage = parentImage;
            if(shortRequest) this.parentImage.view = (ImageView) v.findViewById(R.id.parentImage);
            else this.parentImage.view = (ImageView) getView().findViewById(R.id.parentImage);

            downloader = new Downloader();
            downloader.connector = this.parentImage;

            if(!shortRequest){
                SimilarImageRequest similarReqest = new SimilarImageRequest();
                Long[] arr = new Long[2];
                arr[0] = parentImage.getParentId();
                arr[1] = Long.parseLong(String.valueOf(QUANTITY));
                similarReqest.connector = Tab1Main.this;
                similarReqest.execute(arr);

            }
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
    public void scoreWasUpdated(boolean success) {}

    @Override
    public void updateTotalScore(int totalScore) {
        int lastSpace = this.score.getText().toString().lastIndexOf(" ");
        String display = this.score.getText().toString().substring(0,lastSpace) + " " + totalScore;
        this.score.setText(display);
    }
}


