package focusfriend.focusfriend;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class StudySession extends AppCompatActivity {

    private Button timer_start;
    private TextView timer;
    private Handler time_handler;
    private long time_remaining;
    private long time_seconds;
    int add_points =100;
    Context context=this;
    DBHandler db=new DBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_session);
        timer_start = (Button) findViewById(R.id.start_timer);
        timer = (TextView) findViewById(R.id.study_timeremaining);

    }
    public void start_timer(View view){
        time_remaining = 5000;
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
                    StudySessionComplete();
                }

            }
        };
        time_handler.post(runnable);
    }

    public void StudySessionComplete(){
            SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
            DBHandler db=new DBHandler(context);

            ArrayList<Class_Student> temp=db.getStudent(settings.getString("my_username","default"));
            int SpendablePoints=temp.get(0).SpendablePoints+add_points;
            int TotalPoints=temp.get(0).TotalPoints+add_points;

        db.updateStudent(temp.get(0).UserID,temp.get(0).Email,temp.get(0).FullName,temp.get(0).Password,temp.get(0).University,temp.get(0).Major,
                temp.get(0).Group1,temp.get(0).Group2,temp.get(0).Group3,SpendablePoints,TotalPoints);

        String ethan= db.getAllStudent().toString();
        Log.d("Ethan2", ethan);


        new AlertDialog.Builder(context)
                .setTitle("Study Session Complete")
                .setMessage("Would you like to continue for another 45 Minutes")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Restart_timer();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }
    public void Restart_timer(){
        time_remaining = 5000;
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
                    StudySessionComplete();
                    toasty();
                }

            }
        };
        time_handler.post(runnable);
    }
    public void toasty(){
        Toast.makeText(context, add_points+" added to your account", Toast.LENGTH_SHORT).show();
    }
}
