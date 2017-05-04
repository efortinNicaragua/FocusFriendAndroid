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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainMenu extends AppCompatActivity {
    //basic intializations
    final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences settings;
    Context context=this;
    Button existinguser, newuser, login;
    EditText et_name, et_email, et_username, et_password, exuser_username, exuser_password;
    Dialog dialog_newuser, dialog_existinguser;
    double time_remaining;
    boolean cont,pressed;
    int toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        cont=true;
        toast=1;

        //set setting=to shared preferences
        settings = getSharedPreferences(PREFS_NAME, 0);

        //if false we don't know who the user is
        if (check_credentials()==false) {

            //Dialog pops us and allows user to create new user or log in
                    dialog_newuser= new Dialog(this);
                    dialog_newuser.setTitle("Enter Personal Info");
                    dialog_newuser.setContentView(R.layout.dialog_newuser);
                    dialog_newuser.show();

            //set views
            existinguser=(Button) dialog_newuser.findViewById(R.id.existing_user);
            newuser=(Button) dialog_newuser.findViewById(R.id.create_newuser);
            et_name=(EditText) dialog_newuser.findViewById(R.id.edit_fullName);
            et_email=(EditText) dialog_newuser.findViewById(R.id.edit_email);
            et_username=(EditText) dialog_newuser.findViewById(R.id.edit_username);
            et_password=(EditText) dialog_newuser.findViewById(R.id.edit_password);

            //new user on click gets info entered and pushes it to the db
            newuser.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    String name, email, username, password;
                    name=et_name.getText().toString();
                    email=et_email.getText().toString();
                    username=et_username.getText().toString();
                    password=et_password.getText().toString();

                    //db insert puts the hardcoded people into the db
                    db_insert();

                    //we put new user in db and save their username and password to our preferences, this will allow for autologin later
                    DBHandler db=new DBHandler(context);
                    db.addStudent(username, email, name, password, "Villanova", "", "", "", "", 0, 0);
                    dialog_newuser.cancel();
                    Toast.makeText(context, "New User Created!", Toast.LENGTH_SHORT).show();


                    settings.edit().putString("my_username", username).commit();
                    settings.edit().putString("my_password",password).commit();

                    //updates text view with total points
                    updatePoints();
                    checkClass();
                }
            });

            //this button is found on the login dialog and allows the user to login to existing account
           existinguser.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                   //puts hardcoded people into db
                    db_insert();
                    Log.d("Insert: ", "Inserting...");

                    //Closes old dialog and pops up log in with existing user dialog
                    dialog_newuser.cancel();
                    dialog_existinguser= new Dialog(context);
                    dialog_existinguser.setTitle("Enter Personal Info");
                    dialog_existinguser.setContentView(R.layout.dialog_existinguser);
                    dialog_existinguser.show();

                    login=(Button) dialog_existinguser.findViewById(R.id.login);
                    exuser_username=(EditText) dialog_existinguser.findViewById(R.id.edit1_username);
                    exuser_password=(EditText)dialog_existinguser.findViewById(R.id.edit1_password);

                    //when login button is clicked check credentials to see if they are real
                    login.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view) {
                            String Username=exuser_username.getText().toString();
                            String User_Password=exuser_password.getText().toString();
                            ArrayList<String> passwords;

                            DBHandler db=new DBHandler(context);
                            passwords=db.login(Username);
                            Log.d("Ethan Password:",User_Password);
                            Log.d("Ethan Passwords:",passwords.get(0).toString());


                            //if we returned a password then we check to see if they mathch
                            if(passwords.size()>0) {

                                //if they match save to preferences so we can get them later
                                if (passwords.get(0).toString().equals(User_Password)) {
                                    settings.edit().putString("my_username", Username).commit();
                                    settings.edit().putString("my_password", User_Password).commit();
                                    String tempu=settings.getString("my_username","default");
                                    String tempp=settings.getString("my_password","default");
                                    Log.d("Ethan Passwords:", "Existing user found");
                                    Log.d("Ethan SP:", tempu+ " " + tempp);

                                    dialog_existinguser.cancel();
                                    Toast.makeText(context, "Login Succes", Toast.LENGTH_LONG).show();
                                    updatePoints();
                                    checkClass();
                                } else {
                                    Toast.makeText(MainMenu.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(MainMenu.this, "Login Failed", Toast.LENGTH_SHORT).show();
                            }

                        }



                    });

                }
            });
        }

    }
    //checks stored preferences credentials
    public boolean check_credentials(){
        Log.d("Ethan","Got here ");
        String Username=settings.getString("my_username","default");
        String User_Password=settings.getString("my_password","default");
        ArrayList<String> passwords;
        Log.d("Ethan","Made it here ");
        DBHandler db=new DBHandler(context);
        passwords=db.login(Username);

        if(passwords.size()>0) {

            Log.d("Ethan Password:", Username);
            Log.d("Ethan Passwords:", User_Password);

            if (passwords.get(0).toString().equals(User_Password)) {
                Log.d("Ethan Passwords:", "Successss AutoLOGIN");

                boolean settings_return=false;
                if(getIntent().hasExtra("studysession")==true){
                 settings_return=getIntent().getExtras().getBoolean("studysession");}
                if(settings_return==false){Toast.makeText(context, "Auto Login Succes", Toast.LENGTH_LONG).show();}
                updatePoints();
                checkClass();
                return true;
            } else {
                Log.d("Ethan Passwords:", "Fail");
                return false;
            }
        }
        else{
            return false;
        }

    }
    public void checkClass(){
        DBHandler db=new DBHandler(context);

        ArrayList<Class_Class> temp=db.getClassForUser(settings.getString("my_username","default"));
        int day_of_week=0;

        Date date = Calendar.getInstance().getTime();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int c_dow = c.get(Calendar.DAY_OF_WEEK);
        int c_year =c.get(Calendar.YEAR);
        int c_month=c.get(Calendar.MONTH)+1;
        int c_day=c.get(Calendar.DAY_OF_MONTH);
        double c_hour=c.get(Calendar.HOUR_OF_DAY);
        double c_minute=c.get(Calendar.MINUTE);
        double c_time =((c_minute/60)+c_hour);
        Log.d("Ethan c_dow",c_dow+"");

        int i=0;
        while(i<temp.size()){
            String dow= temp.get(i).dow;
            Log.d("Ethan year",temp.get(i).startTime);
            String s_year =temp.get(i).startTime.substring(0,4);;
            String s_month=temp.get(i).startTime.substring(5,7);
            Log.d("Ethan month",s_month);
            String s_day=temp.get(i).startTime.substring(8,10);
            String s_hour=temp.get(i).startTime.substring(11,13);
            String s_minute=temp.get(i).startTime.substring(14,16);

            double sint_time=(Double.parseDouble(s_minute)/60)+Double.parseDouble(s_hour);




            Log.d("Etan day",s_day);
            Log.d("Etan time",sint_time+"");



            String e_year =temp.get(i).endTime.substring(0,4);
            String e_month=temp.get(i).endTime.substring(5,7);
            String e_day=temp.get(i).endTime.substring(8,10);
            String e_hour=temp.get(i).endTime.substring(11,13);
            String e_minute=temp.get(i).endTime.substring(14,16);
            double eint_time=(Double.parseDouble(e_minute)/60)+Double.parseDouble(e_hour);

            Log.d("Ethan eyear",e_year);
            Log.d("Ethan emonth",e_month);
            Log.d("Etan eday",e_day);
            Log.d("Etan time",eint_time+"");

            Log.d("Ethan c month",c_month+"");
            boolean test =c_hour<=Integer.parseInt(e_hour);
            Log.d("Ethan year test",test+"");
            //Log.d("Ethan",temp.get(0).dow.toString().contains("W")+"");
            //Log.d("Ethan dow string",dow.contains("w")+"");
            //Log.d("Ethan Calendar",date.getDay()+"");

            if(c_dow==1 && dow.contains("su")){
                if((c_year>=Integer.parseInt(s_year)) && c_year<=Integer.parseInt(e_year)){
                    if((c_month>=Integer.parseInt(s_month)) && c_month<=Integer.parseInt(e_month)){
                        if((c_day>=Integer.parseInt(s_day)) && c_day<=Integer.parseInt(e_day)){
                            if((c_time)>=sint_time && c_time<=eint_time)
                            {
                                start_timer(eint_time,c_time);
                            }
                        }
                    }

                }
            }
            if(c_dow==2 && dow.contains("m")){
                if((c_year>=Integer.parseInt(s_year)) && c_year<=Integer.parseInt(e_year)){
                    if((c_month>=Integer.parseInt(s_month)) && c_month<=Integer.parseInt(e_month)){
                        if((c_day>=Integer.parseInt(s_day)) && c_day<=Integer.parseInt(e_day)){
                            if((c_time)>=sint_time && c_time<=eint_time)
                            {
                                start_timer(eint_time,c_time);
                            }
                        }
                    }

                }
            }
            if(c_dow==3 && dow.contains("tu")){
                if((c_year>=Integer.parseInt(s_year)) && c_year<=Integer.parseInt(e_year)){
                    if((c_month>=Integer.parseInt(s_month)) && c_month<=Integer.parseInt(e_month)){
                        if((c_day>=Integer.parseInt(s_day)) && c_day<=Integer.parseInt(e_day)){
                            if((c_time)>=sint_time && c_time<=eint_time)
                            {
                                start_timer(eint_time,c_time);
                            }
                        }
                    }

                }
            }
            if(c_dow==4 && dow.contains("w")){
                if((c_year>=Integer.parseInt(s_year)) && c_year<=Integer.parseInt(e_year)){
                    if((c_month>=Integer.parseInt(s_month)) && c_month<=Integer.parseInt(e_month)){
                        if((c_day>=Integer.parseInt(s_day)) && c_day<=Integer.parseInt(e_day)){
                            if((c_time)>=sint_time && c_time<=eint_time)
                            {

                                Log.d("Ethan Time left ",eint_time-c_time+"");
                                start_timer(eint_time,c_time);
                            }
                        }
                    }

                }
                        }
            if(c_dow==5 && dow.contains("th")){
                if((c_year>=Integer.parseInt(s_year)) && c_year<=Integer.parseInt(e_year)){
                    if((c_month>=Integer.parseInt(s_month)) && c_month<=Integer.parseInt(e_month)){
                        if((c_day>=Integer.parseInt(s_day)) && c_day<=Integer.parseInt(e_day)){
                            if((c_time)>=sint_time && c_time<=eint_time)
                            {
                                start_timer(eint_time,c_time);
                            }
                        }
                    }

                }
            }
            if(c_dow==6 && dow.contains("f")){
                if((c_year>=Integer.parseInt(s_year)) && c_year<=Integer.parseInt(e_year)){
                    if((c_month>=Integer.parseInt(s_month)) && c_month<=Integer.parseInt(e_month)){
                        if((c_day>=Integer.parseInt(s_day)) && c_day<=Integer.parseInt(e_day)){
                            if((c_time)>=sint_time && c_time<=eint_time)
                            {
                                start_timer(eint_time,c_time);
                            }
                        }
                    }

                }
            }
            if(c_dow==7 && dow.contains("sa")){
                if((c_year>=Integer.parseInt(s_year)) && c_year<=Integer.parseInt(e_year)){
                    if((c_month>=Integer.parseInt(s_month)) && c_month<=Integer.parseInt(e_month)){
                        if((c_day>=Integer.parseInt(s_day)) && c_day<=Integer.parseInt(e_day)){
                            if((c_time)>=sint_time && c_time<=eint_time)
                            {
                                start_timer(eint_time,c_time);
                            }
                        }
                    }

                }
            }

            i++;
            Log.d("Ethan Dow",day_of_week+"");
        }
    }
    public void start_timer( double eint_time,double c_time) {

        time_remaining = (eint_time - c_time) * 3600000;
        final Handler time_handler = new Handler();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                double time_seconds;
                //Log.d("Ethan8", "Run was called");
               // System.out.println("entered runnable");
                if (time_remaining > 0) {//&& cont == true) {
                    //Log.d("Ethan cont", cont + "");
                   time_seconds = time_remaining / 1000;
                    TextView timer=(TextView)findViewById(R.id.time_left);
                    timer.setText(Math.round((time_seconds/60))+" minutes left in class");
                    time_remaining = time_remaining - 1000;
                    time_handler.postDelayed(this, 1000);

                      if (pressed == true) {

                        if(toast==1){Toast toastTouch = new Toast(context);
                        toastTouch.makeText(context, "Shame on you for not finishing your session, you have earned no poitns and the timer reset", toastTouch.LENGTH_SHORT).show();
                        Log.d("Ethan","back button pressed, or overview or home");}
                          toast=0;

                        cont = false;

                        }


                }
                else if (time_remaining == 0 && cont==true) {
                    time_seconds = time_remaining / 1000;
                    TextView timer=(TextView)findViewById(R.id.time_left);
                    timer.setText(Math.round((time_seconds/60))+" minutes left in class");
                    ClassSessionComplete();
                    cont=false;

                }

            }

        };
        time_handler.post(runnable);
    }

    public void ClassSessionComplete() {
        int add_points = 500;
        SharedPreferences settings = getSharedPreferences("MyPrefsFile", 0);
        DBHandler db = new DBHandler(context);

        ArrayList<Class_Student> temp = db.getStudent(settings.getString("my_username", "default"));

        Log.d("Ethan SP", temp.get(0).SpendablePoints + "");
        Log.d("Ethan TP", temp.get(0).TotalPoints + "");

        int SpendablePoints = temp.get(0).SpendablePoints + add_points;
        int TotalPoints = temp.get(0).TotalPoints + add_points;

        Log.d("Ethan SPnew", SpendablePoints + add_points + "");
        Log.d("Ethan TPNew", TotalPoints + add_points + "");

        db.updateStudent(temp.get(0).UserID, temp.get(0).Email, temp.get(0).FullName, temp.get(0).Password, temp.get(0).University, temp.get(0).Major,
                temp.get(0).Group1, temp.get(0).Group2, temp.get(0).Group3, SpendablePoints, TotalPoints);

    }
   //On click of text view on main page update total points
    public void refresh(View v){
        updatePoints();
        checkClass();
    }
    //update text view with total points
    public void updatePoints(){
        TextView points=(TextView) findViewById(R.id.totalPoints);
        DBHandler db=new DBHandler(context);
        ArrayList<Class_Student> temp=db.getStudent(settings.getString("my_username","default"));
        int TotalPoints=temp.get(0).TotalPoints;
        points.setText(""+TotalPoints);
    }

    //methods to go to new activities
    public void goto_StudySession(View view){
        Intent intent = new Intent(this, StudySession.class);
        startActivity(intent);
    }

    public void goto_Leaderboard(View view){
        Intent intent = new Intent(this,Leaderboard.class);
        startActivity(intent);
    }
    public void goto_Rewards(View view){
        Intent intent = new Intent(this,Rewards.class);
        startActivity(intent);
    }
    public void goto_Settings(View view){
        Intent intent = new Intent(this,Settings.class);
        startActivity(intent);
    }
    //Put hardcoded data into the db.
    public void db_insert(){
        DBHandler db=new DBHandler(context);

        //add students to db
        db.addStudent("efortin", "efortin@villanova.edu", "Ethan Fortin", "password", "Villanova", "CPE", "FocusFriend", "MADClass", "FrisbeeTeam", 100, 200);
        db.addStudent("mlopez15", "mlopez15@villanova.edu", "Madeline Lopez", "password", "Villanova", "EE", "FocusFriend", "MADClass", "SAE I'ts a Frat", 0, 1000);
       //MITCH IS NOT A FINANCE MAJOR
        db.addStudent("mcustode", "mcustode@villanova.edu", "Michelle Custode", "password", "Villanova", "MIS","FocusFriend", "MADClass", "SAE I'ts a Frat", 1000, 2000);
        db.addStudent("jdardis", "jdardis@villanova.edu", "Jack Dardis", "password", "Villanova", "CSC", "FocusFriend", "MADClass", "SAE I'ts a Frat", 175, 175);
        db.addStudent("mdjay", "mdjay@villanova.edu", "Maria Djay", "password", "Villanova", "MTH", "FocusFriend", "MADClass", "SAE I'ts a Frat", 600, 2400);

        //add rewards to db
        db.addReward("campco1","BOGO %50 off Campco Zilli Fries ",4000,"food","zilliwilli");
        db.addReward("chipotle1","%10 off Chipotle Burrito",1000,"food","chipotle1");
        db.addReward("chipotle2","free guac with a Chiptole Burrito",1500,"food","chipotle2");
        db.addReward("forever21","%15 off purchase of $30 or more",2000,"clothes","15off21");
        db.addReward("alexandannie","free shipping on next order",4000,"clothes","alexshipsfree");
        db.addReward("nails","BOGO free things girls do with nails",6000,"care","freenails");

        //add classes
        //db.addClass("efortin","00159","2017-05-03T14:00","2017-05-30T14:30","mwf");

    }
    @Override
    public void onBackPressed() {

        pressed = true;
        // }
        Intent intent = new Intent(context, MainMenu.class);
        intent.putExtra("studysession",true);
        startActivity(intent);
    }
    @Override
    public void onPause(){
        super.onPause();

        //if(toastTouch != null){
        pressed = true;
        // }

    }
   /* @Override
    public void onResume(){
        timer.setText("" + time_seconds);
    }*/

    @Override
    public void onStop(){
        super.onStop();
        finish();
    }

}

