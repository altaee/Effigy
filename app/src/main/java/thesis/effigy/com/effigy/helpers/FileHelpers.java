package thesis.effigy.com.effigy.helpers;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import thesis.effigy.com.effigy.Tab1Main;
import thesis.effigy.com.effigy.data.ParentImage;
import thesis.effigy.com.effigy.data.SimilarImage;

/**
 * Created by Borys on 1/26/17.
 */

public class FileHelpers {

    private String fileName;
    private Context context;

    public FileHelpers(String userName, Context context){
        this.fileName = userName;
        this.context = context;
    }

    public void writeToFile(ParentImage parent, List<SimilarImage> similar){
        File file = new File(context.getFilesDir(),fileName);
        OutputStream outputStream = null;
        String toWrite = fileName+"\n";
        try {
            outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            outputStream.write(toWrite.getBytes());
            outputStream.write((String.valueOf(parent.getParentId())+"\n").getBytes());
            outputStream.write((parent.getImageUrl()+"\n").getBytes());
            outputStream.write((String.valueOf(similar.size())+"\n").getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i=0;i<similar.size();i++){
            try {
                toWrite = String.valueOf(similar.get(i).getImageId())+"\n";
                outputStream.write(toWrite.getBytes());
                outputStream.write(String.valueOf(similar.get(i).getParentImageId()+"\n").getBytes());
                outputStream.write((similar.get(i).getImageUrl()+"\n").getBytes());
                outputStream.write(String.valueOf(similar.get(i).getRanking()+"\n").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public boolean readFromFile(Tab1Main tab){
        boolean result = true;
        List<SimilarImage> similar = new ArrayList<>();
        ParentImage parent;
        File file = new File(context.getFilesDir(),fileName);
        if(file.exists()){
            Scanner sc = null;
            try {
                sc = new Scanner(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            String user = sc.nextLine();
            long parentId = sc.nextLong();
            sc.nextLine();
            String parentURL = sc.nextLine();
            parent = new ParentImage(parentId,parentURL);

            int size = sc.nextInt();
            sc.nextLine();

            for(int i=0;i<size;i++){
                long id = sc.nextLong();
                sc.nextLine();
                long parId = sc.nextLong();
                sc.nextLine();
                String url = sc.nextLine();
                int ranking = sc.nextInt();
                SimilarImage sim = new SimilarImage(id, parId, url, ranking);
                similar.add(sim);
            }

            sc.close();
        } else return false;
        tab.setParentImage(parent, true);
        tab.setSimilarImages(similar);

        return result;
    }
}
