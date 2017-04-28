package focusfriend.focusfriend;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wildcat on 11/3/2016.
 */

public class DBHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "FocusFriend";

    // Users table name
    private static final String TABLE_USERS = "users";

    // Users Table Columns names
    private static final String KEY_USERID = "userid";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_FULL_NAME = "full_name";
    private static final String KEY_PASSWORD="password";
    private static final String KEY_UNIVERSITY="university";
    private static final String KEY_MAJOR="major";
    private static final String KEY_GROUP1="group1";
    private static final String KEY_GROUP2="group2";
    private static final String KEY_GROUP3="group3";
    private static final String KEY_SPENDABLE_POINTS="sp";
    private static final String KEY_TOTAL_POINTS="tp";

    //Table name
    private static final String TABLE_REWARDS="rewards";

    //Users Table Columns names
    private static final String KEY_REWARDID = "rewardid";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_COST = "cost";
    private static final String KEY_TYPE="type";
    private static final String KEY_REDEMPTION_CODE="code";

    //Pictures will in corperated later in development
    // private static final String KEY_PICTURE="picture";
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //onCreate Function Builds the database locally on the phone
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Write the string in SQL syntax to create the users tables, outlines columns and data types
        String Create_Students_Table = "CREATE TABLE " + TABLE_USERS +
                "(" + KEY_USERID + " TEXT, " + KEY_EMAIL+ " TEXT, " + KEY_FULL_NAME + " TEXT, " +KEY_PASSWORD + " TEXT, "
                + KEY_UNIVERSITY+ " TEXT, " + KEY_MAJOR + " TEXT, " + KEY_GROUP1 + " TEXT, " +KEY_GROUP2 + " TEXT, "
                + KEY_GROUP3 + " TEXT, " + KEY_SPENDABLE_POINTS + " BIGINT, " + KEY_TOTAL_POINTS + " BIGINT "
                + ")";
        //execute sql statement to built users table
        db.execSQL(Create_Students_Table);
        //Write the string in SQL syntax to create the rewards table, outlines columns and datatypes
        String Create_Rewards_Table="CREATE TABLE " + TABLE_REWARDS +
                "(" + KEY_REWARDID + " TEXT, " + KEY_DESCRIPTION+ " BLOB, " + KEY_COST + " INT, " +KEY_TYPE+" TEXT,"+KEY_REDEMPTION_CODE + " TEXT " + ")";
        db.execSQL(Create_Rewards_Table);
    }

    //used to upgrade table on local device if we need to remake the table
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS" +TABLE_REWARDS);
        onCreate(db);
    }

    //this is used to add a student to the database
    public void addStudent(String UserID, String Email, String FullName, String Password, String University, String Major, String Group1, String Group2, String Group3, int SP, int TP) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //Fill contentValues with column name and matching data
        values.put(KEY_USERID, UserID.toString());
        values.put(KEY_EMAIL, Email.toString());
        values.put(KEY_FULL_NAME, FullName.toString());
        values.put(KEY_PASSWORD, Password.toString());
        values.put(KEY_UNIVERSITY, University.toString());
        values.put(KEY_MAJOR, Major.toString());
        values.put(KEY_GROUP1, Group1.toString());
        values.put(KEY_GROUP2, Group2.toString());
        values.put(KEY_GROUP3, Group3.toString());
        values.put(KEY_SPENDABLE_POINTS, SP);
        values.put(KEY_TOTAL_POINTS, TP);

        //insert values into db and close db
        db.insert(TABLE_USERS, null, values);
        db.close();
    }

    //This getstudent returns an array of students who match the userid provided
    public ArrayList<Class_Student> getStudent(String UserID) {

        //intialize array list which we will put sql query resutls in
        ArrayList<Class_Student> studentList = new ArrayList<Class_Student>();

        //String SQL query
        String selectQuery = "SELECT *  FROM " + TABLE_USERS + " WHERE userid like '" +UserID+"'";

        //set the cursor= to results of sql query
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //loops through cursor putting adding values of cursor to student array
        if (cursor.moveToFirst()) {
            do {
                int totalPoints, spendablePoints;
                totalPoints=Integer.parseInt(cursor.getString(10));
                spendablePoints=Integer.parseInt(cursor.getString(9));
                Class_Student Student= new Class_Student(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getString(6), cursor.getString(7),cursor.getString(8),
                        spendablePoints, totalPoints);
                studentList.add(Student);
            }
            while (cursor.moveToNext());
        }
        return studentList;

    }

    //similar to getstudent but gets all students
    public ArrayList<Class_Student> getAllStudent() {
        ArrayList<Class_Student> studentList = new ArrayList<Class_Student>();

        String selectQuery = "SELECT * FROM " + TABLE_USERS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int totalPoints, spendablePoints;
                totalPoints=Integer.parseInt(cursor.getString(10));
                spendablePoints=Integer.parseInt(cursor.getString(9));
                        Class_Student Student= new Class_Student(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getString(6), cursor.getString(7),cursor.getString(8),
                        spendablePoints, totalPoints);
                studentList.add(Student);
            }
            while (cursor.moveToNext());
        }
        return studentList;
    }

    //Similar to get all students but only returns students who match group/university/major
    public ArrayList<Class_Student> getAllStudentfromGroup(String sort, String value) {
        ArrayList<Class_Student> studentList = new ArrayList<Class_Student>();

        String selectQuery = "SELECT * FROM " + TABLE_USERS + " WHERE "+ sort+ " like '"+value+"' ORDER BY tp DESC;";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int totalPoints, spendablePoints;
                totalPoints=Integer.parseInt(cursor.getString(10));
                spendablePoints=Integer.parseInt(cursor.getString(9));
                Class_Student Student= new Class_Student(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                        cursor.getString(5), cursor.getString(6), cursor.getString(7),cursor.getString(8),
                        spendablePoints, totalPoints);
                studentList.add(Student);
            }
            while (cursor.moveToNext());
        }
        return studentList;
    }

    //used to update info on student
    public int updateStudent(String UserID, String Email, String FullName, String Password, String University, String Major, String Group1, String Group2, String Group3, int SP, int TP) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USERID, UserID.toString());
        values.put(KEY_EMAIL, Email.toString());
        values.put(KEY_FULL_NAME, FullName.toString());
        values.put(KEY_PASSWORD, Password.toString());
        values.put(KEY_UNIVERSITY, University.toString());
        values.put(KEY_MAJOR, Major.toString());
        values.put(KEY_GROUP1, Group1.toString());
        values.put(KEY_GROUP2, Group2.toString());
        values.put(KEY_GROUP3, Group3.toString());
        values.put(KEY_SPENDABLE_POINTS, SP);
        values.put(KEY_TOTAL_POINTS, TP);

        return db.update(TABLE_USERS, values, KEY_USERID + " =?",
                new String[]{UserID.toString()});

    }

    //used to update a students groups
    public void updateGroups(String userID, String university, String major, String group1, String group2, String group3){
        String selectQuery = "Update " +TABLE_USERS+
                            " Set "+ KEY_UNIVERSITY + " = '" + university+ "', "+ KEY_MAJOR+ " = '"+major+ "', "+ KEY_GROUP1+ "= '"+group1+"', "
                            +KEY_GROUP2+ " = '"+group2+"', "+ KEY_GROUP3+ " = '"+ group3+"' "+
                            "Where "+KEY_USERID+ " = '"+userID+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(selectQuery);
    }

    //used to delete contact
    public void deleteContact(String UserID){
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete(TABLE_USERS,KEY_USERID+ " = ?",
                new String[]{UserID.toString()});
        db.close();
    }
    //returns password of userid , password is then matched to what the user enters, if same the screen lets the user continue
    public ArrayList<String> login(String UserID){
        ArrayList<String> passwords = new ArrayList<String>();
        String selectQuery = "SELECT password FROM " + TABLE_USERS + " WHERE userid like '" +UserID+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String password= cursor.getString(0);
                passwords.add(password);
            }
            while (cursor.moveToNext());
        }
        return passwords;

    }
    //this is used to add a reward to the database
    public void addReward(String RewardID, String Description, int Cost, String Type, String Code) {
        String addQuery="INSERT into "+ TABLE_REWARDS + " ( "+KEY_REWARDID+ ", "+ KEY_DESCRIPTION+", "+ KEY_COST+ ", "+KEY_TYPE+", "+KEY_REDEMPTION_CODE+") "
                +" VALUES( '"+RewardID+"',  '"+Description+"', '"+Cost+"', '"+Type+"', '"+Code+"');";

        /*
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //Fill contentValues with column name and matching data
        values.put(KEY_USERID, RewardID.toString());
        values.put(KEY_DESCRIPTION, Description.toString());
        values.put(KEY_COST, Cost);
        values.put(KEY_TYPE,Type.toString());
        values.put(KEY_REDEMPTION_CODE, Code.toString());

        //insert values into db and close db
        db.insert(TABLE_REWARDS, null, values);
        db.close();*/
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(addQuery);
    }
    public ArrayList<Class_Rewards> getAllRewards(String sort, String value) {
        ArrayList<Class_Rewards> RewardList = new ArrayList<Class_Rewards>();

        String selectQuery = "SELECT * FROM " + TABLE_REWARDS + " WHERE "+ sort+ " like '"+value+"' ORDER BY cost DESC;";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int cost;

                cost=Integer.parseInt(cursor.getString(2));
                Class_Rewards reward= new Class_Rewards(cursor.getString(0), cursor.getString(1), cost, cursor.getString(3), cursor.getString(4));
                RewardList.add(reward);
            }
            while (cursor.moveToNext());
        }
        return RewardList;
    }

}