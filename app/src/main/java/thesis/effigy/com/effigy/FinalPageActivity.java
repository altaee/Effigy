package thesis.effigy.com.effigy;

import android.app.AlertDialog;
import android.app.DialogFragment;
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

import thesis.effigy.com.effigy.backend.GetBestScores;
import thesis.effigy.com.effigy.backend.GetTotalScore;
import thesis.effigy.com.effigy.interfaces.BestScoresProcessor;
import thesis.effigy.com.effigy.interfaces.ScoreUpdate;

import static thesis.effigy.com.effigy.config.ConfigConstants.PREFS_NAME;


public class FinalPageActivity extends AppCompatActivity implements ScoreUpdate, BestScoresProcessor{

    private TextView score;
    private String userName;
    private String[] namesForDialog;

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
        SharedPreferences sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userName = sharedPref.getString("USER_NAME", "");

        GetTotalScore total = new GetTotalScore(this, userName);
        total.execute();

        GetBestScores best = new GetBestScores(this, userName);
        best.execute();

        Button signOutButton = (Button) findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FinalPageActivity.this, LoginActivity.class));

            }
        });
        Button startAgainButton = (Button) findViewById(R.id.startAgainButton);
        startAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FinalPageActivity.this, MainActivity.class));

            }
        });
        Button pointsButton = (Button) findViewById(R.id.pointsButton);
        pointsButton.setEnabled(false);
    }
    public void onPointsView(View v, String[] names, String user) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = inflater.inflate(R.layout.points_list, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Scores");
        ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        TextView yourScore = (TextView) convertView.findViewById(R.id.your_score);
        yourScore.setText(user);
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
                DialogFragment dialog = new InformationDialog();
                dialog.show(getFragmentManager(), "information");
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

    @Override
    public void bestScoresReceived(final String[] names) {
        this.namesForDialog = new String[names.length-1];
        for(int i = 0;i < names.length-1;i++){
            this.namesForDialog[i] = names[i];
        }
        Button points = (Button) findViewById(R.id.pointsButton);
        points.setEnabled(true);
        points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String names[] = {"1. Anna: 26 points", "2. Boris: 24 points", "3. Camilla: 20 points", "4. Daniel: 18 points", "5. Zahraa: 17 points", "6. Natalia: 10 points"};
                onPointsView(view, namesForDialog, names[names.length-1]);
            }
        });
    }
}
