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
    private long time_remaining = 60000;
    private long time_seconds;
    Context context=this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_session);
        timer_start = (Button) findViewById(R.id.start_timer);
        timer = (TextView) findViewById(R.id.study_timeremaining);



    }
    public void start_timer(View view){
        time_handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("VIVZ", "Run was called");
                time_remaining = time_remaining - 1000;

                if (time_remaining > 0) {
                    time_handler.postDelayed(this, 1000);
                    time_seconds=time_remaining/1000;
                    timer.setText(""+time_seconds);
                }
                else if (time_remaining==0){
                }
            }
        };
        time_handler.post(runnable);
    }

}
