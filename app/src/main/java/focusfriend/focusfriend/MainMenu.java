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

public class MainMenu extends AppCompatActivity {
    final String PREFS_NAME = "MyPrefsFile";
    Context context=this;
    Button accept;
    EditText et_name, et_email, et_username, et_password;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
            //the app is being launched for first time, do something
            Log.d("Comments", "First time");

            // first time task
                    dialog= new Dialog(this);
                    dialog.setTitle("Enter Personal Info");
                    dialog.setContentView(R.layout.dialog_newuser);
                    dialog.show();

            accept=(Button) dialog.findViewById(R.id.create_newuser);
            et_name=(EditText) dialog.findViewById(R.id.edit_fullName);
            et_email=(EditText) dialog.findViewById(R.id.edit_email);
            et_username=(EditText) dialog.findViewById(R.id.edit_username);
            et_password=(EditText) dialog.findViewById(R.id.edit_password);

            accept.setOnClickListener(new View.OnClickListener(){
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
                    dialog.cancel();
                    Toast.makeText(context, "New User Created!", Toast.LENGTH_SHORT).show();
                }
            });

            // record the fact that the app has been started at least once
            settings.edit().putBoolean("my_first_time", false).commit();
        }
    }
    public void goto_StudySession(View view){
        Intent intent = new Intent(this, StudySession.class);
        startActivity(intent);
    }
   /* public void create_newuser(View view){
        String name, email, username, password;
        name=et_username.getText().toString();
        email=et_email.getText().toString();
        username=et_username.getText().toString();
        password=et_password.getText().toString();

        DBHandler db=new DBHandler(this);
        db.addStudent(username, email, name, password, "Villanova", "", "", "", "", 0, 0);
        db.addStudent("efortin", "efortin@villanova.edu", "Ethan Fortin", "password", "Villanova", "CPE", "FocusFriend", "Engineer", "FrisbeeTeam", 100, 200);
        db.addStudent("mlopez15", "mlopez15@villanova.edu", "Madeline Lopez", "password", "Villanova", "EE", "FocusFriend", "Engineer", "SAE I'ts a Frat", 0, 1000);
    }*/


}
