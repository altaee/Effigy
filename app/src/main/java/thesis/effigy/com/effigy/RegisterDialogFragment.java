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
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import thesis.effigy.com.effigy.backend.RegistrationTask;
import thesis.effigy.com.effigy.interfaces.RegistrationInterface;

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

        sharedPref = getActivity().getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        this.act = getActivity();

        builder.setView(dialogView)
                // Add action buttons
                .setPositiveButton(R.string.register, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // register the user ...
                        if(inputEmail.getText().toString().isEmpty() || mPassword.getText().toString().isEmpty()
                                || mPasswordConfirm.getText().toString().isEmpty() || inputAge.getText().toString().isEmpty()
                                || mPassword.getText().toString().equals(mPasswordConfirm.getText().toString()))
                        {
                            RegistrationTask task = new RegistrationTask(RegisterDialogFragment.this);
                            JSONObject data = buildJSON(inputEmail.getText().toString(), mPassword.getText().toString(), Integer.parseInt(inputAge.getText().toString()) );
                            task.execute(data);
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RegisterDialogFragment.this.getDialog().cancel();
                    }
                })
                // Set title
                .setTitle(R.string.register);

        mPasswordConfirm.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!mPassword.getText().toString().equals(mPasswordConfirm.getText().toString())){
                    mPassword.setError("Confirmation is different than password!");
                } else {

                }
            }
        });

        return builder.create();
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
        Snackbar.make(getView(), "Something went wrong :( Try again later!",
                Snackbar.LENGTH_LONG).show();
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
        editor.putString("TOKEN", (String) token );
        editor.putString("USER_NAME", (String) userName );
        editor.apply();

        act.startActivity(new Intent(act, MainActivity.class));
    }
}
