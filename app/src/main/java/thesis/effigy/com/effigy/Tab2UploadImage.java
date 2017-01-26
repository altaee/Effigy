package thesis.effigy.com.effigy;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import thesis.effigy.com.effigy.adapters.SimilarImagesAdapter;
import thesis.effigy.com.effigy.backend.images.ImageUploadRequest;
import thesis.effigy.com.effigy.data.SimilarImage;
import thesis.effigy.com.effigy.interfaces.image_interfaces.FileUploader;

import static android.app.Activity.RESULT_OK;
import static thesis.effigy.com.effigy.helpers.SimilarImagesParser.updateSingleImages;


public class Tab2UploadImage extends Fragment implements FileUploader{

    private static final int RESULT_LOAD_IMG = 99;
    private List<SimilarImage> similarImages;

    private ImageUploadRequest imageUploadRequest;

    private ViewPager viewPager;
    private SimilarImagesAdapter adapter;
    private static final int QUANTITY = 5;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab2_upload_image, container, false);

        similarImages = new ArrayList<>();

        if(adapter==null) {
            viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
            adapter = new SimilarImagesAdapter(this.getContext(), null);
            adapter.imageResources = similarImages;
            adapter.notifyDataSetChanged();
            viewPager.setAdapter(adapter);
            TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabDots);
            tabLayout.setupWithViewPager(viewPager, true);

        }



        Button UploadImage = (Button) rootView.findViewById(R.id.uploadButton);
        UploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });
        return rootView;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContext().getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                ImageView imgView = (ImageView) getView().findViewById(R.id.uploadImage1);
                // Set the Image in ImageView after decoding the String
                imgView.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));



                String[] strings = new String[2];
                strings[0] = imgDecodableString;
                imageUploadRequest = new ImageUploadRequest();
                imageUploadRequest.connector = Tab2UploadImage.this;
                imageUploadRequest.execute(strings);

            } else {
                Snackbar.make(getView(), "You haven't picked an Image",
                        Snackbar.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Snackbar.make(getView(), "Something went wrong", Snackbar.LENGTH_LONG)
                    .show();
            e.printStackTrace();
        }

    }

    @Override
    public void imageWasUploaded(List<SimilarImage> images) {
        this.similarImages = new ArrayList<>(QUANTITY);
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
}
