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
import android.widget.Toast;

import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {
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
        settings = getSharedPreferences(PREFS_NAME, 0);
        if (check_credentials()==false) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");

            // first time task
                    dialog_newuser= new Dialog(this);
                    dialog_newuser.setTitle("Enter Personal Info");
                    dialog_newuser.setContentView(R.layout.dialog_newuser);
                    dialog_newuser.show();

            existinguser=(Button) dialog_newuser.findViewById(R.id.existing_user);
            newuser=(Button) dialog_newuser.findViewById(R.id.create_newuser);
            et_name=(EditText) dialog_newuser.findViewById(R.id.edit_fullName);
            et_email=(EditText) dialog_newuser.findViewById(R.id.edit_email);
            et_username=(EditText) dialog_newuser.findViewById(R.id.edit_username);
            et_password=(EditText) dialog_newuser.findViewById(R.id.edit_password);

            newuser.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    String name, email, username, password;
                    name=et_name.getText().toString();
                    email=et_email.getText().toString();
                    username=et_username.getText().toString();
                    password=et_password.getText().toString();


                    DBHandler db=new DBHandler(context);
                    db.addStudent(username, email, name, password, "Villanova", "", "", "", "", 0, 0);
                    db.addStudent("efortin", "efortin@villanova.edu", "Ethan Fortin", "password", "Villanova", "CPE", "FocusFriend", "Engineer", "FrisbeeTeam", 100, 200);
                    db.addStudent("mlopez15", "mlopez15@villanova.edu", "Madeline Lopez", "password", "Villanova", "EE", "FocusFriend", "Engineer", "SAE I'ts a Frat", 0, 1000);
                    dialog_newuser.cancel();
                    Toast.makeText(context, "New User Created!", Toast.LENGTH_SHORT).show();
                    // record the fact that the app has been started at least once
                    settings.edit().putString("my_username", username).commit();
                    settings.edit().putString("my_password",password).commit();
                }
            });
           existinguser.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    dialog_newuser.cancel();
                    Log.d("Insert: ", "Inserting...");

                    DBHandler db=new DBHandler(context);
                    db.addStudent("efortin", "efortin@villanova.edu", "Ethan Fortin", "password", "Villanova", "CPE", "FocusFriend", "Engineer", "FrisbeeTeam", 100, 200);
                    db.addStudent("mlopez15", "mlopez15@villanova.edu", "Madeline Lopez", "password", "Villanova", "EE", "FocusFriend", "Engineer", "SAE I'ts a Frat", 0, 1000);

                    dialog_existinguser= new Dialog(context);
                    dialog_existinguser.setTitle("Enter Personal Info");
                    dialog_existinguser.setContentView(R.layout.dialog_existinguser);
                    dialog_existinguser.show();

                    login=(Button) dialog_existinguser.findViewById(R.id.login);
                    exuser_username=(EditText) dialog_existinguser.findViewById(R.id.edit1_username);
                    exuser_password=(EditText)dialog_existinguser.findViewById(R.id.edit1_password);

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

                            if(passwords.size()>0) {

                                if (passwords.get(0).toString().equals(User_Password)) {
                                    settings.edit().putString("my_username", Username).commit();
                                    settings.edit().putString("my_password", User_Password).commit();
                                    String tempu=settings.getString("my_username","default");
                                    String tempp=settings.getString("my_password","default");
                                    Log.d("Ethan Passwords:", "Existing user found");
                                    Log.d("Ethan SP:", tempu+ " " + tempp);

                                    dialog_existinguser.cancel();
                                    Toast.makeText(context, "Login Succes", Toast.LENGTH_LONG).show();
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
    public void goto_StudySession(View view){
        Intent intent = new Intent(this, StudySession.class);
        startActivity(intent);
    }

}
