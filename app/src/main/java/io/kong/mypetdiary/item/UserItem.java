package io.kong.mypetdiary.item;

import android.os.Parcelable;

public class UserItem {

    static String stUserID;
    static String stUserPW;
    static String stUserSalt;
    static String stUserName;
    static String stUserArea;
    static String stUserBirth;
    static String stUserProfile;
    static Parcelable recyclerViewState;

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

    public void setStUserSalt(String stUserSalt) {
        UserItem.stUserSalt = stUserSalt;
    }

    public void setRecyclerViewState(Parcelable recyclerViewState) {
        UserItem.recyclerViewState =recyclerViewState;
    }

    public String getStUserID() {
        return stUserID;
    }
    public String getStUserPW() {
        return stUserPW;
    }
    public String getStUserName() {
        return stUserName;
    }
    public String getStUserArea() {
        return stUserArea;
    }
    public String getStUserBirth() {
        return stUserBirth;
    }
    public String getStUserProfile() {
        return stUserProfile;
    }
    public String getStUserSalt() {
        return stUserSalt;
    }
    public Parcelable getRecyclerViewState() {
        return recyclerViewState;
    }

}
