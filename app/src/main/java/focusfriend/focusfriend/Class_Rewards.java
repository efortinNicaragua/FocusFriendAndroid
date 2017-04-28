package focusfriend.focusfriend;

/**
 * Created by Wildcat on 4/26/2017.
 */

//student class holds all data from student db
public class Class_Rewards {
    public String RewardID;
    public String Description;
    public int Cost;
    public String Type;
    public String Code;



    public Class_Rewards(String RewardID, String Description, int Cost, String Type, String Code){
        super();
        this.RewardID=RewardID;
        this.Description=Description;
        this.Cost=Cost;
        this.Type=Type;
        this.Code=Code;

    }
}
