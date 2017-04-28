package focusfriend.focusfriend;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class Leaderboard extends AppCompatActivity {

    //typical intializes
    final String PREFS_NAME = "MyPrefsFile";
    SharedPreferences settings;
    Context context = this;
    Button updateGroups, dialog_updateButton;
    EditText et_university, et_major, et_group1, et_group2, et_group3;
    Dialog dialog_update_groups;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        //get shared preference username so we know who user is
        settings = getSharedPreferences(PREFS_NAME, 0);
        username = settings.getString("my_username", "default");

        //dialog allows user to update their groups university and major for comparisons on the leaderboard only launched on click
        dialog_update_groups = new Dialog(context);
        dialog_update_groups.setContentView(R.layout.dialog_updategroups);
        updateGroups = (Button) findViewById(R.id.update_groups);
        et_university = (EditText) dialog_update_groups.findViewById(R.id.edit_university);
        et_major = (EditText) dialog_update_groups.findViewById(R.id.edit_major);
        et_group1 = (EditText) dialog_update_groups.findViewById(R.id.edit_group1);
        et_group2 = (EditText) dialog_update_groups.findViewById(R.id.edit_group2);
        et_group3 = (EditText) dialog_update_groups.findViewById(R.id.edit_group3);
        dialog_updateButton=(Button)dialog_update_groups.findViewById(R.id.dialog_update_groups);

        //launches dialog for update groups
        updateGroups.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog_update_groups.show();

            }
        });

        //when clicked sends updated info to db
        dialog_updateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String university, major, group1, group2, group3;
                university = et_university.getText().toString();
                major = et_major.getText().toString();
                group1 = et_group1.getText().toString();
                group2 = et_group2.getText().toString();
                group3 = et_group3.getText().toString();

                DBHandler db=new DBHandler(context);
                db.updateGroups(username,university,major,group1,group2,group3);
                ArrayList<Class_Student> AL= db.getStudent(username);
                String u=AL.get(0).University;
                String m= AL.get(0).Major; String g1=AL.get(0).Group1; String g2=AL.get(0).Group2; String g3=AL.get(0).Group3;
                Log.d("Ethan Username",username);
                Log.d("Ethan u",u);
                Log.d("Ethan m",m);
                Log.d("Ethan g1",g1);
                Log.d("Ethan g2",g2);
                Log.d("Ethan g3",g3);
                dialog_update_groups.cancel();
            }
        });
    }

    //these on click methods all work the same they just show the different ways to sort the list.
    //these buttons are all at the top of the screen, when click they send getallstudents from db with params for column name and value that matches the users
    //SQL query is put into array list and returned where we put it into an array adapter to get it into the list view
    public void show_university(View view){
        DBHandler db=new DBHandler(context);
        ArrayList<Class_Student> student=db.getStudent(username);
        String uni= student.get(0).University;
        student=db.getAllStudentfromGroup("university",uni);
        ArrayAdapter<Class_Student> adapter = new ArrayAdapter_Leaderboard(context, student);
        ListView listView = (ListView) findViewById(R.id.leaderboard);
        listView.setAdapter(adapter);
    }
    public void show_major(View view){
        DBHandler db=new DBHandler(context);
        ArrayList<Class_Student> student=db.getStudent(username);
        String maj= student.get(0).Major;
        student=db.getAllStudentfromGroup("major",maj);
        ArrayAdapter<Class_Student> adapter = new ArrayAdapter_Leaderboard(context, student);
        ListView listView = (ListView) findViewById(R.id.leaderboard);
        listView.setAdapter(adapter);
    }
    public void show_group1(View view){
        DBHandler db=new DBHandler(context);
        ArrayList<Class_Student> student=db.getStudent(username);
        String g1= student.get(0).Group1;
        student=db.getAllStudentfromGroup("group1",g1);
        ArrayAdapter<Class_Student> adapter = new ArrayAdapter_Leaderboard(context, student);
        ListView listView = (ListView) findViewById(R.id.leaderboard);
        listView.setAdapter(adapter);
    }
    public void show_group2(View view){
        DBHandler db=new DBHandler(context);
        ArrayList<Class_Student> student=db.getStudent(username);
        String g2= student.get(0).Group2;
        student=db.getAllStudentfromGroup("group2",g2);
        ArrayAdapter<Class_Student> adapter = new ArrayAdapter_Leaderboard(context, student);
        ListView listView = (ListView) findViewById(R.id.leaderboard);
        listView.setAdapter(adapter);
    }
    public void show_group3(View view){
        DBHandler db=new DBHandler(context);
        ArrayList<Class_Student> student=db.getStudent(username);
        String g3= student.get(0).Group3;
        student=db.getAllStudentfromGroup("group3",g3);
        ArrayAdapter<Class_Student> adapter = new ArrayAdapter_Leaderboard(context, student);
        ListView listView = (ListView) findViewById(R.id.leaderboard);
        listView.setAdapter(adapter);
    }
}
