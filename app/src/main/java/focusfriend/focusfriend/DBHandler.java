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

    // Contacts table name
    private static final String TABLE_USERS = "users";

    // Contacts Table Columns names
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


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Create_Students_Table = "CREATE TABLE " + TABLE_USERS +
                "(" + KEY_USERID + " TEXT, " + KEY_EMAIL+ " TEXT, " + KEY_FULL_NAME + " TEXT, " +KEY_PASSWORD + " TEXT, "
                + KEY_UNIVERSITY+ " TEXT, " + KEY_MAJOR + " TEXT, " + KEY_GROUP1 + " TEXT, " +KEY_GROUP2 + " TEXT, "
                + KEY_GROUP3 + " TEXT, " + KEY_SPENDABLE_POINTS + " BIGINT, " + KEY_TOTAL_POINTS + " BIGINT "
                + ")";
        db.execSQL(Create_Students_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void addStudent(String UserID, String Email, String FullName, String Password, String University, String Major, String Group1, String Group2, String Group3, int SP, int TP) {
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

        db.insert(TABLE_USERS, null, values);
        db.close();
    }

   /* public String getStudent(String Name, String Major, int Year) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USERS, new String[]{KEY_NAME, KEY_MAJOR,
                KEY_GRADUATION_YEAR}, KEY_NAME + "=?", new String[]{Name.toString()}, null, null, null, null);
        if (cursor != null) ;
        cursor.moveToFirst();

        String Student = (cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2));

        return Student;
    }*/

    public List<String> getAllStudent() {
        List<String> studentList = new ArrayList<String>();

        String selectQuery = "SELECT userid, tp, sp FROM " + TABLE_USERS; //+ " WHERE "+ KEY_USERID +" =efortin";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String Student = (cursor.getString(0) + " " + cursor.getString(1) + " " + cursor.getString(2));
                studentList.add(Student);
            }
            while (cursor.moveToNext());
        }
        return studentList;
    }

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
    public void deleteContact(String UserID){
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete(TABLE_USERS,KEY_USERID+ " = ?",
                new String[]{UserID.toString()});
        db.close();
    }
}