package thesis.effigy.com.effigy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import thesis.effigy.com.effigy.backend.users.RegistrationTask;
import thesis.effigy.com.effigy.interfaces.users.RegistrationInterface;

import static thesis.effigy.com.effigy.config.ConfigConstants.PREFS_NAME;

public class RegisterDialogFragment extends DialogFragment implements RegistrationInterface{

    private SharedPreferences sharedPref;

    public Activity act;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View dialogView = inflater.inflate(R.layout.dialog_register, null);

        final EditText inputEmail = (EditText) dialogView.findViewById(R.id.input_email);
        final EditText mPassword = (EditText) dialogView.findViewById(R.id.input_password);
        final EditText mPasswordConfirm = (EditText) dialogView.findViewById(R.id.input_password_confirm);
        final EditText inputAge = (EditText) dialogView.findViewById(R.id.input_age);

        sharedPref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        this.act = getActivity();

        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.register, null)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RegisterDialogFragment.this.getDialog().cancel();
                    }
                })
                // Set title
                .setTitle(R.string.register);

        final Dialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                Button b = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // register the user ...
                        //mEmailView.setError(getString(R.string.error_field_required));
                        if(validateUserInput(inputEmail, mPassword, mPasswordConfirm, inputAge))
                        {
                            RegistrationTask task = new RegistrationTask(RegisterDialogFragment.this);
                            JSONObject data = buildJSON(inputEmail.getText().toString(), mPassword.getText().toString(), Integer.parseInt(inputAge.getText().toString()) );
                            task.execute(data);
                            dialogInterface.dismiss();
                        }
                    }
                });
            }
        });

        return dialog;
    }

    public boolean validateUserInput(EditText inputEmail, EditText mPassword, EditText mPasswordConfirm, EditText inputAge){
        boolean valid = true;
        if(inputEmail.getText().toString().isEmpty()){
            inputEmail.setError(getString(R.string.error_field_required));
            valid = false;
        }
        if(mPassword.getText().toString().isEmpty()){
            mPassword.setError(getString(R.string.error_field_required));
            valid = false;
        }
        else if(mPasswordConfirm.getText().toString().isEmpty()){
            mPasswordConfirm.setError(getString(R.string.error_field_required));
            valid = false;
        }
        else if(!mPassword.getText().toString().equals(mPasswordConfirm.getText().toString())){
            mPasswordConfirm.setError(getString(R.string.error_field_confirm_pass));
            valid = false;
        }
        if(inputAge.getText().toString().isEmpty()){
            inputAge.setError(getString(R.string.error_field_required));
            valid = false;
        }
        return valid;
    }

    public JSONObject buildJSON(String email, String passwd, int age ){
        JSONObject registration = new JSONObject();
        try {
            registration.put("email", email);
            registration.put("password", passwd);
            registration.put("age", age);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return registration;
    }

    @Override
    public void registrationResult(String result) {
        Log.e("REGISTRATION_ERROR", "Something went wrong during the registration");
    }

    @Override
    public void registrationResult(JSONObject result) {
        try {
            String token = result.getString("token");
            String userName = result.getString("username");
            changePrefs(token, userName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void changePrefs(String token, String userName){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("TOKEN", token);
        editor.putString("USER_NAME", userName);
        editor.apply();

        act.startActivity(new Intent(act, MainActivity.class));
    }
}
