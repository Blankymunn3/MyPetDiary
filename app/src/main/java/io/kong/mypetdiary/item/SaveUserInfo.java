package io.kong.mypetdiary.item;


import android.content.SharedPreferences;

public class SaveUserInfo {
    public static void saveUserInfo(SharedPreferences appData, String stUserID, String stUserPW, String stUserName, String stUserBirth, String stUserProfile,
                                    String stUserArea, String stPetName, String stPetBirth, String stPetCome, String stPetKind) {

        SharedPreferences.Editor editor = appData.edit();

        editor.putBoolean("SAVE_LOGIN_DATA", true);

        editor.putString("user_id", stUserID);
        editor.putString("user_pw", stUserPW);
        editor.putString("user_name", stUserName);
        editor.putString("user_birth", stUserBirth);
        editor.putString("user_profile", stUserProfile);
        editor.putString("user_area", stUserArea);

        editor.putString("pet_name", stPetName);
        editor.putString("pet_birth", stPetBirth);
        editor.putString("pet_come", stPetCome);
        editor.putString("pet_kind", stPetKind);
    }
}
