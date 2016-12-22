package thesis.effigy.com.effigy.helpers;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by Borys on 12/22/16.
 */

public class SimpleDialogCreator {
    public static AlertDialog createInfoDialog(Activity toCreate){
        AlertDialog alertDialog = new AlertDialog.Builder(toCreate).create();
        alertDialog.setTitle("Information");
        alertDialog.setMessage("Usage information for the app");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return alertDialog;
    }
}
