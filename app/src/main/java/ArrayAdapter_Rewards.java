package focusfriend.focusfriend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import focusfriend.focusfriend.Class_Student;


public class ArrayAdapter_Rewards extends ArrayAdapter<Class_Rewards> {

    public ArrayAdapter_Rewards(Context context, ArrayList<Class_Rewards> rewards) {
        super(context,0,rewards);
    }

    @Override
    //typical array adapter but puts the values full name totalPoints university major into the row xml created.
    public View getView(int position,View convertView, ViewGroup parent) {

        Class_Rewards single_reward = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_rewards, parent, false);
        }

        TextView Description = (TextView)convertView.findViewById(R.id.rewards_description);
        TextView Cost = (TextView)convertView.findViewById(R.id.rewards_cost);
        //Button purchase= (Button) convertView.findViewById(R.id.rewards_purchase);


        Description.setText(single_reward.Description);
        Cost.setText("Cost:"+ single_reward.Cost+" points");

        return convertView;

    }

}