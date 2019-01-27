package io.kong.mypetdiary.item;

public class UserItem {

    static String stUserID;
    static String stUserPW;
    static String stUserName;
    static String stUserArea;
    static String stUserBirth;
    static String stUserProfile;


    public void setStUserID(String stUserID) {
        UserItem.stUserID = stUserID;
    }


    public void setStUserPW(String stUserPW) {
        UserItem.stUserPW = stUserPW;
    }


    public void setStUserName(String stUserName) {
        UserItem.stUserName = stUserName;
    }



    public void setStUserArea(String stUserArea) {
        UserItem.stUserArea = stUserArea;
    }


    public void setStUserBirth(String stUserBirth) {
        UserItem.stUserBirth = stUserBirth;
    }


    public void setStUserProfile(String stUserProfile) {
        UserItem.stUserProfile = stUserProfile;
    }

    public static String getStUserID() {
        return stUserID;
    }
    public static String getStUserPW() {
        return stUserPW;
    }
    public static String getStUserName() {
        return stUserName;
    }
    public static String getStUserArea() {
        return stUserArea;
    }
    public static String getStUserBirth() {
        return stUserBirth;
    }
    public static String getStUserProfile() {
        return stUserProfile;
    }

}
