package focusfriend.focusfriend;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StudySession extends AppCompatActivity {

    private Button timer_start;
    private TextView timer;
    private Handler time_handler;
    private long time_remaining = 5000;
    private long time_seconds;
    int first =0;
    Context context=this;
    DBHandler db=new DBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_session);
        timer_start = (Button) findViewById(R.id.start_timer);
        timer = (TextView) findViewById(R.id.study_timeremaining);
if (first ==1 ){

    Log.d("Insert: ", "Inserting...");
    db.addStudent("efortin", "efortin@villanova.edu", "Ethan Fortin", "password", "Villanova", "CPE", "FocusFriend", "Engineer", "FrisbeeTeam", 100, 200);
    db.addStudent("mlopez15", "mlopez15@villanova.edu", "Madeline Lopez", "password", "Villanova", "EE", "FocusFriend", "Engineer", "SAE I'ts a Frat", 0, 1000);

}
        String ethan= db.getAllStudent().toString();
        Log.d("Ethan1", ethan);

    }
    public void start_timer(View view){
        time_handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("Ethan8", "Run was called");


                if (time_remaining > 0) {
                    time_seconds=time_remaining/1000;
                    timer.setText(""+time_seconds);
                    time_remaining = time_remaining - 1000;
                    time_handler.postDelayed(this, 1000);

                }
                else if (time_remaining==0){
                    time_seconds=time_remaining/1000;
                    timer.setText(""+time_seconds);
                }

            }
        };
        time_handler.post(runnable);
    }

    public void StudySessionComplete(View view){
        db.updateStudent("efortin", "efortin@villanova.edu", "Ethan Fortin", "password", "Villanova", "CPE", "FocusFriend", "Engineer", "FrisbeeTeam", 200, 400);
        String ethan= db.getAllStudent().toString();
        Log.d("Ethan2", ethan);

    }
}
