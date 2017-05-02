package focusfriend.focusfriend;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Rewards extends AppCompatActivity {
    final String PREFS_NAME = "MyPrefsFile";
    Context context=this;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards);

      settings = getSharedPreferences(PREFS_NAME, 0);
    }

    public void show_food(View view){
        final DBHandler db=new DBHandler(this);

        final ArrayList<Class_Rewards> rewards=db.getAllRewards("type","food");
        ArrayAdapter<Class_Rewards> adapter = new focusfriend.focusfriend.ArrayAdapter_Rewards(this, rewards);

        Log.d("Ethan rewards",rewards.get(0).Description);

        final ListView listView = (ListView) findViewById(R.id.rewards);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                final Class_Rewards selectedListItem = (Class_Rewards) listView.getItemAtPosition(position);

                new AlertDialog.Builder(context)
                        .setTitle(selectedListItem.Description)
                        .setMessage("Would you like to purchase for "+selectedListItem.Cost+ " points")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                ArrayList<Class_Student> temp=db.getStudent(settings.getString("my_username","default"));

                               if(temp.get(0).SpendablePoints-selectedListItem.Cost>=0) {
                                   int SpendablePoints = temp.get(0).SpendablePoints - selectedListItem.Cost;
                                   int TotalPoints = temp.get(0).TotalPoints - selectedListItem.Cost;

                                   db.updateStudent(temp.get(0).UserID, temp.get(0).Email, temp.get(0).FullName, temp.get(0).Password, temp.get(0).University, temp.get(0).Major,
                                           temp.get(0).Group1, temp.get(0).Group2, temp.get(0).Group3, SpendablePoints, TotalPoints);
                                  new AlertDialog.Builder(context)
                                          .setTitle("Sucessfuly Purchased"+selectedListItem.Description)
                                          .setMessage("Use this code in store: \n"+selectedListItem.Code)
                                          .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                              public void onClick(DialogInterface dialog, int which) {
                                                  Toast.makeText(Rewards.this, "Purchased!!!!!", Toast.LENGTH_SHORT).show();
                                                  dialog.cancel();

                                                 }

                                            }).show();
                                    }
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

        });
    }
    public void show_clothes(View view){
        final DBHandler db=new DBHandler(this);
        ArrayList<Class_Rewards> rewards=db.getAllRewards("type","clothes");
        ArrayAdapter<Class_Rewards> adapter = new focusfriend.focusfriend.ArrayAdapter_Rewards(this, rewards);
        final ListView listView = (ListView) findViewById(R.id.rewards);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                final Class_Rewards selectedListItem = (Class_Rewards) listView.getItemAtPosition(position);

                new AlertDialog.Builder(context)
                        .setTitle(selectedListItem.Description)
                        .setMessage("Would you like to purchase for "+selectedListItem.Cost+ " points")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                ArrayList<Class_Student> temp=db.getStudent(settings.getString("my_username","default"));

                                if(temp.get(0).SpendablePoints-selectedListItem.Cost>=0) {
                                    int SpendablePoints = temp.get(0).SpendablePoints - selectedListItem.Cost;
                                    int TotalPoints = temp.get(0).TotalPoints - selectedListItem.Cost;

                                    db.updateStudent(temp.get(0).UserID, temp.get(0).Email, temp.get(0).FullName, temp.get(0).Password, temp.get(0).University, temp.get(0).Major,
                                            temp.get(0).Group1, temp.get(0).Group2, temp.get(0).Group3, SpendablePoints, TotalPoints);
                                    new AlertDialog.Builder(context)
                                            .setTitle("Sucessfuly Purchased"+selectedListItem.Description)
                                            .setMessage("Use this code in store: \n"+selectedListItem.Code)
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Toast.makeText(Rewards.this, "Purchased!!!!!", Toast.LENGTH_SHORT).show();
                                                    dialog.cancel();

                                                }

                                            }).show();
                                }
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

        });
    }
    public void show_care(View view){
        final DBHandler db=new DBHandler(this);
        ArrayList<Class_Rewards> rewards=db.getAllRewards("type","care");
        ArrayAdapter<Class_Rewards> adapter = new focusfriend.focusfriend.ArrayAdapter_Rewards(this, rewards);
       final ListView listView = (ListView) findViewById(R.id.rewards);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                final Class_Rewards selectedListItem = (Class_Rewards) listView.getItemAtPosition(position);

                new AlertDialog.Builder(context)
                        .setTitle(selectedListItem.Description)
                        .setMessage("Would you like to purchase for "+selectedListItem.Cost+ " points")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                ArrayList<Class_Student> temp=db.getStudent(settings.getString("my_username","default"));

                                if(temp.get(0).SpendablePoints-selectedListItem.Cost>=0) {
                                    final int SpendablePoints = temp.get(0).SpendablePoints - selectedListItem.Cost;
                                    int TotalPoints = temp.get(0).TotalPoints;

                                    db.updateStudent(temp.get(0).UserID, temp.get(0).Email, temp.get(0).FullName, temp.get(0).Password, temp.get(0).University, temp.get(0).Major,
                                            temp.get(0).Group1, temp.get(0).Group2, temp.get(0).Group3, SpendablePoints, TotalPoints);
                                    new AlertDialog.Builder(context)
                                            .setTitle("Sucessfuly Purchased"+selectedListItem.Description)
                                            .setMessage("Use this code in store: \n"+selectedListItem.Code)
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Toast.makeText(Rewards.this, "Purchased, you have "+SpendablePoints+"left", Toast.LENGTH_SHORT).show();
                                                    dialog.cancel();

                                                }

                                            }).show();
                                }
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

        });
    }
}
