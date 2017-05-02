package focusfriend.focusfriend;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {
    //basic intializations
    final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences settings;
    Context context=this;
    Button existinguser, newuser, login;
    EditText et_name, et_email, et_username, et_password, exuser_username, exuser_password;
    Dialog dialog_newuser, dialog_existinguser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


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
                Toast.makeText(context, "Auto Login Succes", Toast.LENGTH_LONG).show();
                updatePoints();
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
   //On click of text view on main page update total points
    public void refresh(View v){
        updatePoints();
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
        db.addStudent("mcustode", "mcustode@villanova.edu", "Michelle Custode", "password", "Villanova", "Finance","FocusFriend", "MADClass", "SAE I'ts a Frat", 1000, 2000);
        db.addStudent("jdardis", "jdardis@villanova.edu", "Jack Dardis", "password", "Villanova", "CSC", "FocusFriend", "MADClass", "SAE I'ts a Frat", 175, 175);
        db.addStudent("mdjay", "mdjay@villanova.edu", "Maria Djay", "password", "Villanova", "MTH", "FocusFriend", "MADClass", "SAE I'ts a Frat", 600, 2400);

        //add rewards to db
        db.addReward("campco1","BOGO %50 off Campco Zilli Fries ",4000,"food","zilliwilli");
        db.addReward("chipotle1","%10 off Chipotle Burrito",1000,"food","chipotle1");
        db.addReward("chipotle2","free guac with a Chiptole Burrito",1500,"food","chipotle2");
        db.addReward("forever21","%15 off purchase of $30 or more",2000,"clothes","15off21");
        db.addReward("alexandannie","free shipping on next order",4000,"clothes","alexshipsfree");
        db.addReward("nails","BOGO free things girls do with nails",6000,"care","freenails");

    }
}
