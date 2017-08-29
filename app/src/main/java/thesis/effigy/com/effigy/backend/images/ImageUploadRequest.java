package thesis.effigy.com.effigy.backend.images;

import android.os.AsyncTask;
import android.util.Base64;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import thesis.effigy.com.effigy.data.SimilarImage;
import thesis.effigy.com.effigy.interfaces.images.FileUploader;

import static thesis.effigy.com.effigy.config.ConfigConstants.REQUEST_FILE;
import static thesis.effigy.com.effigy.helpers.SimilarImagesParser.parseJSON;

/**
 * Created by Borys on 1/6/17.
 */

public class ImageUploadRequest extends AsyncTask<String, Void, List<SimilarImage>> {
    private static final String FILE_UPLOAD_URL = REQUEST_FILE;
    public FileUploader connector;
    @Override
    protected List<SimilarImage> doInBackground(String... strings) {
        URL url = null;
        List<SimilarImage> images = new ArrayList<>();
        String fileString = strings[0];
        try {
            url = new URL(FILE_UPLOAD_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String responseString = "";
        HttpURLConnection urlConnection=null;
        try {
            try {

                MultipartEntityBuilder multipartEntity = MultipartEntityBuilder.create();
                multipartEntity.addPart("image", new FileBody(new File(fileString)));
                multipartEntity.addPart("UserName", new StringBody("randomUser", ContentType.TEXT_PLAIN));
                HttpEntity entity = multipartEntity.build();

                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setUseCaches(false);
                urlConnection.setDoOutput(true);

                urlConnection.setRequestMethod("POST");
                urlConnection.addRequestProperty("Content-length", entity.getContentLength() + "");
                urlConnection.addRequestProperty(entity.getContentType().getName(), entity.getContentType().getValue());

                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                entity.writeTo(wr);

                wr.flush();
                wr.close();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader r = new BufferedReader(new InputStreamReader(in));
                StringBuilder total = new StringBuilder();
                String line;
                while ((line = r.readLine()) != null) {
                    total.append(line);
                }
                responseString = total.toString();

                images = parseJSON(new JSONObject(responseString), -1);

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } finally {
            urlConnection.disconnect();
        }


        return images;
    }

    @Override
    protected void onPostExecute(List<SimilarImage> images) {
        super.onPostExecute(images);
        connector.imageWasUploaded(images);
    }

    private String toBase64(String filePath) throws FileNotFoundException {
        InputStream inputStream = new FileInputStream(filePath);
        byte[] bytes;
        byte[] buffer = new byte[8192];
        int bytesRead;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = output.toByteArray();

        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}
