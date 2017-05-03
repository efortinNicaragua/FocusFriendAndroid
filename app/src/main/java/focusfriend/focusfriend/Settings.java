package focusfriend.focusfriend;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.Locale;

public class Settings extends AppCompatActivity {
    String classid,weekdays,userid;
    String st_year, st_month, st_day,st_hour,st_minute, e_year,e_month,e_day,e_hour,e_minute;
    Context context=this;
    final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);


    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void start_timer(View v)  {
        DBHandler db=new DBHandler(context);

        settings = getSharedPreferences(PREFS_NAME, 0);
        userid=settings.getString("my_username","default");


        EditText start_year=(EditText)findViewById(R.id.start_year);
        EditText start_month=(EditText)findViewById(R.id.start_month);
        EditText start_day=(EditText)findViewById(R.id.start_day);
        EditText start_hour=(EditText)findViewById(R.id.start_hour);
        EditText start_minute=(EditText)findViewById(R.id.start_minute);

        st_year=start_year.getText().toString();
        st_month=start_month.getText().toString();
        st_day=start_day.getText().toString();
        st_hour=start_hour.getText().toString();
        st_minute=start_minute.getText().toString();

        EditText end_year=(EditText)findViewById(R.id.end_year);
        EditText end_month=(EditText)findViewById(R.id.end_month);
        EditText end_day=(EditText)findViewById(R.id.end_day);
        EditText end_hour=(EditText)findViewById(R.id.end_hour);
        EditText end_minute=(EditText)findViewById(R.id.end_Minute);

        e_year=end_year.getText().toString();
        e_month=end_month.getText().toString();
        e_day=end_day.getText().toString();
        e_hour=end_hour.getText().toString();
        e_minute=end_minute.getText().toString();

        EditText t_classid=(EditText)findViewById(R.id.edit_classid);
        EditText t_weekdays=(EditText)findViewById(R.id.edit_daysoftheweek);

        classid=t_classid.getText().toString();
        weekdays=t_weekdays.getText().toString();

        String dtStart=st_year+"-"+st_month+"-"+st_day+"T"+st_hour+":"+st_minute+"";
        Log.d("Ethan dt start",dtStart);
        String dtEnd=e_year+"-"+e_month+"-"+e_day+"T"+e_hour+":"+e_minute;
        Log.d("Ethan et start",dtEnd);

        db.addClass(userid,classid,dtStart,dtEnd,weekdays);
        Toast.makeText(context, "Succesfully updated", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,MainMenu.class);
        intent.putExtra("studysession",true);
        startActivity(intent);
    }
}
