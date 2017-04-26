package focusfriend.focusfriend;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

}
        ArrayList<Class_Student> ethan= db.getAllStudent();
        String sethan=ethan.get(0).getUserID().toString();
        Log.d("Ethan1", sethan+ " "+ethan.get(0).SpendablePoints );

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
        db.updateStudent("efortin", "efortin@villanova.edu", "Ethan Fortin", "password", "Villanova", "CPE", "FocusFriend", "Engineer", "FrisbeeTeam", 200, 400);
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
                }

            }
        };
        time_handler.post(runnable);
    }
}
