package focusfriend.focusfriend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import focusfriend.focusfriend.Class_Student;


public class ArrayAdapter_Leaderboard extends ArrayAdapter<Class_Student> {

    public ArrayAdapter_Leaderboard(Context context, ArrayList<Class_Student> students) {
        super(context,0,students);
    }

    @Override
    //typical array adapter but puts the values full name totalPoints university major into the row xml created.
    public View getView(int position,View convertView, ViewGroup parent) {

        Class_Student single_student = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_leaderboard, parent, false);
        }

        TextView fullname = (TextView)convertView.findViewById(R.id.leaderboard_fullname);
        TextView totalPoints = (TextView)convertView.findViewById(R.id.leaderboard_totalpoints);
        TextView university = (TextView)convertView.findViewById(R.id.leaderboard_university);
        TextView major = (TextView)convertView.findViewById(R.id.leaderboard_major);

        fullname.setText(single_student.FullName);
        totalPoints.setText(single_student.TotalPoints+"");
        university.setText(single_student.University);
        major.setText(single_student.Major);

        return convertView;

    }

}