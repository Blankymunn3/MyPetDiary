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
        this.stUserID = stUserID;
    }


    public void setStUserPW(String stUserPW) {
        this.stUserPW = stUserPW;
    }


    public void setStUserName(String stUserName) {
        this.stUserName = stUserName;
    }



    public void setStUserArea(String stUserArea) {
        this.stUserArea = stUserArea;
    }


    public void setStUserBirth(String stUserBirth) {
        this.stUserBirth = stUserBirth;
    }


    public void setStUserProfile(String stUserProfile) {
        this.stUserProfile = stUserProfile;
    }

    public void setStUserSalt(String stUserSalt) {
        this.stUserSalt = stUserSalt;
    }

    public void setRecyclerViewState(Parcelable recyclerViewState) {
        this.recyclerViewState =recyclerViewState;
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
