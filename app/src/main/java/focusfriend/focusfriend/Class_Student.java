package focusfriend.focusfriend;

/**
 * Created by Wildcat on 4/26/2017.
 */

//student class holds all data from student db
public class Class_Student {
        public String UserID;
        public String Email;
        public String FullName;
        public String Password;
        public String University;
        public String Major;
        public String Group1;
        public String Group2;
        public String Group3;
        public int SpendablePoints;
        public int TotalPoints;


        public Class_Student(String UserID, String Email, String FullName, String Password, String University, String Major, String Group1,
                             String Group2, String Group3, int SpendablePoints, int TotalPoints) {
            super();
            this.UserID=UserID;
            this.Email=Email;
            this.FullName=FullName;
            this.Password=Password;
            this.University=University;
            this.Major=Major;
            this.Group1=Group1;
            this.Group2=Group2;
            this.Group3=Group3;
            this.SpendablePoints=SpendablePoints;
            this.TotalPoints=TotalPoints;

        }
        //these methdos allow us to return one piece of data if we need it
        public String getUserID(){return UserID;}
        public String getEmail(){return Email;}
        public String getFullName(){return FullName;}
        public String getPassword(){return Password;}
        public String getUniversity(){return University;}
        public String getMajor(){return Major;}
        public String getGroup1(){return Group1;}
        public String getGroup2(){return Group2;}
        public String getGroup3(){return Group3;}
        public int getSpendablePoints(){return SpendablePoints;}
        public int getTotalPoints(){return TotalPoints;}

    }
