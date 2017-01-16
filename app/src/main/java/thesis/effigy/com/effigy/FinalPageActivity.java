package thesis.effigy.com.effigy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import thesis.effigy.com.effigy.backend.GetTotalScore;
import thesis.effigy.com.effigy.interfaces.ScoreUpdate;

import static thesis.effigy.com.effigy.helpers.SimpleDialogCreator.createInfoDialog;

public class FinalPageActivity extends AppCompatActivity implements ScoreUpdate{

    private TextView score;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
        TextView textView = new TextView(this);
        ImageView img = new ImageView(this);
        textView.setTextSize(40);
        textView.setText(message);

        ViewGroup layout = (ViewGroup) findViewById(R.id.activity_final_page);
        layout.addView(textView);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.effigylogo);

        score = (TextView) findViewById(R.id.bigText);
        SharedPreferences sharedPref = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        userName = sharedPref.getString("USER_NAME", "");

        GetTotalScore total = new GetTotalScore(this, userName);
        total.execute();

        Button SignOutButton = (Button) findViewById(R.id.signOutButton);
        SignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FinalPageActivity.this, LoginActivity.class));

            }
        });

        Button StartAgainButton = (Button) findViewById(R.id.startAgainButton);
        StartAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FinalPageActivity.this, MainActivity.class));

            }
        });

        Button PointsButton = (Button) findViewById(R.id.pointsButton);
        PointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String names[] = {"1. Anna: 26 points", "2. Boris: 24 points", "3. Camilla: 20 points", "4. Daniel: 18 points", "5. Zahraa: 17 points", "6. Natalia: 10 points"};
                onPointsView(view, names);
            }
        });
    }
    public void onPointsView(View v, String[] names) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.points_list, null);
      //  View convertView = inflater.inflate(R.layout.points_list, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Scores");
        ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        lv.setAdapter(adapter);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id)
        {
            case R.id.action_settings:
            {
                Intent intent = new Intent();
                intent.setClassName(this, "thesis.effigy.com.effigy.SettingsActivity");
                startActivity(intent);
                return true;
            }
            case R.id.action_info: {
                android.support.v7.app.AlertDialog alertDialog = createInfoDialog(FinalPageActivity.this);
                alertDialog.show();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void scoreWasUpdated(boolean success) {
        return;
    }

    @Override
    public void updateTotalScore(int totalScore) {
        String text = getResources().getString(R.string.large_text);
        String text2 = getResources().getString(R.string.large_text2);
        String display = text + " " + totalScore + " " + text2;
        this.score.setText(display);
    }
}
