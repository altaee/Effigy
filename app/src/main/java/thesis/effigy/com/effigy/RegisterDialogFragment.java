package thesis.effigy.com.effigy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class RegisterDialogFragment extends DialogFragment {
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = inflater.inflate(R.layout.dialog_register, null);
        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.register, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // register the user ...
                        startActivity(new Intent(getActivity(), MainActivity.class));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RegisterDialogFragment.this.getDialog().cancel();
                    }
                })
                // Set title
                .setTitle(R.string.register);

        final EditText mPassword = (EditText) dialogView.findViewById(R.id.input_password);
        final EditText mPasswordConfirm = (EditText) dialogView.findViewById(R.id.input_password_confirm);

        mPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (!mPassword.getText().toString().isEmpty()) {
                        mPasswordConfirm.setEnabled(true);
                    } else {
                        mPasswordConfirm.setEnabled(false);
                        mPasswordConfirm.setText("");
                    }
                }
            }
        });

        mPasswordConfirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (mPassword.getText().toString().equals(mPasswordConfirm.getText().toString())){

                } else {

                }
            }
        });

        return builder.create();
    }
}
