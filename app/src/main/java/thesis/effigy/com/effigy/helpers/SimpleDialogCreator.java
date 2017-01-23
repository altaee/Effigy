package thesis.effigy.com.effigy.helpers;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.Html;


/**
 * Created by Borys on 12/22/16.
 */

public class SimpleDialogCreator {
    public static AlertDialog createInfoDialog(Activity toCreate){
        AlertDialog alertDialog = new AlertDialog.Builder(toCreate).create();
        alertDialog.setTitle("Information");
        alertDialog.setMessage(Html.fromHtml("<b>Assessment</b>"+
                "\n" + "Your job is to assess the similarity of the colours and pattern in the four images presented based on the larger original image above.\n" +
                "\n" + "<b>Rating</b>"+
                "You are able to rate the image by choosing 1-5 from the five starts shown below, the right most star (5th one) is the highest rate and lowest is on the left most one (1st)\n" +
                "\n" +
                "After evaluating the images below you can click the next button and continue to rate the next set of images.\n" +
                "\n" +
                "Thank You!"));



        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return alertDialog;
    }
}
