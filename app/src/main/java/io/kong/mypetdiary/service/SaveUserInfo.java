package io.kong.mypetdiary.service;


import android.content.SharedPreferences;

public class SaveUserInfo {
    public static void saveUserInfo(SharedPreferences appData, Boolean save, String stUserID, String stUserPW, String stUserSalt, String stUserName,
                                    String stUserBirth, String stUserProfile, String stUserArea, String stPetName, String stPetBirth, String stPetCome,
                                    int stPetKind) {

        SharedPreferences.Editor editor = appData.edit();

        editor.putBoolean("SAVE_LOGIN_DATA", save);

        editor.putString("user_id", stUserID);
        editor.putString("user_pw", stUserPW);
        editor.putString("user_salt", stUserSalt);
        editor.putString("user_name", stUserName);
        editor.putString("user_birth", stUserBirth);
        editor.putString("user_profile", stUserProfile);
        editor.putString("user_area", stUserArea);

        editor.putString("pet_name", stPetName);
        editor.putString("pet_birth", stPetBirth);
        editor.putString("pet_come", stPetCome);
        editor.putInt("pet_kind", stPetKind);

        editor.apply();
    }
}
